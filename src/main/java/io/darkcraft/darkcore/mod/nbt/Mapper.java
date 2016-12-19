package io.darkcraft.darkcore.mod.nbt;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Mapper<T>
{
	public static final String NBTCLASSNAME = "class.name";

	/**
	 * Writes appropriate NBT data from t to nbt
	 */
	public abstract void writeToNBT(NBTTagCompound nbt, T o);

	public void writeToNBT(NBTTagCompound nbt, String id, T object)
	{
		NBTTagCompound snbt = writeToNBT(object);
		nbt.setTag(id, snbt);
	}

	public NBTTagCompound writeToNBT(T o)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt, o);
		return nbt;
	}

	public abstract T fillFromNBT(NBTTagCompound nbt, T t);

	public T fillFromNBT(NBTTagCompound nbt, String id, T t)
	{
		return fillFromNBT(nbt.getCompoundTag(id), t);
	}

	public abstract T createFromNBT(NBTTagCompound nbt, Object... arguments);

	public T readFromNBT(NBTTagCompound nbt, String id)
	{
		NBTTagCompound snbt = nbt.getCompoundTag(id);
		return createFromNBT(snbt);
	}

	public boolean shouldCreateNew()
	{
		return false;
	}

	public boolean handleSubclasses()
	{
		return false;
	}
}
