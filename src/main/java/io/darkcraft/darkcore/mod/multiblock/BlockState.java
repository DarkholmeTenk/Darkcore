package io.darkcraft.darkcore.mod.multiblock;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockState implements IBlockState
{
	private final Block	b;
	private final int	m;

	/**
	 * @param _b
	 *            the block which should be considered in this block state
	 * @param _m
	 *            the metadata which should be considered in this block state or -1 if its unimportant
	 */
	public BlockState(Block _b, int _m)
	{
		b = _b;
		m = _m;
	}

	/**
	 * @param _b
	 *            the block which should be considered in this block state
	 */
	public BlockState(Block _b)
	{
		this(_b,-1);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((b == null) ? 0 : b.hashCode());
		result = (prime * result) + m;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof BlockState)) return false;
		BlockState other = (BlockState) obj;
		if ((b == other.b) && (m == other.m)) return true;
		return false;
	}

	@Override
	public boolean equals(Block block, int meta)
	{
		 return (block == b) && ((m == -1) || (meta == m));
	}

	@Override
	public boolean equals(World w, int x, int y, int z)
	{
		Block block = w.getBlock(x, y, z);
		int meta = w.getBlockMetadata(x, y, z);
		return equals(block,meta);
	}

	@Override
	public void set(World w, int x, int y, int z)
	{
		w.setBlock(x, y, z, b, m==-1?0:m,3);
	}

	@Override
	public String toString()
	{
		return b.getClass().getSimpleName();
	}
}
