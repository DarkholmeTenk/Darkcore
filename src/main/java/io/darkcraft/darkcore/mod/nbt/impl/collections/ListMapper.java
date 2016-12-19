package io.darkcraft.darkcore.mod.nbt.impl.collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.impl.SubTypeMapper;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("rawtypes")
public abstract class ListMapper<T extends List> extends Mapper<T>
{
	private static final String SIZE_KEY = "size";
	private final SubTypeMapper<Object> stm;

	public ListMapper(SerialisableType type)
	{
		stm = new SubTypeMapper<Object>(type);
	}

	public abstract T createNewList();

	@Override
	public void writeToNBT(NBTTagCompound nbt, T t)
	{
		int size = t.size();
		nbt.setInteger(SIZE_KEY, size);
		for(int i = 0; i < size; i++)
			stm.writeToNBT(nbt, "i"+i, t.get(i));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T fillFromNBT(NBTTagCompound nbt, T t)
	{
		if(t == null)
			return createFromNBT(nbt);
		t.clear();
		int size = nbt.getInteger(SIZE_KEY);
		for(int i = 0; i < size; i++)
			t.add(stm.readFromNBT(nbt, "i"+i));
		return t;
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		T t = createNewList();
		fillFromNBT(nbt, t);
		return t;
	}

	public static void register()
	{
		for(SerialisableType t : SerialisableType.values())
		{
			NBTHelper.register(List.class, new SubTypeMapper<List>(t));
			NBTHelper.register(ArrayList.class, t, new ArrayListMapper(t));
			NBTHelper.register(LinkedList.class, t, new LinkedListMapper(t));
		}
	}

	public static class ArrayListMapper extends ListMapper<ArrayList>
	{
		public ArrayListMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public ArrayList<?> createNewList()
		{
			return new ArrayList<>();
		}
	}

	public static class LinkedListMapper extends ListMapper<LinkedList>
	{
		public LinkedListMapper(SerialisableType type)
		{
			super(type);
		}

		@Override
		public LinkedList<?> createNewList()
		{
			return new LinkedList<>();
		}
	}
}
