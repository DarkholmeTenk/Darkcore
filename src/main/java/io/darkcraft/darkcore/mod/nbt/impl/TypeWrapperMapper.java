package io.darkcraft.darkcore.mod.nbt.impl;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import net.minecraft.nbt.NBTTagCompound;

public class TypeWrapperMapper<T> extends Mapper<T>
{
	private final Class<T> c;
	private final Mapper<T> subMapper;

	public TypeWrapperMapper(Class<T> c, Mapper<T> subMapper)
	{
		this.c = c;
		this.subMapper = subMapper;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, T o)
	{
		nbt.setString(NBTCLASSNAME, c.getName());
		subMapper.writeToNBT(nbt, o);
	}

	@Override
	public T fillFromNBT(NBTTagCompound nbt, T t)
	{
		return subMapper.fillFromNBT(nbt, t);
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		return subMapper.createFromNBT(nbt, arguments);
	}

}
