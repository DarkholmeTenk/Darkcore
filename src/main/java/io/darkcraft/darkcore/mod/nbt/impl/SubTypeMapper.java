package io.darkcraft.darkcore.mod.nbt.impl;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import net.minecraft.nbt.NBTTagCompound;

public class SubTypeMapper<T> extends Mapper<T>
{
	private final SerialisableType type;
	private static final String OBJ_KEY = "obj";

	public SubTypeMapper(SerialisableType type)
	{
		this.type = type;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, Object o)
	{
		if(o == null)
			return;
		Class<?> clazz = o.getClass();
		String clazzName = o.getClass().getName();
		nbt.setString(NBTCLASSNAME, clazzName);
		Mapper<?> mapper = NBTHelper.getMapper(clazz, type);
		if(mapper == null)
			throw new RuntimeException("No mapper could be found for class " + clazzName);
		mapper.writeToNBT(nbt, OBJ_KEY, o);
	}

	@Override
	public T fillFromNBT(NBTTagCompound nbt, Object o)
	{
		if(o == null)
			return createFromNBT(nbt);
		Class<?> clazz = o.getClass();
		Mapper<?> mapper = NBTHelper.getMapper(clazz, type);
		if(mapper == null)
			throw new RuntimeException("No mapper could be found for class " + clazz.getName());
		mapper.fillFromNBT(nbt, OBJ_KEY, o);
		return (T) o;
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		if(!nbt.hasKey(NBTCLASSNAME))
			return null;
		String clazzName = nbt.getString(NBTCLASSNAME);
		try
		{
			Class<?> clazz = Class.forName(clazzName);
			Mapper<?> mapper = NBTHelper.getMapper(clazz, type);
			if(mapper == null)
				throw new RuntimeException("No mapper could be found fod class " + clazz.getName());
			return (T) mapper.readFromNBT(nbt, OBJ_KEY);
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

}
