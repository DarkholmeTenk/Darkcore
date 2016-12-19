package io.darkcraft.darkcore.mod.nbt.impl;

import java.lang.reflect.Array;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import net.minecraft.nbt.NBTTagCompound;

public class ArrayMapper<T> extends Mapper<T[]>
{
	private final Class<T> clazz;
	private final Mapper<T> mapper;

	public ArrayMapper(Class<T> clazz, Mapper<T> mapper)
	{
		this.clazz = clazz;
		this.mapper = mapper;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, T[] t)
	{
		if(t == null)
			return;
		nbt.setInteger("s", t.length);
		for(int i = 0; i < t.length; i++)
		{
			if(t[i] != null)
				mapper.writeToNBT(nbt, "i" + i, t[i]);
		}
	}

	@Override
	public T[] fillFromNBT(NBTTagCompound nbt, T[] t)
	{
		if(t == null)
			return createFromNBT(nbt);
		if(t.length != nbt.getInteger("s"))
			return createFromNBT(nbt);
		for(int i = 0; i < t.length; i++)
		{
			if(nbt.hasKey("i" + i))
			{
				if((t[i] == null) || mapper.shouldCreateNew())
					t[i] = mapper.readFromNBT(nbt, "i"+i);
				else
					t[i] = mapper.fillFromNBT(nbt, "i"+i, t[i]);
			}
			else
				t[i] = null;
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		T[] tArr = (T[]) Array.newInstance(clazz, nbt.getInteger("s"));
		fillFromNBT(nbt, tArr);
		return tArr;
	}
}
