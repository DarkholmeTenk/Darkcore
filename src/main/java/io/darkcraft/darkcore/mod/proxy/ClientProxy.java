package io.darkcraft.darkcore.mod.proxy;

import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy
{
	@Override
	public World getWorld(int id)
	{
		World w = Minecraft.getMinecraft().theWorld;
		if(w != null && WorldHelper.getWorldID(w) == id)
			return w;
		return null;
	}
}
