package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractItemBlock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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

	@Override
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
		if((is == null) || !(is.getItem() instanceof ItemBlock)) return;
		Block b = ((ItemBlock)is.getItem()).field_150939_a;
		if(b instanceof AbstractBlock)
			((AbstractBlock)b).addInfo(is.getItemDamage(), is.stackTagCompound, infoList);
	}
}
