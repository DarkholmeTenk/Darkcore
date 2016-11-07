package io.darkcraft.darkcore.mod.events;

import net.minecraft.entity.effect.EntityLightningBolt;

public class LightningEvent extends Event
{
	public EntityLightningBolt lightning;

	public LightningEvent(EntityLightningBolt lightning)
	{
		this.lightning = lightning;
	}

}
