package io.darkcraft.darkcore.mod.nbt.impl;

import net.minecraft.nbt.NBTTagCompound;

public class EnumMapper<T extends Enum> extends PrimMapper<T>
{
	private final Class<T> enumClass;
	private final T[] values;

	public EnumMapper(Class<T> clazz)
	{
		this.enumClass = clazz;
		this.values = clazz.getEnumConstants();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String id, T t)
	{
		nbt.setInteger(id, t.ordinal());
	}

	@Override
	public T readFromNBT(NBTTagCompound nbt, String id)
	{
		return values[nbt.getInteger(id)];
	}

	@Override
	public boolean shouldCreateNew()
	{
		return true;
	}

}
