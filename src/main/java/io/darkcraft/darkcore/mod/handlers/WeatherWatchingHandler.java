package io.darkcraft.darkcore.mod.handlers;

import io.darkcraft.darkcore.mod.events.LightningEvent;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

public class WeatherWatchingHandler
{
	private Set<EntityLightningBolt> sentEvents = Collections.newSetFromMap(new WeakHashMap<EntityLightningBolt,Boolean>());
	private HashSet<Integer> worldSet = new HashSet();

	private void handleWorldServer(WorldServer ws)
	{
		if(ws == null) return;
		worldSet.remove(ws);
		List effects = ws.weatherEffects;
		if(effects != null)
		{
			for(Object o : effects)
			{
				if(o instanceof EntityLightningBolt)
				{
					EntityLightningBolt lightning = (EntityLightningBolt) o;
					if(!sentEvents.contains(lightning))
					{
						sentEvents.add(lightning);
						MinecraftForge.EVENT_BUS.post(new LightningEvent(lightning));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void handleTick(TickEvent event)
	{
		if((event.side == Side.SERVER) && (event.phase == Phase.END))
		{
			WorldServer[] wss = DimensionManager.getWorlds();
			for(WorldServer ws : wss)
				if(worldSet.contains(ws))
					handleWorldServer(ws);
		}
	}

	@SubscribeEvent
	public void entityConstruct(EntityConstructing event)
	{
		if(event.entity instanceof EntityLightningBolt)
			worldSet.add(WorldHelper.getWorldID(event.entity));
	}
}
