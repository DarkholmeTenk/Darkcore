package io.darkcraft.darkcore.mod.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface IDataPacketHandler
{
	public void handleData(NBTTagCompound data);
}
