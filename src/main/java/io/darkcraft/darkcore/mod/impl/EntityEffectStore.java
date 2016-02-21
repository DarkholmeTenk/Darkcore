package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityEffectStore implements IExtendedEntityProperties
{
	private HashMap<String,AbstractEffect> effects = new HashMap<String,AbstractEffect>();
	public final EntityLivingBase entity;

	public EntityEffectStore(EntityLivingBase ent)
	{
		entity = ent;
	}

	public void tick()
	{
		if(entity.isDead) return;
		Iterator<Entry<String, AbstractEffect>> iter = effects.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String, AbstractEffect> effEnt = iter.next();
			AbstractEffect eff = effEnt.getValue();
			eff.update();
			if((eff.duration != -1) && (eff.duration <= eff.getTT()))
				iter.remove();
		}
	}

	public boolean shouldBeWatched()
	{
		if(entity.isDead) return false;
		if(effects.size() == 0) return false;
		for(AbstractEffect eff : effects.values())
			if((eff.duration != -1) || eff.doesTick)
				return true;
		return false;
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt)
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
	public void loadNBTData(NBTTagCompound nbt)
	{
		effects.clear();
		int i = 0;
		while(nbt.hasKey("eff"+i))
		{
			NBTTagCompound effTag = nbt.getCompoundTag("eff"+i);
			AbstractEffect effect = EffectHandler.getEffect(effTag);
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
	public void init(Entity entity, World world)
	{
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
	}

	public void remove(String id)
	{
		effects.remove(id);
	}

}
