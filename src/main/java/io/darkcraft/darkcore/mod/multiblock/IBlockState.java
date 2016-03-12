package io.darkcraft.darkcore.mod.multiblock;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IBlockState
{
	public boolean equals(Block b, int m);

	public boolean equals(World w, int x, int y, int z);

	public void set(World w, int x, int y, int z);
}
