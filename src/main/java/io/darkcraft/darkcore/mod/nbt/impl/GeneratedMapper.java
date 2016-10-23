package io.darkcraft.darkcore.mod.nbt.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import net.minecraft.nbt.NBTTagCompound;

public class GeneratedMapper<T> extends Mapper<T>
{

	public static <T> Mapper<T> getMapper(Class<T> clazz, SerialisableType t)
	{
		if(NBTHelper.hasMapper(clazz, t))
			return NBTHelper.getMapper(clazz, t);
		NBTSerialisable serProp = clazz.getAnnotation(NBTSerialisable.class);
		if(serProp == null)
			return null;
		Mapper<? super T> parent = NBTHelper.getMapper(clazz.getSuperclass(), t);

		Map<Field, Data> fields;
		if(parent instanceof GeneratedMapper)
			fields = Maps.newHashMap(((GeneratedMapper)parent).fields);
		else
			fields = Maps.newHashMap();

		for(Field f : clazz.getDeclaredFields())
		{
			NBTProperty property = f.getAnnotation(NBTProperty.class);
			if((property == null) || !t.valid(property))
				continue;
			Mapper<?> mapper = NBTHelper.getMapper(f.getType(), t);
			if(mapper == null)
				throw new RuntimeException("Field " + f.getName() + " in " + clazz.getSimpleName()
					+ " is declared as NBTProperty but no mapper can be found");
			fields.put(f, new Data(f, property, mapper));
		}
		Mapper<T> m = new GeneratedMapper(clazz, fields);
		if(serProp.includeType())
			m = new TypeWrapperMapper(clazz, m);
		return m;
	}

	private final Class<T> clazz;
	private final Map<Field, Data> fields;

	private GeneratedMapper(Class c, Map<Field, Data> fieldMap)
	{
		clazz = c;
		fields = fieldMap;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, Object t)
	{
		for(Entry<Field, Data> entry : fields.entrySet())
		{
			Field f = entry.getKey();
			Mapper<?> m = entry.getValue().m;
			String n = entry.getValue().n;
			try
			{
				f.setAccessible(true);
				Object o = f.get(t);
				if(o != null)
					m.writeToNBT(nbt, n, o);
			}
			catch(SecurityException | IllegalArgumentException | IllegalAccessException e){}
		}
	}

	@Override
	public T fillFromNBT(NBTTagCompound nbt, Object t)
	{
		for(Entry<Field, Data> entry : fields.entrySet())
		{
			Field f = entry.getKey();
			Mapper<?> m = entry.getValue().m;
			String n = entry.getValue().n;
			try
			{
				f.setAccessible(true);
				if(nbt.hasKey(n))
				{
					Object o = f.get(t);
					if(m.shouldCreateNew() || (o == null))
						f.set(t, m.readFromNBT(nbt, n));
					else
						f.set(t, m.fillFromNBT(nbt, n, o));
				}
				else
					f.set(t, null);
			}
			catch(SecurityException | IllegalArgumentException | IllegalAccessException e){}
		}
		return (T) t;
	}

	private T getNewT(Object... args)
	{
		try
		{
			if(args.length == 0)
				return clazz.newInstance();
			else
			{
				cLoop:
				for(Constructor<?> constr : clazz.getConstructors())
				{
					if(constr.getParameterCount() != args.length)
						continue;
					Class[] classes = constr.getParameterTypes();
					for(int i = 0; i < classes.length; i++)
					{
						if(!classes[i].isInstance(args[i]))
							continue cLoop;
					}
					return (T) constr.newInstance(args);
				}
			}
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... args)
	{
		T t = getNewT(args);
		if(t != null)
			fillFromNBT(nbt, t);
		return t;
	}

	private static class Data
	{
		private final Mapper<?> m;
		private final String n;

		public Data(Field f, NBTProperty p, Mapper<?> m)
		{
			this.m = m;
			if(p.name().equals(""))
				n = f.getName();
			else
				n = p.name();
		}
	}
}
