package io.darkcraft.darkcore.mod.handlers;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLCommonHandler;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;

public class DelayedItemHandler
{
	static
	{
		FMLCommonHandler.instance().bus().register(new DelayedItemHandler());
	}

	@ForgeSubscribe
	public void handleTicks(ServerTickEvent tick)
	{
		if(tick.phase != Phase.END) return;
		Iterator<EntityPlayer> plIter = map.keySet().iterator();
		while(plIter.hasNext())
		{
			EntityPlayer pl = plIter.next();
			List<ItemStack> items = (List<ItemStack>) map.get(pl);
			for(ItemStack is : items)
				WorldHelper.giveItemStack(pl, is);
			plIter.remove();
		}
	}

	private static Multimap<EntityPlayer, ItemStack> map = ArrayListMultimap.create();

	private static void addItem(ItemStack toAdd, List<ItemStack> existing)
	{
		for(ItemStack is : existing)
		{
			if(!WorldHelper.sameItem(toAdd, is)) continue;
			if(is.stackSize >= is.getMaxStackSize()) continue;
			int max = Math.min(toAdd.stackSize, is.getMaxStackSize() - is.stackSize);
			is.stackSize += max;
			if(max == toAdd.stackSize) return;
			toAdd.stackSize -= max;
		}
		existing.add(toAdd);
	}

	public static void addItemDrop(EntityPlayer pl, List<ItemStack> itemstack)
	{
		List<ItemStack> isMap = (List<ItemStack>) map.get(pl);
		for(ItemStack is : itemstack)
			addItem(is, isMap);
	}

	public static void clear()
	{
		map.clear();
	}
}
