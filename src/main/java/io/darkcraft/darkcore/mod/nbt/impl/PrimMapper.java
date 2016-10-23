package io.darkcraft.darkcore.mod.nbt.impl;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PrimMapper<T> extends Mapper<T>
{
	@Override
	public void writeToNBT(NBTTagCompound nbt, Object o)
	{
		writeToNBT(nbt, "value", o);
	}

	@Override
	public abstract void writeToNBT(NBTTagCompound nbt, String id, Object t);

	@Override
	public T fillFromNBT(NBTTagCompound nbt, Object t){return (T) t;}

	@Override
	public abstract T readFromNBT(NBTTagCompound nbt, String id);

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		return readFromNBT(nbt, "value");
	}

	@Override
	public boolean shouldCreateNew() { return true; }
}
