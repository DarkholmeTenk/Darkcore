package io.darkcraft.darkcore.mod.network;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.CustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class PacketHandler
{
	HashMap<Integer, IDataPacketHandler>	handlers	= new HashMap<Integer, IDataPacketHandler>();

	@SubscribeEvent
	public void handleCustomClientPacket(ClientCustomPacketEvent event)
	{
		handleCustomPacket(event);
	}

	@SubscribeEvent
	public void handleCustomServerPacket(ServerCustomPacketEvent event)
	{
		handleCustomPacket(event);
	}

	@SubscribeEvent
	public void handleCustomPacket(CustomPacketEvent event)
	{
		if (DarkcoreMod.debugText) System.out.println("Packet received!");
		FMLProxyPacket p = event.packet;
		int discriminator = p.payload().getByte(0);
		p.payload().readerIndex(1);
		p.payload().discardReadBytes();
		DataPacket dp = new DataPacket(p.payload());
		NBTTagCompound nbt = dp.getNBT();
		if (handlers.containsKey(discriminator))
			handlers.get(discriminator).handleData(nbt);
		else
			System.err.println("Packet with unknown discriminator " + discriminator + " received!");
	}

	public boolean registerHandler(int discriminator, IDataPacketHandler handler)
	{
		if (handlers.containsKey(discriminator))
		{
			IDataPacketHandler old = handlers.get(discriminator);
			throw new RuntimeException("Unable to register packet handler with discriminator " + discriminator + ":" + handler + ":" + old);
		}
		handlers.put(discriminator, handler);
		return true;
	}

	public byte getNextFreeDiscriminator()
	{
		Set<Integer> taken = handlers.keySet();
		for(int i = 0; i<256;i++)
			if(!taken.contains(i))
				return (byte)i;
		return -1;
	}
}
