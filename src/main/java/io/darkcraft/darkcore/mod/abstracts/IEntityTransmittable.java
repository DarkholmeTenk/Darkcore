package io.darkcraft.darkcore.mod.abstracts;

import net.minecraft.nbt.NBTTagCompound;

public interface IEntityTransmittable
{
	public void writeToNBTTransmittable(NBTTagCompound nbt);

	public void readFromNBTTransmittable(NBTTagCompound nbt);
}
