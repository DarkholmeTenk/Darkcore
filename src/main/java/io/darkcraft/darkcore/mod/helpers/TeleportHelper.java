package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;

public class TeleportHelper
{
	public static void transferEntityToDimension(Entity ent, int newDimension, double newX, double newY, double newZ)
	{
		if (ServerHelper.isClient()) return;
		ServerConfigurationManager conf = ServerHelper.getConfigManager();
		int oldDimension = WorldHelper.getWorldID(ent);
		WorldServer dest = WorldHelper.getWorldServer(newDimension);
		WorldServer source = WorldHelper.getWorldServer(oldDimension);
		if ((dest == null) || (source == null)) return;
		if (ent.isRiding())
			ent.mountEntity(null);
		if (ent instanceof EntityPlayerMP)
		{
			EntityPlayerMP pl = (EntityPlayerMP) ent;
			conf.transferPlayerToDimension(pl, newDimension, DarkcoreTeleporter.i);
			if (source.provider instanceof WorldProviderEnd) ent = ServerHelper.getConfigManager().respawnPlayer(pl, newDimension, true);
			pl.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(pl.experience, pl.experienceTotal, pl.experienceLevel));
			Entity entity = EntityList.createEntityByName(EntityList.getEntityString(ent), dest);
			if (entity != null)
			{
				entity.copyDataFrom(ent, true);
				dest.spawnEntityInWorld(entity);
				ent.isDead = true;
				source.resetUpdateEntityTick();
				dest.resetUpdateEntityTick();
			}
		}
		else
		{
			ServerHelper.getConfigManager().transferEntityToWorld(ent, newDimension, source, dest);
			// ent.travelToDimension(newDimension);
			// conf.transferEntityToWorld(ent, newDimension, source, dest, TardisMod.teleporter);
		}
	}

	public static void teleportEntity(Entity ent, int worldID, double x, double y, double z)
	{
		teleportEntity(ent, worldID, x, y, z, 0);
	}

	public static void teleportEntity(Entity ent, int worldID, double x, double y, double z, double rot)
	{
		System.out.println("[TTH]Teleport request: " + worldID + " > " + x + "," + y + "," + z);
		MinecraftServer serv = MinecraftServer.getServer();
		if (ServerHelper.isServer() && (serv != null) && (ent instanceof EntityLivingBase))
		{
			/*
			 * WorldServer nW = WorldHelper.getWorldServer(worldID); if(nW.provider instanceof TardisWorldProvider && ServerHelper.isServer()) { Packet dP = TardisDimensionRegistry.getPacket(); Helper.getConfMan().sendToAllNear(ent.posX, ent.posY, ent.posZ, 100, Helper.getWorldID(ent), dP); }
			 */

			if (WorldHelper.getWorldID(ent.worldObj) != worldID)
			{
				transferEntityToDimension(ent, worldID, x, y, z);
			}
			((EntityLivingBase) ent).fallDistance = 0;
			((EntityLivingBase) ent).motionX = 0;
			((EntityLivingBase) ent).motionY = 0;
			((EntityLivingBase) ent).motionZ = 0;
			((EntityLivingBase) ent).velocityChanged = true;
			((EntityLivingBase) ent).setPositionAndRotation(x, y, z, (float) rot, 0F);
			((EntityLivingBase) ent).setPositionAndUpdate(x, y, z);
		}
	}

	public static void teleportEntity(Entity ent, int worldID)
	{
		teleportEntity(ent, worldID, ent.posX, ent.posY, ent.posZ);
	}
}
