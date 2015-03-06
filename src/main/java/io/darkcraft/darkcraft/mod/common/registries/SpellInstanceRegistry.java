package io.darkcraft.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.PlayerMagicHelper;
import io.darkcraft.darkcraft.mod.common.spellsystem.SpellInstance;

import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class SpellInstanceRegistry
{
	private HashSet<SpellInstance> currentSpellInstances = new HashSet<SpellInstance>();
	private HashSet<SpellInstance> spellInstanceBuffer = new HashSet<SpellInstance>();
	
	@SubscribeEvent
	public void tickNotify(TickEvent ev)
	{
		/*if(Math.random() < 0.05)
		{
			EntityLivingBase pl = Minecraft.getMinecraft().thePlayer;
			SimpleDoubleCoordStore d = PlayerMagicHelper.getAimingAt(pl, 20);
			if(d != null)
				System.out.println(d.toSimpleString());
		}*/
		if(ev.phase.equals(Phase.END))
		{
			processInstances();
		}
	}
	
	public void registerSpellInstance(SpellInstance inst)
	{
		synchronized(spellInstanceBuffer)
		{
			spellInstanceBuffer.add(inst);
		}
	}
	
	private void processInstances()
	{
		synchronized(currentSpellInstances)
		{
			synchronized(spellInstanceBuffer)
			{
				currentSpellInstances.addAll(spellInstanceBuffer);
				spellInstanceBuffer.clear();
			}
			Iterator<SpellInstance> iter = currentSpellInstances.iterator();
			while(iter.hasNext())
			{
				SpellInstance spell = iter.next();
				if(spell.isDead())
				{
					iter.remove();
					continue;
				}
				spell.tick();
					
			}
		}
	}
}
