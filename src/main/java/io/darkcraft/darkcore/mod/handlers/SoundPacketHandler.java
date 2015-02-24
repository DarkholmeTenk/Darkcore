package io.darkcraft.darkcore.mod.handlers;

import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SoundPacketHandler implements IDataPacketHandler
{
	public void handleData(NBTTagCompound data)
	{
		if(ServerHelper.isServer())
			return;
		if(data != null && data.hasKey("sound"))
		{
			String sound = data.getString("sound");
			int dim = data.getInteger("world");
			int x = data.getInteger("x");
			int y = data.getInteger("y");
			int z = data.getInteger("z");
			float vol = data.getFloat("vol");
			float speed = 1;
			if(data.hasKey("spe"))
				speed = data.getFloat("spe");
			World w = WorldHelper.getWorld(dim);
			if(w != null)
			{
				System.out.println("[TSP]Attempting to play sound packet: " + sound +"," + vol+","+speed);
				w.playSound(x, y, z, sound, vol, speed, true);
			}
		}
	}
}
