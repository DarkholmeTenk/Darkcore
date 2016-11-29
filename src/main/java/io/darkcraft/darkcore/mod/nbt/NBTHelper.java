package io.darkcraft.darkcore.mod.nbt;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Supplier;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.primitives.Primitives;

import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.impl.ArrayMapper;
import io.darkcraft.darkcore.mod.nbt.impl.BasicMappers;
import io.darkcraft.darkcore.mod.nbt.impl.GeneratedMapper;
import io.darkcraft.darkcore.mod.nbt.impl.collections.CollectionMappers;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper
{
	private static Table<Class<?>, SerialisableType, Mapper<?>> mapperTable;

	static
	{
		mapperTable = Tables.newCustomTable(new HashMap<Class<?>, Map<SerialisableType, Mapper<?>>>(),
			new Supplier<Map<SerialisableType, Mapper<?>>>()
			{
					@Override
					public Map<SerialisableType, Mapper<?>> get()
					{
						return new EnumMap<>(SerialisableType.class);
					}
			});
		BasicMappers.register();
		CollectionMappers.register();
	}

	public static <T> void register(Class<T> c, Mapper<T> mapper)
	{
		for(SerialisableType type : SerialisableType.values())
			register(c, type, mapper);
	}

	public static <T> void register(Class<T> c, SerialisableType type, Mapper<T> mapper)
	{
		mapperTable.put(c, type, mapper);
	}

	public static boolean hasMapper(Class<?> c, SerialisableType type)
	{
		return mapperTable.contains(c, type);
	}

	public static <T> Mapper<T> getMapper(Class<T> c, SerialisableType type)
	{
		if(c.isPrimitive())
			c = Primitives.wrap(c);
		Mapper<T> mapper = (Mapper<T>) mapperTable.get(c, type);
		if(mapper == null)
		{
			if(c.isArray())
			{
				Class<?> arr = c.getComponentType();
				Mapper<?> map = getMapper(arr, type);
				if(map != null)
					mapper = new ArrayMapper(arr, map);
			}
			else
			{
				mapper = GeneratedMapper.getMapper(c, type);
				if(mapper == null)
				{
					Class p = c.getSuperclass();
					while(p != null)
					{
						Mapper m = getMapper(p, type);
						if((m != null) && m.handleSubclasses())
						{
							mapper = m;
							break;
						}
						p = p.getSuperclass();
					}
				}
			}
			if(mapper != null)
				register(c, type, mapper);
		}
		return mapper;
	}

	public static Object loadFromAnonymousNBT(NBTTagCompound nbt, SerialisableType type, Object... args)
	{
		String name = nbt.getString(Mapper.NBTCLASSNAME);
		try
		{
			Class<?> c = Class.forName(name);
			Mapper<?> m = getMapper(c, type);
			if(m == null)
				throw new RuntimeException("Could not create mapper for " + c.getSimpleName());
			return m.createFromNBT(nbt, args);
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
