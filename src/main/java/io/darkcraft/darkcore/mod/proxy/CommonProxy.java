package io.darkcraft.darkcore.mod.proxy;

import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.world.World;

public class CommonProxy
{
	public World getWorld(int id)
	{
		return WorldHelper.getWorldServer(id);
	}
}
