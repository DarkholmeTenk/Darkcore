package io.darkcraft.darkcore.mod.helpers;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper
{
	public static void writeUUIDToNBT(UUID uuid, NBTTagCompound nbt, String id)
	{
		nbt.setLong(id+".m", uuid.getMostSignificantBits());
		nbt.setLong(id+".l", uuid.getLeastSignificantBits());
	}

	public static UUID readUUIDFromNBT(NBTTagCompound nbt, String id)
	{
		long m = nbt.getLong(id+".m");
		long l = nbt.getLong(id+".l");
		return new UUID(m, l);
	}

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
			{
				items[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("i"+i));
				if(items[i].stackSize <= 0)
					items[i] = null;
			}
			else
				items[i] = null;
		}
	}
}
