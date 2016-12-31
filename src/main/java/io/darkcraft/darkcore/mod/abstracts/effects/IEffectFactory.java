package io.darkcraft.darkcore.mod.abstracts.effects;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public interface IEffectFactory
{
	public AbstractEffect createEffect(Entity ent, String id, NBTTagCompound nbt);
}
