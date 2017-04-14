package io.darkcraft.darkcore.mod.nbt.impl.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.datastore.PropertyMap;
import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.impl.SubTypeMapper;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class MapMapper<T extends Map> extends Mapper<T>
{
	protected static final String SIZE_KEY = "size";
	protected final SubTypeMapper<Object> stm;

	public MapMapper(SerialisableType type)
	{
		stm = new SubTypeMapper<>(type);
	}

	public abstract T createNewMap();

	@Override
	public void writeToNBT(NBTTagCompound nbt, T t)
	{
		int size = t.size();
		nbt.setInteger(SIZE_KEY, size);
		int i = 0;
		for(Object objO : t.entrySet())
		{
			Entry obj = (Entry) objO;
			stm.writeToNBT(nbt, "k"+i, obj.getKey());
			stm.writeToNBT(nbt, "v"+i, obj.getValue());
			i++;
		}
	}

	@Override
	public T fillFromNBT(NBTTagCompound nbt, T t)
	{
		if(t == null)
			return createFromNBT(nbt);
		Map<Object,Object> map = t;
		Map<Object, Integer> keyMap = new HashMap<>();
		int size = nbt.getInteger(SIZE_KEY);
		for(int i = 0; i < size; i++)
		{
			Object k  = stm.createFromNBT(nbt.getCompoundTag("k"+i));
			keyMap.put(k, i);
		}

		Iterator<Entry<Object, Object>> iter = map.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry entry = iter.next();
			if(!keyMap.containsKey(entry.getKey()))
				iter.remove();
			else
			{
				int slot = keyMap.remove(entry.getKey());
				Object v = stm.fillFromNBT(nbt, "v"+slot, entry.getValue());
				entry.setValue(v);
			}
		}
		for(Entry<Object, Integer> remaining : keyMap.entrySet())
		{
			int slot = remaining.getValue();
			map.put(remaining.getKey(), stm.createFromNBT(nbt.getCompoundTag("v"+slot)));
		}
		return t;
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		T t = createNewMap();
		fillFromNBT(nbt, t);
		return t;
	}

	public static void register()
	{
		for(SerialisableType type : SerialisableType.values())
		{
			NBTHelper.register(Map.class, type, new SubTypeMapper<Map>(type));
			NBTHelper.register(HashMap.class, type, new HashMapMapper(type));
			NBTHelper.register(TreeMap.class, type, new TreeMapMapper(type));
			NBTHelper.register(LinkedHashMap.class, type, new LinkedHashMapMapper(type));
			NBTHelper.register(PropertyMap.class, new PropertyMapMapper(type));
		}
	}

	public static class LinkedHashMapMapper extends MapMapper<LinkedHashMap>
	{
		public LinkedHashMapMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public LinkedHashMap createNewMap()
		{
			return new LinkedHashMap();
		}
	}

	public static class TreeMapMapper extends MapMapper<TreeMap>
	{
		public TreeMapMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public TreeMap createNewMap()
		{
			return new TreeMap();
		}
	}

	public static class HashMapMapper extends MapMapper<HashMap>
	{
		public HashMapMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public HashMap createNewMap()
		{
			return new HashMap();
		}
	}
}
