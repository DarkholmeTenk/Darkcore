package io.darkcraft.darkcore.mod.datastore;

import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SimpleDoubleCoordStore
{
	public final int world;
	public final double x;
	public final double y;
	public final double z;
	
	public SimpleDoubleCoordStore(TileEntity te)
	{
		world = WorldHelper.getWorldID(te);
		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
	}
	
	public SimpleDoubleCoordStore(int win, double xin, double yin, double zin)
	{
		world = win;
		x = xin;
		y = yin;
		z = zin;
	}
	
	public SimpleDoubleCoordStore(World w, double xin, double yin, double zin)
	{
		world = WorldHelper.getWorldID(w);
		x = xin;
		y = yin;
		z = zin;
	}
	
	public SimpleDoubleCoordStore(int w, Entity ent)
	{
		world = w;
		x = ent.posX;
		y = ent.posY;
		z = ent.posZ;
	}
	
	public SimpleDoubleCoordStore(EntityLivingBase ent)
	{
		world = WorldHelper.getWorldID(ent);
		x = ent.posX;
		y = ent.posY;
		z = ent.posZ;
	}

	public World getWorldObj()
	{
		return WorldHelper.getWorld(world);
	}
	
	@Override
	public String toString()
	{
		return "World "+world + ":" + x+ ","+ y+ ","+ z;
	}
	
	public String toSimpleString()
	{
		return x + "," + y + "," + z;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + world;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleDoubleCoordStore))
			return false;
		SimpleDoubleCoordStore other = (SimpleDoubleCoordStore) obj;
		if (world != other.world)
			return false;
		if (x != other.x || y != other.y || z != other.z)
			return false;
		return true;
	}

	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("w", world);
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
	}
	
	public static SimpleDoubleCoordStore readFromNBT(NBTTagCompound nbt)
	{
		if(!(nbt.hasKey("w") && nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z")))
			return null;
		int w = nbt.getInteger("w");
		double x = nbt.getDouble("x");
		double y = nbt.getDouble("y");
		double z = nbt.getDouble("z");
		return new SimpleDoubleCoordStore(w,x,y,z);
	}
}
