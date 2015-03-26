package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;

public abstract class AbstractWorldDataStore extends WorldSavedData
{
	private final String name;

	public AbstractWorldDataStore(String _name)
	{
		super(_name);
		name = _name;
	}
	
	public void load()
	{
		try
		{
			MapStorage data = getData();
			WorldSavedData wsd = data.loadData(getClass(), getName());
			NBTTagCompound nbt = new NBTTagCompound();
			wsd.writeToNBT(nbt);
			readFromNBT(nbt);
		}
		catch(NullPointerException e){}
	}
	
	public void save()
	{
		try
		{
			MapStorage data = getData();
			data.setData(getName(), this);
		}
		catch(NullPointerException e){}
	}
	
	private MapStorage getData()
	{
		WorldServer world = WorldHelper.getWorldServer(getDimension());
		if(world != null)
			return world.perWorldStorage;
		return null;
	}
	
	public String getName()
	{
		return name;
	}
	
	public abstract int getDimension();

	@Override
	public abstract void readFromNBT(NBTTagCompound nbt);

	@Override
	public abstract void writeToNBT(NBTTagCompound nbt);

}
