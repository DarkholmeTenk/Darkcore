package io.darkcraft.darkcore.mod.abstracts.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;

import java.lang.ref.WeakReference;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractEffect
{
	public final String id;
	public final WeakReference<EntityLivingBase> entity;
	public final boolean visible;
	public final boolean doesTick;
	public final int tickFreq;
	public final int duration;
	private int tt = -1;

	public AbstractEffect(String _id, EntityLivingBase ent, int duration)
	{
		this(_id,ent,duration,false,false,1);
	}

	public AbstractEffect(String _id, EntityLivingBase ent, int _duration, boolean _visible, boolean _doesTick, int _tickFreq)
	{
		id = _id;
		visible = _visible;
		doesTick = _doesTick;
		tickFreq = _tickFreq;
		entity = new WeakReference(ent);
		duration = _duration;
	}

	public EntityLivingBase getEntity()
	{
		return entity.get();
	}

	public int getTT()
	{
		return tt;
	}

	public final void update()
	{
		tt++;
		if(!doesTick) return;
		if((tt % tickFreq) == 0)
			apply();
	}

	/**
	 * Called when the effect is added to an entity (including when that entity is created (e.g. when a player logs in or the chunk loads))
	 */
	public void effectAdded(){}

	/**
	 * Called when the effect is removed from an entity
	 */
	public void effectRemoved(){}

	/**
	 * Called every tickFreq if doesTick is true
	 */
	public abstract void apply();

	public ResourceLocation getIcon(){return null;}
	public UVStore getIconLocation(){return null;}

	public final void write(NBTTagCompound nbt)
	{
		nbt.setString("id", id);
		nbt.setInteger("tt", tt);
		nbt.setInteger("dur", duration);
		writeToNBT(nbt);
	}

	public final void read(NBTTagCompound nbt)
	{
		tt = nbt.getInteger("tt");
		readFromNBT(nbt);
	}

	/**
	 * Save the current state of the effect to NBT (id, tt and duration are all saved automatically in the write function)
	 * @param nbt
	 */
	protected abstract void writeToNBT(NBTTagCompound nbt);

	/**
	 * Load the state of the effect from the nbt
	 * (id and dur should be used in the factory to create the effect and tt is read in the read function)
	 * @param nbt
	 */
	protected abstract void readFromNBT(NBTTagCompound nbt);
}
