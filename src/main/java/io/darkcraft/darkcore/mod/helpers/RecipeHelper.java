package io.darkcraft.darkcore.mod.helpers;

import java.util.Map;

import gnu.trove.map.hash.THashMap;
import io.darkcraft.darkcore.mod.datastore.Pair;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class RecipeHelper
{
	private static Map<Pair<Block,Integer>,Pair<Block,Integer>> blockSmeltMap = new THashMap();

	private static Pair<Block,Integer> addBlockSmelt(Pair<Block, Integer> in, Pair<Block, Integer> out)
	{
		blockSmeltMap.put(in, out);
		return out;
	}

	public static Pair<Block,Integer> getSmeltResult(Block in, int meta)
	{
		if(in == null) return null;
		Pair<Block,Integer> key = new Pair(in,meta);
		if(blockSmeltMap.containsKey(key))
			return blockSmeltMap.get(key);
		ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(in,1,meta));
		if(result == null)
			return addBlockSmelt(key, null);
		Item i = result.getItem();
		if(i instanceof ItemBlock)
			return addBlockSmelt(key, new Pair(((ItemBlock)i).field_150939_a,result.getItemDamage()));
		return addBlockSmelt(key, null);
	}
}
