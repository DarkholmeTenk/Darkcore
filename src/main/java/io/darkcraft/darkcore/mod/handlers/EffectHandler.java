package io.darkcraft.darkcore.mod.handlers;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.abstracts.effects.IEffectFactory;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;

public class EffectHandler
{
	private static HashSet<IEffectFactory> factories = new HashSet<IEffectFactory>();
	private static Set<EntityEffectStore> watchedStores = Collections.newSetFromMap(new WeakHashMap<EntityEffectStore,Boolean>());

	public static void registerEffectFactory(IEffectFactory factory)
	{
		factories.add(factory);
	}

	public static AbstractEffect getEffect(EntityLivingBase ent, NBTTagCompound nbt)
	{
		String id = nbt.getString("id");
		if((id == null) || id.isEmpty()) return null;
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
			ent.registerExtendedProperties("dcEff", store);
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
		if((tick.phase != Phase.END) || (ServerHelper.isIntegratedClient())) return;
		synchronized(watchedStores)
		{
			Iterator<EntityEffectStore> iter = watchedStores.iterator();
			while(iter.hasNext())
			{
				EntityEffectStore store = iter.next();
				store.tick();
				if(!store.shouldBeWatched())
					iter.remove();
			}
		}
	}
}
