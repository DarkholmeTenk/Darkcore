package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreTeleporter;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;

public class TeleportHelper
{
	/**
	 * Transfers ent to newDimension hopefully skipping any end teleportation glitches or anything
	 * @param ent the entity to teleport
	 * @param newDimension the id of the dimension to teleport to
	 * @param newX the new X coord (only works on non-players)
	 * @param newY the new Y coord (only works on non-players)
	 * @param newZ the new Z coord (only works on non-players)
	 * @return a new entity if the transfer resulted in a new entity getting created
	 */
	public static Entity transferEntityToDimension(Entity ent, int newDimension, double newX, double newY, double newZ)
	{
		if(ent instanceof IBossDisplayData)
			return ent;
		//if (ServerHelper.isClient()) return ent;
		ServerConfigurationManager conf = ServerHelper.getConfigManager();
		int oldDimension = WorldHelper.getWorldID(ent);
		WorldServer dest = WorldHelper.getWorldServer(newDimension);
		WorldServer source = WorldHelper.getWorldServer(oldDimension);
		if ((dest == null) || (source == null)) return ent;
		if (ent.isRiding())
			ent.mountEntity(null);
		if (ent instanceof EntityPlayerMP)
		{
			EntityPlayerMP pl = (EntityPlayerMP) ent;
			float xp = pl.experience;
			int xpT = pl.experienceTotal;
			int xpL = pl.experienceLevel;
			if(source.provider instanceof WorldProviderEnd)
			{
				conf.transferPlayerToDimension(pl, newDimension, DarkcoreTeleporter.i);
				pl.dimension = 0;
			}
			conf.transferPlayerToDimension(pl, newDimension, DarkcoreTeleporter.i);
			pl.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0F));
			pl.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(xp, xpT, xpL));
		}
		else
		{
			//conf.transferEntityToWorld(ent, newDimension, source, dest, DarkcoreTeleporter.i);
			// ent.travelToDimension(newDimension);
			// conf.transferEntityToWorld(ent, newDimension, source, dest, TardisMod.teleporter);
			Entity entity = EntityList.createEntityByName(EntityList.getEntityString(ent), dest);
			if (entity != null)
			{
				dest.getBlock((int)newX, (int)newY, (int)newZ); //Should force chunk to generate
				entity.copyDataFrom(ent, true);
				entity.posX = newX; //Set the entity position so it's in the right chunk
				entity.posY = newY;
				entity.posZ = newZ;
				entity.forceSpawn = true; //Force it to spawn
				entity.copyDataFrom(ent, true);
				boolean didSpawn = dest.spawnEntityInWorld(entity);
				if(!didSpawn)
					System.err.println("Failed to spawn entity");
				else
					entity.dimension = newDimension;
				ent.isDead = didSpawn;
				source.resetUpdateEntityTick();
				dest.resetUpdateEntityTick();
				return entity;
			}
		}
		return ent;
	}

	/**
	 * Teleports ent to worldID at x,y,z
	 * @param ent
	 * @param worldID
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void teleportEntity(Entity ent, int worldID, double x, double y, double z)
	{
		teleportEntity(ent, worldID, x, y, z, 0);
	}

	/**
	 * Teleports ent to worldID at x,y,z with rotation rot
	 * @param ent
	 * @param worldID
	 * @param x
	 * @param y
	 * @param z
	 * @param rot
	 */
	public static void teleportEntity(Entity ent, int worldID, double x, double y, double z, double rot)
	{
		if(ent instanceof IBossDisplayData)
			return;
		System.out.println("[TTH]Teleport request: " + worldID + " > " + x + "," + y + "," + z);
		MinecraftServer serv = MinecraftServer.getServer();
		if (ServerHelper.isServer() && (serv != null) && (ent instanceof EntityLivingBase))
		{
			/*
			 * WorldServer nW = WorldHelper.getWorldServer(worldID); if(nW.provider instanceof TardisWorldProvider && ServerHelper.isServer()) { Packet dP = TardisDimensionRegistry.getPacket(); Helper.getConfMan().sendToAllNear(ent.posX, ent.posY, ent.posZ, 100, Helper.getWorldID(ent), dP); }
			 */

			if (WorldHelper.getWorldID(ent.worldObj) != worldID)
			{
				ent = transferEntityToDimension(ent, worldID, x, y, z);
			}
			((EntityLivingBase) ent).fallDistance = 0;
			((EntityLivingBase) ent).motionX = 0;
			((EntityLivingBase) ent).motionY = 0;
			((EntityLivingBase) ent).motionZ = 0;
			((EntityLivingBase) ent).velocityChanged = true;
			((EntityLivingBase) ent).setPositionAndRotation(x, y, z, (float) rot, ent.rotationPitch);
			((EntityLivingBase) ent).setPositionAndUpdate(x, y, z);
		}
	}

	/**
	 * Teleports ent to pos with rotation 0
	 * @param ent
	 * @param pos
	 */
	public static void teleportEntity(Entity ent, SimpleDoubleCoordStore pos)
	{
		teleportEntity(ent,pos,WorldHelper.getWorldID(ent) == pos.world ? ent.rotationYaw : 0);
	}

	/**
	 * Teleports ent to pos with rotation rot
	 * @param ent
	 * @param pos
	 * @param rot
	 */
	public static void teleportEntity(Entity ent, SimpleDoubleCoordStore pos, double rot)
	{
		teleportEntity(ent, pos.world, pos.x, pos.y, pos.z, rot);
	}

	/**
	 * Teleports ent to worldID but keeps their x,y,z the same
	 * @param ent
	 * @param worldID
	 */
	public static void teleportEntity(Entity ent, int worldID)
	{
		teleportEntity(ent, worldID, ent.posX, ent.posY, ent.posZ);
	}

	/**
	 * Teleports ent to the overworld spawn, a relatively safe location
	 * @param ent
	 */
	public static void teleportEntityToOverworldSpawn(Entity ent)
	{
		World overworld = WorldHelper.getWorld(0);
		if(overworld == null)
			return;
		ChunkCoordinates scc = overworld.getSpawnPoint();
		teleportEntity(ent, 0, scc.posX+0.5, scc.posY+1,scc.posZ+0.5);
	}
}
