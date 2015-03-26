package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SoundHelper
{
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
		if (!ServerHelper.isServer())
			return;
		NBTTagCompound data = new NBTTagCompound();
		World w = WorldHelper.getWorld(dim);
		// No point playing a sound to a world with no entities in
		if (w != null)
			if (w.playerEntities != null && w.playerEntities.size() == 0)
				return;
		if (!sound.contains(":"))
			sound = "tardismod:" + sound;
		data.setString("sound", sound);
		data.setInteger("world", dim);
		data.setInteger("x", x);
		data.setInteger("y", y);
		data.setInteger("z", z);
		data.setFloat("vol", vol);
		System.out.println("PlayingSound:" + sound);
		if (speed != 1)
			data.setFloat("spe", speed);
		DataPacket packet = new DataPacket(data, (byte)0);
		DarkcoreMod.networkChannel.sendToDimension(packet, dim);
	}
}
