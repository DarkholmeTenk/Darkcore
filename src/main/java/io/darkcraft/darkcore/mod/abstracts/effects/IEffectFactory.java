package io.darkcraft.darkcore.mod.abstracts.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IEffectFactory
{
	public AbstractEffect createEffect(EntityLivingBase ent, String id, NBTTagCompound nbt);
}
