package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractItemBlock;
import net.minecraft.block.Block;

public class DefaultItemBlock extends AbstractItemBlock
{
	private final AbstractBlock b;
	public DefaultItemBlock(Block par1)
	{
		super(par1);
		if(par1 instanceof AbstractBlock)
			b = (AbstractBlock) par1;
		else
			b = null;
	}

	@Override
	protected AbstractBlock getBlock()
	{
		return b;
	}

}
