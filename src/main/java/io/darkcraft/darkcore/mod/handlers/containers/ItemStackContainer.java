package io.darkcraft.darkcore.mod.handlers.containers;

import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Provides a syncable itemstack container which will maintain an EntityItem for rendering purposes.<br>
 *
 * The itemstack contained is mutable.
 * @author dark
 *
 */
@NBTSerialisable
public class ItemStackContainer
{
	private static IEntityItemInitialiser defaultCallback = null;
	public static ItemStackContainer[] getArray(int length){ return getArray(length, defaultCallback); }

	public static ItemStackContainer[] getArray(int length, IEntityItemInitialiser callback)
	{
		ItemStackContainer[] newArr = new ItemStackContainer[length];
		for(int i = 0; i < length; i++)
			newArr[i] = getContainer(callback);
		return newArr;
	}

	public static ItemStackContainer getContainer(IEntityItemInitialiser callback)
	{
		return new ItemStackContainer(callback);
	}

	public static ItemStackContainer getContainer(){ return getContainer(defaultCallback); }

	@NBTProperty
	private ItemStack is;

	private EntityItem ei;
	private final IEntityItemInitialiser callback;

	private ItemStackContainer(IEntityItemInitialiser initer)
	{
		callback = initer;
	}

	public ItemStack is()
	{
		return is;
	}

	public void setIS(ItemStack newIS)
	{
		is = newIS;
	}

	public boolean decr(int count)
	{
		if(is == null) return false;
		else if(is.stackSize < count) return false;
		else if(is.stackSize > count) is.stackSize-= count;
		else setIS(null);
		return true;
	}

	public EntityItem ei()
	{
		if(ServerHelper.isServer()) return null;
		if(is == null)
			return null;
		else
		{
			ItemStack eiI = null;
			if(ei != null)
				eiI = ei.getEntityItem();
			if(!WorldHelper.sameItem(is, eiI))
			{
				ItemStack tI = is.copy();
				tI.stackSize = 1;
				ei = new EntityItem(WorldHelper.getClientWorld(),0,0,0,tI);
				if(callback != null)
					callback.initEI(ei);
			}
		}
		return ei;
	}

	public void tick()
	{
		if(ei == null) return;
		ei.age++;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		if(is == null) return;
		is.writeToNBT(nbt);
	}

	public void writeToNBT(NBTTagCompound nbt, String id)
	{
		if(is == null) return;
		NBTTagCompound snbt = new NBTTagCompound();
		writeToNBT(snbt);
		nbt.setTag(id, snbt);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasNoTags())
			setIS(null);
		else
			setIS(ItemStack.loadItemStackFromNBT(nbt));
	}

	public void readFromNBT(NBTTagCompound nbt, String id)
	{
		if(!nbt.hasKey(id))
		{
			setIS(null);
			return;
		}
		else
			readFromNBT(nbt.getCompoundTag(id));
	}

	public static void writeToNBT(NBTTagCompound nbt, String id, ItemStackContainer[] array)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < array.length; i++)
		{
			NBTTagCompound snbt = new NBTTagCompound();
			array[i].writeToNBT(snbt);
			list.appendTag(snbt);
		}
		nbt.setTag(id, list);
	}

	public static void readFromNBT(NBTTagCompound nbt, String id, ItemStackContainer[] array)
	{
		boolean listFull = nbt.hasKey(id);
		NBTTagList list = null;
		if(listFull)
		{
			list = nbt.getTagList(id, 10);
			listFull = list.tagCount() == array.length;
		}
		for(int i = 0; i < array.length; i++)
			if(listFull)
				array[i].readFromNBT(list.getCompoundTagAt(i));
			else
				array[i].setIS(null);
	}

	public static void tick(ItemStackContainer[] array)
	{
		if(ServerHelper.isServer()) return;
		for(ItemStackContainer isc : array)
			isc.tick();
	}

	public static interface IEntityItemInitialiser
	{
		public void initEI(EntityItem ei);
	}
}
