package io.darkcraft.darkcraft.mod.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcraft.mod.common.tileent.MultiBlockCoreTE;

public class MultiBlockCoreBlock extends AbstractBlockContainer
{

	public MultiBlockCoreBlock(boolean render, String sm)
	{
		super(render, sm);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new MultiBlockCoreTE();
	}

	@Override
	public void initData()
	{
		setBlockName("MultiBlockCore");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

}
