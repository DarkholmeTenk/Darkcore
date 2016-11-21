package io.darkcraft.darkcore.mod.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.abstracts.effects.IEffectFactory;
import io.darkcraft.darkcore.mod.abstracts.effects.StackedEffect;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

public class EffectHandler
{
	private static HashSet<IEffectFactory> factories = new HashSet<IEffectFactory>();
	private static Set<EntityEffectStore> watchedStores = Collections.newSetFromMap(new WeakHashMap<EntityEffectStore,Boolean>());

	public static void registerEffectFactory(IEffectFactory factory)
	{
		factories.add(factory);
	}

	private static AbstractEffect getStackedEffect(EntityLivingBase ent, NBTTagCompound nbt)
	{
		int num = nbt.getInteger("numSubs");
		if(num == 0) return null;
		AbstractEffect[] subs = new AbstractEffect[num];
		for(int i = 0; i < num; i++)
		{
			NBTTagCompound subNBT = nbt.getCompoundTag("sub"+i);
			AbstractEffect e = getEffect(ent, subNBT);
			if(e == null) return null;
			if(num == 1)
				return e;
			subs[i] = e;
		}
		StackedEffect se = new StackedEffect(subs);
		se.read(nbt);
		return se;
	}

	public static AbstractEffect getEffect(EntityLivingBase ent, NBTTagCompound nbt)
	{
		String id = nbt.getString("id");
		if((id == null) || id.isEmpty()) return null;
		if(StackedEffect.stackId.equals(id))
			return getStackedEffect(ent, nbt);
		for(IEffectFactory fact : factories)
		{
			AbstractEffect effect = fact.createEffect(ent, id, nbt);
			if(effect != null)
			{
				effect.read(nbt);
				return effect;
			}
		}
		return null;
	}

	public static void addWatchedStore(EntityEffectStore store)
	{
		synchronized(watchedStores)
		{
			watchedStores.add(store);
		}
	}

	public static EntityEffectStore getEffectStore(EntityLivingBase ent)
	{
		IExtendedEntityProperties store = ent.getExtendedProperties("dcEff");
		if((store == null) || !(store instanceof EntityEffectStore))
		{
			store = new EntityEffectStore(ent);
			ent.registerExtendedProperties(EntityEffectStore.disc, store);
		}
		return (EntityEffectStore) store;
	}

	@SubscribeEvent
	public void entConstructEvent(EntityConstructing event)
	{
		Entity ent = event.entity;
		if(ent instanceof EntityLivingBase)
			ent.registerExtendedProperties("dcEff", new EntityEffectStore((EntityLivingBase) ent));
	}

	@SubscribeEvent
	public void entTickEvent(TickEvent tick)
	{
		if((ServerHelper.isClient() && (tick.type != Type.CLIENT)) || (ServerHelper.isServer() && (tick.type != Type.SERVER))) return;
		if((tick.phase != Phase.START)) return;
		synchronized(watchedStores)
		{
			Iterator<EntityEffectStore> iter = watchedStores.iterator();
			while(iter.hasNext())
			{
				EntityEffectStore store = iter.next();
				if(store.client != (tick.type == Type.CLIENT)) continue;
				store.tick();
				if(!store.shouldBeWatched())
					iter.remove();
			}
		}
	}
}
