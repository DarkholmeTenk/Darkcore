package io.darkcraft.darkcore.mod.nbt.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTConstructor;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTMethod;
import io.darkcraft.darkcore.mod.nbt.NBTMethod.Type;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;

public class GeneratedMapper<T> extends Mapper<T>
{

	private static <T> NBTConstructorData getConstructor(Class<T> clazz, Map<Field, Data> fields)
	{
		try
		{
			for(Constructor<?> constrObj : clazz.getConstructors())
			{
				Constructor<T> constr = (Constructor<T>) constrObj;
				NBTConstructor annotation = constr.getAnnotation(NBTConstructor.class);
				if(annotation != null)
				{
					return new NBTConstructorData(annotation, constr, fields);
				}
			}
		}
		catch(SecurityException e){
			e.printStackTrace();
		}
		return null;
	}

	private static <T> Multimap<Type, Method> getMethods(Class<T> clazz)
	{
		Multimap<Type, Method> methods = Multimaps.newSetMultimap(new EnumMap<Type, Collection<Method>>(Type.class),
				new Supplier<HashSet<Method>>()
				{
					@Override
					public HashSet<Method> get()
					{
						return new HashSet<>();
					}
				});
		for(Method m : clazz.getMethods())
		{
			NBTMethod annotation = m.getAnnotation(NBTMethod.class);
			if(annotation == null)
				continue;
			if(!Arrays.equals(m.getParameterTypes(), new Class<?>[]{NBTTagCompound.class}))
				throw new RuntimeException("Exception constructing mapper. " + m.getName() + " does not take a single NBTTagCompound");
			methods.put(annotation.value(), m);
		}
		return methods;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Mapper<T> getMapper(Class<T> clazz, SerialisableType t)
	{
		if(NBTHelper.hasMapper(clazz, t))
			return NBTHelper.getMapper(clazz, t);
		if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
			return new SubTypeMapper(t);
		NBTSerialisable serProp = clazz.getAnnotation(NBTSerialisable.class);
		if(serProp == null)
			return null;
		Mapper<? super T> parent = NBTHelper.getMapper(clazz.getSuperclass(), t);

		Map<Field, Data> fields;
		if(parent instanceof GeneratedMapper)
			fields = Maps.newHashMap(((GeneratedMapper)parent).fields);
		else
			fields = Maps.newHashMap();
		boolean recurse = fields.isEmpty();
		Class<?> c = clazz;
		while(c != null)
		{
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
	//			if(!serProp.createNew() && Modifier.isFinal(f.getModifiers()))
	//				throw new RuntimeException("Field " + f.getName() + " is final, but class is not set to create new instances on fill");
			}
			c = recurse ? c.getSuperclass() : null;
		}
		NBTConstructorData<T> constructor = getConstructor(clazz, fields);
		Multimap<Type, Method> methods = getMethods(clazz);
		Mapper<T> m = new GeneratedMapper(clazz, fields, methods, constructor, serProp.createNew());
		if(serProp.includeType())
			m = new TypeWrapperMapper(clazz, m);
		return m;
	}

	private final Class<T> clazz;
	private final Map<Field, Data> fields;
	private final Map<String, Data> fieldNames;
	private final Multimap<Type, Method> methods;
	private final NBTConstructorData<T> constructor;
	private final boolean shouldCreateNew;

	private GeneratedMapper(Class c, Map<Field, Data> fieldMap, Multimap<Type, Method> methods, NBTConstructorData<T> constructor, boolean shouldCreateNew)
	{
		clazz = c;
		fields = fieldMap;
		fieldNames = new HashMap<>();
		for(Entry<Field, Data> entry : fields.entrySet())
			fieldNames.put(entry.getKey().getName(), entry.getValue());
		this.methods = methods;
		this.constructor = constructor;
		this.shouldCreateNew = shouldCreateNew;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, T t)
	{
		for(Entry<Field, Data> entry : fields.entrySet())
		{
			Field f = entry.getKey();
			Mapper<Object> m = (Mapper<Object>) entry.getValue().m;
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
		for(Method m : methods.get(Type.WRITE))
		{
			try
			{
				m.setAccessible(true);
				m.invoke(t, nbt);
			}
			catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e){}
		}
	}

	@Override
	public T fillFromNBT(NBTTagCompound nbt, T t)
	{
		if(shouldCreateNew)
		{
			t = createFromNBT(nbt);
			if(constructor != null)
				return t;
		}
		for(Entry<Field, Data> entry : fields.entrySet())
		{
			Field f = entry.getKey();
			if(Modifier.isFinal(f.getModifiers()))
				continue;
			Mapper<Object> m = (Mapper<Object>) entry.getValue().m;
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
		for(Method m : methods.get(Type.READ))
		{
			try
			{
				m.setAccessible(true);
				m.invoke(t, nbt);
			}
			catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e){}
		}
		return t;
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
		if((constructor != null) && (args.length == 0))
			return constructor.construct(nbt);
		T t = getNewT(args);
		if(t != null)
			fillFromNBT(nbt, t);
		return t;
	}

	@Override
	public boolean shouldCreateNew()
	{
		return shouldCreateNew;
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

	private static class NBTConstructorData<T>
	{
		private Constructor<T> constructor;
		private final Data[] data;

		public NBTConstructorData(NBTConstructor annotation, Constructor<T> constructor, Map<Field, Data> dataMap)
		{
			this.constructor = constructor;
			Map<String, Data> dm = new HashMap<>();
			for(Entry<Field, Data> entry : dataMap.entrySet())
				dm.put(entry.getKey().getName(), entry.getValue());

			String[] names = annotation.value();
			data = new Data[names.length];
			int i = 0;
			for(String name : names)
				if((data[i++] = dm.get(name)) == null)
					throw new RuntimeException("Unable to create constructor: " + name + " not found");
		}

		public T construct(NBTTagCompound nbt)
		{
			try
			{
				Object[] objs = new Object[data.length];
				for(int i = 0; i < objs.length; i++)
				{
					Data data = this.data[i];
					objs[i] = data.m.readFromNBT(nbt, data.n);
				}
				return constructor.newInstance(objs);
			}
			catch(SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
