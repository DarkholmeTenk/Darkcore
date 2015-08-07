package io.darkcraft.darkcore.mod.interfaces;

import net.minecraft.entity.player.EntityPlayer;

public interface IActivatablePrecise
{
	public boolean activate(EntityPlayer ent, int side, float x, float y, float z);
}
