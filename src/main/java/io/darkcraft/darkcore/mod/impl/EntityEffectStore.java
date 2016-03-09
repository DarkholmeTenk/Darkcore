package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.handlers.packets.EffectsPacketHandler;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.network.DataPacket;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityEffectStore implements IExtendedEntityProperties
{
	private HashMap<String,AbstractEffect> effects = new HashMap<String,AbstractEffect>();
	private final WeakReference<EntityLivingBase> entity;
	private boolean updateQueued = true;

	public EntityEffectStore(EntityLivingBase ent)
	{
		entity = new WeakReference(ent);
	}

	public EntityLivingBase getEntity()
	{
		return entity.get();
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
		if(updateQueued)
		{
			sendUpdate();
			updateQueued = false;
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
		sendUpdate();
	}

	public void remove(String id)
	{
		AbstractEffect eff = effects.remove(id);
		if(eff != null) eff.effectRemoved();
		sendUpdate();
	}

	public Collection<AbstractEffect> getEffects()
	{
		return effects.values();
	}

	private void sendUpdate()
	{
		if(ServerHelper.isClient()) return;
		if((getEntity() == null) || getEntity().isDead) return;
		if(!(getEntity() instanceof EntityPlayerMP)) return;
		EntityPlayerMP pl = (EntityPlayerMP) getEntity();
		NBTTagCompound nbt = new NBTTagCompound();
		saveNBTData(nbt);
		nbt.setString("dcEff", "plOnly");
		DataPacket dp = new DataPacket(nbt,EffectsPacketHandler.effPacketDisc);
		DarkcoreMod.networkChannel.sendTo(dp, pl);
	}

	public void queueUpdate()
	{
		updateQueued = true;
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
	public void init(Entity entity, World world)
	{
	}

}
