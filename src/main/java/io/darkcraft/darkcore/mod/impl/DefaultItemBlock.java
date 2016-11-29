package io.darkcraft.darkcore.mod.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractItemBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class DefaultItemBlock extends AbstractItemBlock
{
	private final AbstractBlock b;
	public DefaultItemBlock(int par1)
	{
		super(par1);
		Block d = Block.blocksList[par1];
		if(d instanceof AbstractBlock)
			b = (AbstractBlock) d;
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
		Block b = Block.blocksList[is.itemID];
		if(b instanceof AbstractBlock)
			((AbstractBlock)b).addInfo(is.getItemDamage(), is.stackTagCompound, infoList);
	}
}
