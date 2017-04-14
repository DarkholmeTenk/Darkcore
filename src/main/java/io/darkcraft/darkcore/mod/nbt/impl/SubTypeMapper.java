package io.darkcraft.darkcore.mod.nbt.impl;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;

public class SubTypeMapper<T> extends Mapper<T>
{
	private final SerialisableType type;
	private static final String OBJ_KEY = "obj";

	public SubTypeMapper(SerialisableType type)
	{
		this.type = type;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, T o)
	{
		if(o == null)
			return;
		Class<T> clazz = (Class<T>) o.getClass();
		String clazzName = o.getClass().getName();
		nbt.setString(NBTCLASSNAME, clazzName);
		Mapper<T> mapper = NBTHelper.getMapper(clazz, type);
		if(mapper == null)
			throw new RuntimeException("No mapper could be found for class " + clazzName);
		mapper.writeToNBT(nbt, OBJ_KEY, o);
	}

	@Override
	public T fillFromNBT(NBTTagCompound nbt, T o)
	{
		if(o == null)
			return createFromNBT(nbt);
		Class<T> clazz = (Class<T>) o.getClass();
		Mapper<T> mapper = NBTHelper.getMapper(clazz, type);
		if(mapper == null)
			throw new RuntimeException("No mapper could be found for class " + clazz.getName());
		return mapper.fillFromNBT(nbt, OBJ_KEY, o);
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		if(!nbt.hasKey(NBTCLASSNAME))
			return null;
		String clazzName = nbt.getString(NBTCLASSNAME);
		try
		{
			Class<T> clazz = (Class<T>) Class.forName(clazzName);
			Mapper<T> mapper = NBTHelper.getMapper(clazz, type);
			if(mapper == null)
				throw new RuntimeException("No mapper could be found fod class " + clazz.getName());
			return mapper.readFromNBT(nbt, OBJ_KEY);
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

}
