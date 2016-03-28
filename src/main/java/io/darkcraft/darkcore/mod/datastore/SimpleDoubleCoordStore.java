package io.darkcraft.darkcore.mod.datastore;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class SimpleDoubleCoordStore
{
	public final int	world;
	public final double	x;
	public final double	y;
	public final double	z;
	public final int	iX;
	public final int	iY;
	public final int	iZ;

	public SimpleDoubleCoordStore(TileEntity te)
	{
		this(WorldHelper.getWorldID(te), te.xCoord, te.yCoord, te.zCoord);
	}

	public SimpleDoubleCoordStore(int win, double xin, double yin, double zin)
	{
		world = win;
		x = xin;
		y = yin;
		z = zin;

		iX = (int) Math.floor(xin);
		iY = (int) Math.floor(yin);
		iZ = (int) Math.floor(zin);
	}

	public SimpleDoubleCoordStore(World w, double xin, double yin, double zin)
	{
		this(WorldHelper.getWorldID(w), xin, yin, zin);
	}

	public SimpleDoubleCoordStore(int w, Entity ent)
	{
		this(w, ent.posX, ent.posY, ent.posZ);
	}

	public SimpleDoubleCoordStore(Entity ent)
	{
		this(WorldHelper.getWorldID(ent), ent);
	}

	public SimpleDoubleCoordStore(EntityLivingBase ent)
	{
		this(WorldHelper.getWorldID(ent), ent.posX, ent.posY, ent.posZ);
	}

	public double diagonalParadoxDistance(double sx, double sy, double sz)
	{
		return Math.abs(x-sx) + Math.abs(y-sy) + Math.abs(z-sz);
	}

	public double diagonalParadoxDistance(SimpleDoubleCoordStore scds)
	{
		return diagonalParadoxDistance(scds.x, scds.y, scds.z);
	}

	/**
	 * Returns the distance between this and other simple double coord store
	 *
	 * @param other
	 * @return -1 if not in same world or abs(distance between the two)
	 */
	public double distance(SimpleDoubleCoordStore other)
	{
		if ((other == null) || (world != other.world)) return Double.POSITIVE_INFINITY;
		double total = 0;
		double temp = (other.x - x);
		total += (temp * temp);
		temp = (other.y - y);
		total += (temp * temp);
		temp = (other.z - z);
		total += (temp * temp);
		return Math.sqrt(total);
	}

	public double distance(SimpleCoordStore other)
	{
		if ((other == null) || (world != other.world)) return -1;
		double total = 0;
		double temp = ((other.x + 0.5) - x);
		total += (temp * temp);
		temp = ((other.y + 0.5) - y);
		total += (temp * temp);
		temp = ((other.z + 0.5) - z);
		total += (temp * temp);
		return Math.sqrt(total);
	}

	public double distance(EntityLivingBase e)
	{
		return distance(new SimpleDoubleCoordStore(e));
	}

	public World getWorldObj()
	{
		return WorldHelper.getWorld(world);
	}

	public SimpleCoordStore floor()
	{
		int _x = MathHelper.floor(x);
		int _y = MathHelper.floor(y);
		int _z = MathHelper.floor(z);
		return new SimpleCoordStore(world, _x, _y, _z);
	}

	public SimpleCoordStore round()
	{
		int _x = MathHelper.round(x);
		int _y = MathHelper.round(y);
		int _z = MathHelper.round(z);
		return new SimpleCoordStore(world, _x, _y, _z);
	}

	public SimpleCoordStore ceil()
	{
		int _x = MathHelper.ceil(x);
		int _y = MathHelper.ceil(y);
		int _z = MathHelper.ceil(z);
		return new SimpleCoordStore(world, _x, _y, _z);
	}

	/**
	 * returns an AABB centred around this coord store with lengths 2r in each direction.
	 *
	 * @param r
	 *            the 'radius' of the AABB
	 * @return
	 */
	public AxisAlignedBB getAABB(double r)
	{
		return AxisAlignedBB.getBoundingBox(x - r, y - r, z - r, x + r, y + r, z + r);
	}

	@Override
	public String toString()
	{
		return "World " + world + ":" + x + "," + y + "," + z;
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
		result = (prime * result) + world;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public int hashCodeTolerance(double tolerance)
	{
		int mag = MathHelper.round(1 / tolerance);
		final int prime = 31;
		int result = 1;
		result = (prime * result) + world;
		long temp;
		temp = MathHelper.floor(x * mag);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = MathHelper.floor(y * mag);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = MathHelper.floor(z * mag);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SimpleDoubleCoordStore)) return false;
		SimpleDoubleCoordStore other = (SimpleDoubleCoordStore) obj;
		if (world != other.world) return false;
		if ((x != other.x) || (y != other.y) || (z != other.z)) return false;
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
		if (!(nbt.hasKey("w") && nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z"))) return null;
		int w = nbt.getInteger("w");
		double x = nbt.getDouble("x");
		double y = nbt.getDouble("y");
		double z = nbt.getDouble("z");
		return new SimpleDoubleCoordStore(w, x, y, z);
	}
}
