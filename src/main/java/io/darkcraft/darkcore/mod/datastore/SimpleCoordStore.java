package io.darkcraft.darkcore.mod.datastore;

import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class SimpleCoordStore
{
	public final int world;
	public final int x;
	public final int y;
	public final int z;
	
	public SimpleCoordStore(TileEntity te)
	{
		world = WorldHelper.getWorldID(te);
		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
	}
	
	public SimpleCoordStore(int win, int xin, int yin, int zin)
	{
		world = win;
		x = xin;
		y = yin;
		z = zin;
	}
	
	public SimpleCoordStore(World w, int xin, int yin, int zin)
	{
		world = WorldHelper.getWorldID(w);
		x = xin;
		y = yin;
		z = zin;
	}
	
	public SimpleCoordStore(EntityPlayer player)
	{
		world = WorldHelper.getWorldID(player);
		x = (int) Math.floor(player.posX);
		y = (int) Math.floor(player.posY);
		z = (int) Math.floor(player.posZ);
	}
	
	public SimpleCoordStore(SimpleDoubleCoordStore pos)
	{
		this(pos.world,(int)Math.floor(pos.x),(int)Math.floor(pos.y),(int)Math.floor(pos.z));
	}
	
	public SimpleDoubleCoordStore getCenter()
	{
		return new SimpleDoubleCoordStore(world,x+0.5,y+0.5,z+0.5);
	}
	
	public SimpleCoordStore getNearby(ForgeDirection dir)
	{
		int oX = dir.offsetX;
		int oY = dir.offsetY;
		int oZ = dir.offsetZ;
		return new SimpleCoordStore(world,x+oX,y+oY,z+oZ);
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
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof SimpleCoordStore))
			return false;
		SimpleCoordStore other = (SimpleCoordStore) obj;
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
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
	}
	
	public static SimpleCoordStore readFromNBT(NBTTagCompound nbt)
	{
		if(!(nbt.hasKey("w") && nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z")))
			return null;
		int w = nbt.getInteger("w");
		int x = nbt.getInteger("x");
		int y = nbt.getInteger("y");
		int z = nbt.getInteger("z");
		return new SimpleCoordStore(w,x,y,z);
	}

	public ChunkCoordIntPair toChunkCoords()
	{
		return new ChunkCoordIntPair(x>>4,z>>4);
	}
}
