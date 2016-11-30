package io.darkcraft.darkcore.mod.handlers.containers;

import java.lang.ref.WeakReference;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class EntityLivingBaseContainer implements IEntityContainer<EntityLivingBase>
{
	private final WeakReference<EntityLivingBase> ent;

	public EntityLivingBaseContainer(EntityLivingBase e)
	{
		ent = new WeakReference(e);
	}

	@Override
	public EntityLivingBase getEntity()
	{
		return ent.get();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String tag)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean equals(EntityLivingBase o)
	{
		if(o == null) return false;
		return o.equals(getEntity());
	}

	public static EntityLivingBaseContainer readFromNBT(NBTTagCompound snbt)
	{
		return null;
	}

	private int lastAge = -1;
	private SimpleDoubleCoordStore lastPosition = null;
	@Override
	public SimpleDoubleCoordStore getPosition()
	{
		EntityLivingBase ent = getEntity();
		if(ent != null)
		{
			if(ent.getAge() != lastAge)
			{
				lastAge = ent.getAge();
				return lastPosition = new SimpleDoubleCoordStore(ent);
			}
			return lastPosition;
		}
		return null;
	}

}
