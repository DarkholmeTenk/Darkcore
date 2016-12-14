package io.darkcraft.darkcore.mod.nbt.impl;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;

public class BasicMappers
{

	public static void register()
	{
		NBTHelper.register(String.class, stringMapper);
		NBTHelper.register(Long.class, longMapper);
		NBTHelper.register(Double.class, doubleMapper);
		NBTHelper.register(Byte.class, byteMapper);
		NBTHelper.register(byte[].class, byteArrMapper);
		NBTHelper.register(Integer.class, intMapper);
		NBTHelper.register(int[].class, intArrMapper);
		NBTHelper.register(Boolean.class, boolMapper);
		NBTHelper.register(ItemStack.class, isMapper);
		NBTHelper.register(UUID.class, uuidMapper);
	}

	public static Mapper<String> stringMapper = new PrimMapper<String>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setString(id, (String) t); }

		@Override
		public String readFromNBT(NBTTagCompound nbt, String id) { return nbt.getString(id); }
	};

	public static Mapper<Byte> byteMapper = new PrimMapper<Byte>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setByte(id, (byte) t); }

		@Override
		public Byte readFromNBT(NBTTagCompound nbt, String id) { return nbt.getByte(id); }
	};

	public static Mapper<byte[]> byteArrMapper = new PrimMapper<byte[]>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setByteArray(id, (byte[])t); }

		@Override
		public byte[] readFromNBT(NBTTagCompound nbt, String id) { return nbt.getByteArray(id); }
	};

	public static Mapper<Integer> intMapper = new PrimMapper<Integer>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setInteger(id, (int) t); }

		@Override
		public Integer readFromNBT(NBTTagCompound nbt, String id) { return nbt.getInteger(id); }
	};

	public static Mapper<int[]> intArrMapper = new PrimMapper<int[]>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setIntArray(id, (int[])t); }

		@Override
		public int[] readFromNBT(NBTTagCompound nbt, String id) { return nbt.getIntArray(id); }
	};

	public static Mapper<Long> longMapper = new PrimMapper<Long>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setLong(id, (long) t); }

		@Override
		public Long readFromNBT(NBTTagCompound nbt, String id) { return nbt.getLong(id); }
	};

	public static Mapper<Double> doubleMapper = new PrimMapper<Double>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setDouble(id, (double) t); }

		@Override
		public Double readFromNBT(NBTTagCompound nbt, String id) { return nbt.getDouble(id); }
	};

	public static Mapper<Boolean> boolMapper = new PrimMapper<Boolean>()
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t) { nbt.setBoolean(id, (boolean) t); }

		@Override
		public Boolean readFromNBT(NBTTagCompound nbt, String id) { return nbt.getBoolean(id); }
	};

	public static Mapper<ItemStack> isMapper = new Mapper<ItemStack>()
	{

		@Override
		public void writeToNBT(NBTTagCompound nbt, Object o)
		{
			if(o == null)
				return;
			((ItemStack)o).writeToNBT(nbt);
		}

		@Override
		public ItemStack fillFromNBT(NBTTagCompound nbt, Object o)
		{
			if(o == null)
				return createFromNBT(nbt);
			ItemStack t = (ItemStack) o;
			t.readFromNBT(nbt);
			return t;
		}

		@Override
		public ItemStack createFromNBT(NBTTagCompound nbt, Object... arguments)
		{
			return ItemStack.loadItemStackFromNBT(nbt);
		}
	};

	public static Mapper<FluidStack> fsMapper = new Mapper<FluidStack>()
	{

		@Override
		public void writeToNBT(NBTTagCompound nbt, Object o)
		{
			if(o == null)
				return;
			((FluidStack)o).writeToNBT(nbt);
		}

		@Override
		public FluidStack fillFromNBT(NBTTagCompound nbt, Object t)
		{
			return FluidStack.loadFluidStackFromNBT(nbt);
		}

		@Override
		public FluidStack createFromNBT(NBTTagCompound nbt, Object... arguments)
		{
			return FluidStack.loadFluidStackFromNBT(nbt);
		}

		@Override
		public boolean shouldCreateNew() { return true; }
	};

	public static Mapper<UUID> uuidMapper = new PrimMapper<UUID>()
	{

		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t)
		{
			nbt.setLong(id+".l", ((UUID)t).getLeastSignificantBits());
			nbt.setLong(id+".m", ((UUID)t).getMostSignificantBits());
		}

		@Override
		public UUID readFromNBT(NBTTagCompound nbt, String id)
		{
			long l = nbt.getLong(id  +".l");
			long m = nbt.getLong(id  +".m");
			return new UUID(m, l);
		}

	};
}
