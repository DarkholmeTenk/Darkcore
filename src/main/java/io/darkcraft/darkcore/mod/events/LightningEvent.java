package io.darkcraft.darkcore.mod.events;

import net.minecraft.entity.effect.EntityLightningBolt;
import cpw.mods.fml.common.eventhandler.Event;

public class LightningEvent extends Event
{
	public EntityLightningBolt lightning;

	public LightningEvent(EntityLightningBolt lightning)
	{
		this.lightning = lightning;
	}

}
