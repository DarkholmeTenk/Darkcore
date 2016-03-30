package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.handlers.packets.SoundPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SoundHelper
{
	public static void playSound(Entity ent, String sound, float vol, float pitch)
	{
		playSound(ent.worldObj, MathHelper.floor(ent.posX), MathHelper.floor(ent.posY), MathHelper.floor(ent.posZ), sound, vol, pitch);
	}

	public static void playSound(TileEntity te, String sound, float vol)
	{
		playSound(WorldHelper.getWorldID(te.getWorldObj()), te.xCoord, te.yCoord, te.zCoord, sound, vol);
	}

	public static void playSound(TileEntity te, String sound, float vol, float speed)
	{
		playSound(WorldHelper.getWorldID(te.getWorldObj()), te.xCoord, te.yCoord, te.zCoord, sound, vol, speed);
	}

	public static void playSound(World w, int x, int y, int z, String sound, float vol)
	{
		int dim = WorldHelper.getWorldID(w);
		playSound(dim, x, y, z, sound, vol);
	}

	public static void playSound(World w, int x, int y, int z, String sound, float vol, float speed)
	{
		int dim = WorldHelper.getWorldID(w);
		playSound(dim, x, y, z, sound, vol, speed);
	}

	public static void playSound(int dim, int x, int y, int z, String sound, float vol)
	{
		playSound(dim, x, y, z, sound, vol, 1);
	}

	public static void playSound(int dim, int x, int y, int z, String sound, float vol, float speed)
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("x", x);
		data.setInteger("y", y);
		data.setInteger("z", z);
		playSound(data, dim, sound, vol, speed);
	}

	public static void playSound(int dim, String sound, float vol, float speed)
	{
		NBTTagCompound data = new NBTTagCompound();
		playSound(data, dim, sound, vol, speed);
	}

	private static void playSound(NBTTagCompound data, int dim, String sound, float vol, float speed)
	{
		World w = WorldHelper.getWorld(dim);
		// No point playing a sound to a world with no entities in
		if (w != null) if ((w.playerEntities != null) && (w.playerEntities.size() == 0)) return;
		if (!sound.contains(":")) return;
		data.setString("sound", sound);
		data.setInteger("world", dim);
		data.setFloat("vol", vol);
		if (speed != 1) data.setFloat("spe", speed);
		if(ServerHelper.isServer())
		{
			DataPacket packet = new DataPacket(data, SoundPacketHandler.disc);
			DarkcoreMod.networkChannel.sendToDimension(packet, dim);
		}
		else
		{
			DarkcoreMod.soundPacketHandler.handleData(data);
		}
	}

	public static void playSound(SimpleDoubleCoordStore pos, String sound, float vol)
	{
		playSound(pos.world, pos.iX, pos.iY, pos.iZ, sound, vol);
	}

	public static void playSound(SimpleCoordStore pos, String sound, float vol)
	{
		playSound(pos.world, pos.x, pos.y, pos.z, sound, vol);
	}
}
