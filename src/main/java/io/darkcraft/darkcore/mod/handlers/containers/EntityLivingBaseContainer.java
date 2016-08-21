package io.darkcraft.darkcore.mod.handlers.containers;

import java.lang.ref.WeakReference;

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

}
