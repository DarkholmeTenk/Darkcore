package io.darkcraft.darkcore.mod.nbt.impl.collections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.impl.SubTypeMapper;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SetMapper<T extends Set> extends Mapper<T>
{
	private static final String SIZE_KEY = "size";
	private final SubTypeMapper stm;

	public SetMapper(SerialisableType type)
	{
		stm = new SubTypeMapper(type);
	}

	public abstract T createNewSet();

	@Override
	public void writeToNBT(NBTTagCompound nbt, Object o)
	{
		T t = (T) o;
		int size = t.size();
		nbt.setInteger(SIZE_KEY, size);
		int i = 0;
		for(Object obj : t)
			stm.writeToNBT(nbt, "i"+(i++), obj);
	}

	@Override
	public T fillFromNBT(NBTTagCompound nbt, Object o)
	{
		if(o == null)
			return createFromNBT(nbt);
		Set<Object> t = (Set<Object>) o;
		t.clear();
		int size = nbt.getInteger(SIZE_KEY);
		for(int i = 0; i < size; i++)
			t.add(stm.createFromNBT(nbt.getCompoundTag("i"+i)));
		return (T) t;
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		T t = createNewSet();
		fillFromNBT(nbt, t);
		return t;
	}

	public static void register()
	{
		for(SerialisableType type : SerialisableType.values())
		{
			NBTHelper.register(Set.class, type, new SubTypeMapper<Set>(type));
			NBTHelper.register(LinkedHashSet.class, type, new LinkedHashSetMapper(type));
			NBTHelper.register(HashSet.class, type, new HashSetMapper(type));
			NBTHelper.register(TreeSet.class, type, new TreeSetMapper(type));
		}
	}

	public static class LinkedHashSetMapper extends SetMapper<LinkedHashSet>
	{
		public LinkedHashSetMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public LinkedHashSet createNewSet()
		{
			return new LinkedHashSet();
		}
	}

	public static class HashSetMapper extends SetMapper<HashSet>
	{
		public HashSetMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public HashSet createNewSet()
		{
			return new HashSet();
		}
	}

	public static class TreeSetMapper extends SetMapper<TreeSet>
	{
		public TreeSetMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public TreeSet createNewSet()
		{
			return new TreeSet();
		}
	}
}
