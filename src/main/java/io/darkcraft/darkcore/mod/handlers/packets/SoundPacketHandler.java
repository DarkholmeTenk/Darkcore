package io.darkcraft.darkcore.mod.handlers.packets;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SoundPacketHandler implements IDataPacketHandler
{
	public static final String disc = "core.sound";
	@Override
	public void handleData(NBTTagCompound data)
	{
		if (ServerHelper.isServer()) return;
		if ((data != null) && data.hasKey("sound"))
		{
			String sound = data.getString("sound");
			if(DarkcoreMod.bannedSounds.contains(sound))
				return;
			int dim = data.getInteger("world");

			float vol = data.getFloat("vol");
			float speed = 1;
			if (data.hasKey("spe")) speed = data.getFloat("spe");
			World w = WorldHelper.getWorld(dim);
			if (w != null)
			{
				if (data.hasKey("x"))
				{
					int x = data.getInteger("x");
					int y = data.getInteger("y");
					int z = data.getInteger("z");
					w.playSound(x, y, z, sound, vol, speed, true);
				}
				else
				{
					Minecraft.getMinecraft().thePlayer.playSound(sound, vol, speed);
				}
			}
		}
	}
}
