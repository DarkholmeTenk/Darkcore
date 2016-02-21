package io.darkcraft.darkcore.mod.handlers.packets;

import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import net.minecraft.nbt.NBTTagCompound;

public class EffectsPacketHandler implements IDataPacketHandler
{

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(!data.hasKey("dcEff")) return;

	}

}
