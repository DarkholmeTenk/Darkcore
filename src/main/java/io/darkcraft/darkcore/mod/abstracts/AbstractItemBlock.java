package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.interfaces.IColorableBlock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractItemBlock extends ItemBlock
{
	Block	bID;

	public AbstractItemBlock(Block par1)
	{
		super(par1);
		bID = par1;
		if(!(getBlock() instanceof IColorableBlock))
			setHasSubtypes(true);
		else
			setHasSubtypes(false);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	protected abstract AbstractBlock getBlock();

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		AbstractBlock block = getBlock();
		if (block != null)
		{
			return block.getUnlocalizedName(itemStack.getItemDamage());
		}
		return bID.getUnlocalizedName();
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
			float hitY, float hitZ, int metadata)
	{
		if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
		{
			NBTTagCompound tag = stack.stackTagCompound;
			if (tag != null)
			{
				if (tag.hasKey("x"))
				{
					tag.removeTag("x");
					tag.removeTag("y");
					tag.removeTag("z");
					tag.removeTag("id");
					tag.setBoolean("placed", true);
				}
				TileEntity te = world.getTileEntity(x, y, z);
				if (te != null)
					te.readFromNBT(tag);
			}
			return true;
		}
		return false;
	}

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
		if(is == null)
			return 16777215;
		int m = is.getItemDamage();
		Block b = getBlock();
		if(b instanceof IColorableBlock)
		{
			if((m >= 0) && (m < ItemDye.field_150922_c.length))
				return ItemDye.field_150922_c[m];
		}
        return 16777215;
    }

}
