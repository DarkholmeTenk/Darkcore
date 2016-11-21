package io.darkcraft.darkcore.mod.handlers;

import io.darkcraft.darkcore.mod.events.LightningEvent;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.relauncher.Side;

public class WeatherWatchingHandler
{
	private Set<EntityLightningBolt> sentEvents = Collections.newSetFromMap(new WeakHashMap<EntityLightningBolt,Boolean>());
	private Set<World> worldSet = Collections.newSetFromMap(new WeakHashMap<World,Boolean>());

	private boolean handleWorld(World w)
	{
		if(w == null) return true;
		List effects = w.weatherEffects;
		boolean retVal = false;
		if(effects != null)
		{
			for(Object o : effects)
			{
				if(o instanceof EntityLightningBolt)
				{
					EntityLightningBolt lightning = (EntityLightningBolt) o;
					if(!sentEvents.contains(lightning))
					{
						retVal = true;
						sentEvents.add(lightning);
						MinecraftForge.EVENT_BUS.post(new LightningEvent(lightning));
					}
				}
			}
		}
		return retVal;
	}

	@SubscribeEvent
	public void handleTick(TickEvent event)
	{
		if((event.side == Side.SERVER) && (event.phase == Phase.END))
		{
			if(worldSet.isEmpty()) return;
			Iterator<World> iter = worldSet.iterator();
			while(iter.hasNext())
			{
				World w = iter.next();
				if(w != null)
					if(handleWorld(w))
						iter.remove();
			}
		}
	}

	@SubscribeEvent
	public void entityConstruct(EntityConstructing event)
	{
		if(ServerHelper.isServer())
			if((event.entity != null) && (event.entity instanceof EntityLightningBolt))
				worldSet.add(event.entity.worldObj);
	}
}
