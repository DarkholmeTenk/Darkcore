package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.handlers.packets.WorldDataStoreHandler;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public abstract class AbstractWorldDataStore extends WorldSavedData
{
	private final String	preDimName;
	private final String	name;
	protected final int		dimID;

	public static int getDimID(String s)
	{
		if(s.contains("_"))
		{
			String[] data = s.split("_");
			return MathHelper.toInt(data[data.length-1],0);
		}
		return 0;
	}

	public AbstractWorldDataStore(String _name)
	{
		this(_name, getDimID(_name));
	}

	public AbstractWorldDataStore(String _name, int dim)
	{
		super(_name+"_"+dim);
		name = _name+"_"+dim;
		preDimName = _name;
		dimID = dim;
		WorldDataStoreHandler.register(this);
	}

	public void load()
	{
		synchronized (this)
		{
			try
			{
				MapStorage data = getData();
				WorldSavedData wsd = data.loadData(getClass(), getName());
				if(wsd == null)
					wsd = data.loadData(getClass(), preDimName);
				NBTTagCompound nbt = new NBTTagCompound();
				wsd.writeToNBT(nbt);
				readFromNBT(nbt);
			}
			catch (Exception e){}
		}
	}

	public void save()
	{
		synchronized (this)
		{
			try
			{
				MapStorage data = getData();
				data.setData(getName(), this);
			}
			catch (NullPointerException e)
			{
			}
		}
	}

	public void sendUpdate()
	{
		if(ServerHelper.isClient()) return;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("AWDSdim", getDimension());
		nbt.setString("AWDSname", getName());
		writeToNBT(nbt);
		DataPacket dp = new DataPacket(nbt, WorldDataStoreHandler.disc);
		DarkcoreMod.networkChannel.sendToDimension(dp, getDimension());
	}

	private MapStorage getData()
	{
		World world = WorldHelper.getWorld(getDimension());
		if (world != null) return world.perWorldStorage;
		return null;
	}

	public String getName()
	{
		return name;
	}

	public int getDimension()
	{
		return dimID;
	}

	@Override
	public abstract void readFromNBT(NBTTagCompound nbt);

	@Override
	public abstract void writeToNBT(NBTTagCompound nbt);

}
