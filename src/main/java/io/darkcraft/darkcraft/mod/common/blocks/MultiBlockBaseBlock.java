package io.darkcraft.darkcraft.mod.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcraft.mod.common.tileent.MultiBlockBaseTE;

public class MultiBlockBaseBlock extends AbstractBlockContainer
{

	public MultiBlockBaseBlock(boolean render, String sm)
	{
		super(render, sm);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MultiBlockBaseTE();
	}

	@Override
	public void initData()
	{
		setBlockName("multiblockBaseBlock");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
