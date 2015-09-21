package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.interfaces.IColorableBlock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SimulacrumItemBlock extends AbstractItemBlock
{

	AbstractBlock	bID;

	public SimulacrumItemBlock(Block par1)
	{
		super(par1);
		bID = (AbstractBlock) par1;
		setHasSubtypes(bID.getNumSubNames() > 1);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return bID.getUnlocalizedName(itemStack.getItemDamage());
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
	}

	@SuppressWarnings("rawtypes")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List infoList, boolean par4)
	{
		super.addInformation(is, player, infoList, par4);
		addInfo(is, player, infoList);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack is, int s)
	{
		if (is == null) return 16777215;
		int m = is.getItemDamage();
		Block b = getBlock();
		if (b instanceof IColorableBlock)
		{
			if ((m >= 0) && (m < ItemDye.field_150922_c.length)) return ItemDye.field_150922_c[m];
		}
		return 16777215;
	}

	@Override
	protected AbstractBlock getBlock()
	{
		return bID;
	}

}
