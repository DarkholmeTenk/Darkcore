package io.darkcraft.darkcore.mod.interfaces;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Implement this on a packet handler and call DarkcoreMod.packetHandler.registerHandler() to register your packet
 * handler with the DarkCore networking system.<br>
 *
 * See {@linkplain io.darkcraft.darkcore.mod.network.DataPacket#DataPacket(NBTTagCompound, byte) DataPacket} for info
 * @author dark
 *
 */
public interface IDataPacketHandler
{
	/**
	 * Called when a packet is received with the discriminator this handler is registered with
	 * @param data the nbt data the packet contained
	 */
	public void handleData(NBTTagCompound data);
}
