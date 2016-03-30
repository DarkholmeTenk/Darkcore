package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.handlers.packets.EntityDataStorePacketHandler;

import java.lang.ref.WeakReference;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;

public abstract class AbstractEntityDataStore implements IExtendedEntityProperties
{
	public final String id;
	private final WeakReference<EntityLivingBase> entity;

	public AbstractEntityDataStore(EntityLivingBase ent, String _id)
	{
		id = _id;
		entity = new WeakReference(ent);
	}

	public EntityLivingBase getEntity()
	{
		if(entity == null) return null;
		return entity.get();
	}

	public void queueUpdate()
	{
		EntityDataStorePacketHandler.queueUpdate(this);
	}

	public void sendUpdate()
	{
		EntityDataStorePacketHandler.sendUpdate(this);
	}

	@Override
	public final void saveNBTData(NBTTagCompound nbt)
	{
		writeToNBT(nbt);
		writeTransmittable(nbt);
	}

	@Override
	public final void loadNBTData(NBTTagCompound nbt)
	{
		readFromNBT(nbt);
		readTransmittable(nbt);
	}

	public abstract void writeToNBT(NBTTagCompound nbt);
	public abstract void readFromNBT(NBTTagCompound nbt);
	public abstract void writeTransmittable(NBTTagCompound nbt);
	public abstract void readTransmittable(NBTTagCompound nbt);

	public abstract boolean notifyArea();

}
