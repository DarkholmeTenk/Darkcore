package io.darkcraft.darkcore.mod.nbt.impl.collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.impl.SubTypeMapper;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class MapMapper<T extends Map> extends Mapper<T>
{
	private static final String SIZE_KEY = "size";
	private final SubTypeMapper<Object> stm;

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
		t.clear();
		int size = nbt.getInteger(SIZE_KEY);
		for(int i = 0; i < size; i++)
		{
			Object k  = stm.createFromNBT(nbt.getCompoundTag("k"+i));
			Object v  = stm.createFromNBT(nbt.getCompoundTag("v"+i));
			t.put(k, v);
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
