package io.darkcraft.darkcore.mod.abstracts.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractEffect
{
	public final String id;
	public final EntityLivingBase entity;
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
		entity = ent;
		duration = _duration;
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

	protected abstract void writeToNBT(NBTTagCompound nbt);

	protected abstract void readFromNBT(NBTTagCompound nbt);
}
