package io.darkcraft.darkcore.mod.datastore;

import net.minecraft.nbt.NBTTagCompound;

public class UVStore
{
	public static final UVStore defaultUV = new UVStore(0,1,0,1);

	public final double u;
	public final double U;
	public final double v;
	public final double V;

	public UVStore(double u, double U, double v, double V)
	{
		this.u = u;
		this.U = U;
		this.v = v;
		this.V = V;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("u", u);
		nbt.setDouble("U", U);
		nbt.setDouble("v", v);
		nbt.setDouble("V", V);
	}

	public void writeToNBT(NBTTagCompound nbt, String name)
	{
		NBTTagCompound newNBT = new NBTTagCompound();
		writeToNBT(newNBT);
		nbt.setTag(name, newNBT);
	}

	public static UVStore readFromNBT(NBTTagCompound nbt)
	{
		double u = nbt.hasKey("u") ? nbt.getDouble("u") : 0;
		double U = nbt.hasKey("U") ? nbt.getDouble("U") : 1;
		double v = nbt.hasKey("v") ? nbt.getDouble("v") : 0;
		double V = nbt.hasKey("V") ? nbt.getDouble("V") : 1;
		if((u == 0) && (v == 0) && (U == 1) && (V == 1))
			return defaultUV;
		return new UVStore(u,U,v,V);
	}

	public static UVStore readFromNBT(NBTTagCompound nbt, String name)
	{
		if(nbt.hasKey(name))
			return readFromNBT(nbt.getCompoundTag(name));
		return defaultUV;
	}

	public UVStore div(double i)
	{
		return new UVStore(u/i,U/i,v/i,V/i);
	}
}
