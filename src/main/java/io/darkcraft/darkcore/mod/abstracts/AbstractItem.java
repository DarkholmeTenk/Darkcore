package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.handlers.RecipeHandler;
import io.darkcraft.darkcore.mod.interfaces.IRecipeContainer;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractItem extends Item implements IRecipeContainer
{
	private IIcon			iconBuffer;
	private String			unlocalizedFragment;
	private final String	modName;

	private String[]		subNames	= null;
	private IIcon[]			subIcons	= null;

	public AbstractItem(String mod)
	{
		modName = mod;
		CreativeTabs tab = DarkcoreMod.getCreativeTab(mod);
		if (tab != null) setCreativeTab(tab);
		RecipeHandler.addRecipeContainer(this);
	}

	public AbstractItem register()
	{
		GameRegistry.registerItem(this, getUnlocalizedName());
		return this;
	}

	public abstract void initRecipes();

	public void setSubNames(String... _subNames)
	{
		subNames = _subNames;
		if ((subNames != null) && (subNames.length > 1)) setHasSubtypes(true);
	}

	@Override
	public Item setUnlocalizedName(String unlocal)
	{
		Item orig = super.setUnlocalizedName(unlocal);
		unlocalizedFragment = unlocal;
		return orig;
	}

	@Override
	public String getUnlocalizedName()
	{
		return "item." + modName + "." + unlocalizedFragment;
	}

	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		if (subNames == null)
			return getUnlocalizedName();
		else
		{
			int damage = is.getItemDamage();
			if ((damage >= 0) && (damage < subNames.length))
				return getUnlocalizedName() + "." + subNames[damage];
			else
				return getUnlocalizedName() + ".Malformed";
		}
	}

	public String[] getSubNamesForIcons()
	{
		return subNames;
	}

	public String[] getSubNamesForNEI()
	{
		return subNames;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ir)
	{
		String[] subNames = getSubNamesForIcons();
		if (DarkcoreMod.debugText) System.out.println("[TAI]Registering icon " + unlocalizedFragment);
		if (subNames != null)
		{
			subIcons = new IIcon[subNames.length];
			for (int i = 0; i < subNames.length; i++)
				subIcons[i] = ir.registerIcon(modName + ":" + unlocalizedFragment + "." + subNames[i]);
		}
		else
			iconBuffer = ir.registerIcon(modName + ":" + unlocalizedFragment);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int damage)
	{
		String[] subNames = getSubNamesForIcons();
		if (subNames == null)
			return iconBuffer;
		else if ((damage >= 0) && (damage < subNames.length)) return subIcons[damage];
		return iconBuffer;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list)
	{
		String[] subNames = getSubNamesForNEI();
		if (subNames == null)
			list.add(new ItemStack(par1, 1, 0));
		else
		{
			for (int i = 0; i < subNames.length; i++)
				list.add(new ItemStack(par1, 1, i));
		}
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

}
