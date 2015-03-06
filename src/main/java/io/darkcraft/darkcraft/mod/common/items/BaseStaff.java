package io.darkcraft.darkcraft.mod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcraft.mod.common.spellsystem.BaseSpell;

public class BaseStaff extends AbstractItem
{
	public BaseStaff()
	{
		setUnlocalizedName("staffBase");
	}

	@Override
	public void initRecipes()
	{
	}
	
	public BaseSpell getSpell(ItemStack stack)
	{
		if(stack == null || stack.stackTagCompound == null)
			return null;
		if(!stack.stackTagCompound.hasKey("currentSpell"))
			return null;
		NBTTagCompound spell = stack.stackTagCompound.getCompoundTag("currentSpell");
		return BaseSpell.readFromNBT(spell);
	}
	
	private void cast(ItemStack stack, EntityPlayer player)
	{
		if(!ServerHelper.isServer())
			return;
		BaseSpell spell = getSpell(stack);
		if(spell != null)
		{
			System.out.println("Spellcast");
			spell.cast(player);
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
			float hitY, float hitZ)
	{
		cast(stack,player);
		return true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer player)
	{
		cast(is,player);
		return is;
	}

}
