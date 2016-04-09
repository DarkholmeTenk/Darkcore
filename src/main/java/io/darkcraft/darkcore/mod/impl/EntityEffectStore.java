package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.abstracts.AbstractEntityDataStore;
import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityEffectStore extends AbstractEntityDataStore
{
	public static final String disc = "dcEff";
	private HashMap<String,AbstractEffect> effects = new HashMap<String,AbstractEffect>();

	public EntityEffectStore(EntityLivingBase ent)
	{
		super(ent, disc);
	}

	@Override
	public boolean shouldPersistDeaths()
	{
		return false;
	}

	public void tick()
	{
		if((getEntity() == null) || getEntity().isDead) return;
		Iterator<Entry<String, AbstractEffect>> iter = effects.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String, AbstractEffect> effEnt = iter.next();
			AbstractEffect eff = effEnt.getValue();
			eff.update();
			int tt = eff.getTT();
			if((eff.duration != -1) && (eff.duration <= tt))
			{
				eff.effectRemoved();
				iter.remove();
			}
		}
	}

	public boolean shouldBeWatched()
	{
		if((getEntity() == null) || getEntity().isDead) return false;
		if(effects.size() == 0) return false;
		for(AbstractEffect eff : effects.values())
			if((eff.duration != -1) || eff.doesTick)
				return true;
		return false;
	}

	public AbstractEffect getEffect(String id)
	{
		return effects.get(id);
	}

	public boolean hasEffect(String id)
	{
		return effects.containsKey(id);
	}

	public void addEffect(AbstractEffect effect)
	{
		effects.put(effect.id, effect);
		effect.effectAdded();
		if(shouldBeWatched())
			EffectHandler.addWatchedStore(this);
		queueUpdate();
	}

	public void remove(String id)
	{
		AbstractEffect eff = effects.remove(id);
		if(eff != null) eff.effectRemoved();
		queueUpdate();
	}

	public Collection<AbstractEffect> getEffects()
	{
		return effects.values();
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt){}

	@Override
	public void readFromNBT(NBTTagCompound nbt){}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		int i = 0;
		for(AbstractEffect eff : effects.values())
		{
			if(eff == null) continue;
			NBTTagCompound effTag = new NBTTagCompound();
			eff.write(effTag);
			nbt.setTag("eff"+(i++), effTag);
		}
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		if((getEntity() == null) || getEntity().isDead) return;
		effects.clear();
		int i = 0;
		while(nbt.hasKey("eff"+i))
		{
			NBTTagCompound effTag = nbt.getCompoundTag("eff"+i);
			AbstractEffect effect = EffectHandler.getEffect(getEntity(), effTag);
			if(effect != null)
				addEffect(effect);
			else
				System.err.println("Effect failed to load");
			i++;
		}
		if(shouldBeWatched())
			EffectHandler.addWatchedStore(this);
	}

	@Override
	public boolean notifyArea()
	{
		return true;
	}

}
