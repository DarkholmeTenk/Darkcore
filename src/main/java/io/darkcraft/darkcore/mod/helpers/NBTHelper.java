package io.darkcraft.darkcore.mod.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper
{
	public static void writeItemsToNBT(ItemStack[] items, NBTTagCompound nbt)
	{
		writeItemsToNBT(items, 0, items.length, nbt);
	}

	public static void writeItemsToNBT(ItemStack[] items, int from, int to, NBTTagCompound nbt)
	{
		for(int i = from; i < to; i++)
		{
			if(items[i] != null)
			{
				NBTTagCompound nbtI = new NBTTagCompound();
				items[i].writeToNBT(nbtI);
				nbt.setTag("i"+i,nbtI);
			}
		}
	}

	public static void readItemsFromNBT(ItemStack[] items, NBTTagCompound nbt)
	{
		readItemsFromNBT(items, 0, items.length, nbt);
	}

	public static void readItemsFromNBT(ItemStack[] items, int from, int to, NBTTagCompound nbt)
	{
		for(int i = from; i < to; i++)
		{
			if(nbt.hasKey("i"+i))
				items[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("i"+i));
			else
				items[i] = null;
		}
	}
}
