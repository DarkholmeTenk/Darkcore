package io.darkcraft.darkcore.mod.handlers.containers;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.nbt.impl.BasicMappers;

public class EntityContainer<T extends Entity> implements IEntityContainer<T>
{
	private final int world;
	private final int id;
	private final WeakReference<T> ent;

	public EntityContainer(T e)
	{
		world = WorldHelper.getWorldID(e);
		id = e.getEntityId();
		ent = new WeakReference(e);
	}

	@Override
	public T getEntity()
	{
		return ent.get();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String tag)
	{
		BasicMappers.intMapper.writeToNBT(nbt, "world", world);
		BasicMappers.intMapper.writeToNBT(nbt, "id", id);
	}

	@Override
	public boolean equals(T o)
	{
		if(o == null) return false;
		return o.equals(getEntity());
	}

	public static IEntityContainer readFromNBT(NBTTagCompound nbt)
	{
		int world = BasicMappers.intMapper.createFromNBT(nbt, "world");
		int id = BasicMappers.intMapper.createFromNBT(nbt, "id");
		World w = WorldHelper.getWorld(world);
		if(w != null)
		{
			Entity ent = w.getEntityByID(id);
			if(ent != null)
				return EntityContainerHandler.getContainer(ent);
		}
		return null;
	}

	@Override
	public SimpleDoubleCoordStore getPosition()
	{
		T ent = getEntity();
		if(ent != null)
			return new SimpleDoubleCoordStore(ent);
		return null;
	}

}
