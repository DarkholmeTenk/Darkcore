package io.darkcraft.darkcore.mod.multiblock;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class CombinedBlockState implements IBlockState
{
	private final IBlockState[] subStates;

	public CombinedBlockState(IBlockState... _subStates)
	{
		subStates = _subStates;
	}

	@Override
	public boolean equals(Block b, int m)
	{
		for(IBlockState ibs : subStates)
			if(ibs.equals(b, m))
				return true;
		return false;
	}

	@Override
	public boolean equals(World w, int x, int y, int z)
	{
		Block b = w.getBlock(x, y, z);
		int m = w.getBlockMetadata(x, y, z);
		return equals(b,m);
	}

	@Override
	public void set(World w, int x, int y, int z)
	{
		subStates[0].set(w, x, y, z);
	}

	@Override
	public Block getDefaultBlock()
	{
		return subStates[0].getDefaultBlock();
	}

	@Override
	public int getDefaultMeta()
	{
		return subStates[0].getDefaultMeta();
	}

}
