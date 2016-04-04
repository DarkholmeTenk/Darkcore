package io.darkcraft.darkcore.mod.helpers;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper
{
	public static boolean matches(ItemStack is, String pattern)
	{
		if((is == null) || (pattern == null)) return false;
		int[] ids = OreDictionary.getOreIDs(is);
		for(int id : ids)
			if(pattern.equals(OreDictionary.getOreName(id))) return true;
		return false;
	}

	public static boolean matchesRegex(ItemStack is, String pattern)
	{
		if((is == null) || (pattern == null)) return false;
		int[] ids = OreDictionary.getOreIDs(is);
		for(int id : ids)
			if(OreDictionary.getOreName(id).matches(pattern)) return true;
		return false;
	}

	public static ItemStack getItemStack(String ore, int num)
	{
		if(!OreDictionary.doesOreNameExist(ore)) return null;
		List<ItemStack> iss = OreDictionary.getOres(ore);
		if(iss.size() == 0) return null;
		ItemStack is = iss.get(0).copy();
		is.stackSize = num;
		return is;
	}
}
