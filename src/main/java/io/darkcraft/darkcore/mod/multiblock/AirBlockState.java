package io.darkcraft.darkcore.mod.multiblock;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class AirBlockState implements IBlockState
{
	public static AirBlockState i = new AirBlockState();
	private AirBlockState(){}

	@Override
	public boolean equals(Block b, int m)
	{
		if((b == null) || (b == Blocks.air))
			return true;
		return false;
	}

	@Override
	public boolean equals(World w, int x, int y, int z)
	{
		return w.isAirBlock(x, y, z);
	}

	@Override
	public void set(World w, int x, int y, int z)
	{
		w.setBlockToAir(x, y, z);
	}

}
