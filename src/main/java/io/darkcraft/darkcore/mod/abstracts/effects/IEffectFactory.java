package io.darkcraft.darkcore.mod.abstracts.effects;

import net.minecraft.nbt.NBTTagCompound;

public interface IEffectFactory
{
	public AbstractEffect createEffect(String id, NBTTagCompound nbt);
}
