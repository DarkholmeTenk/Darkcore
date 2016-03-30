package io.darkcraft.darkcore.mod.network;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.CustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class PacketHandler
{
	HashMap<String, IDataPacketHandler>	handlers	= new HashMap<String, IDataPacketHandler>();

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
		int length = p.payload().getByte(0);
		byte[] bytes = new byte[length];
		p.payload().readerIndex(1);
		p.payload().readBytes(bytes);
		p.payload().readerIndex(1+length);
		p.payload().discardReadBytes();
		String discriminator = new String(bytes);
		DataPacket dp = new DataPacket(p.payload());
		NBTTagCompound nbt = dp.getNBT();
		if (handlers.containsKey(discriminator))
			handlers.get(discriminator).handleData(nbt);
		else
			System.err.println("Packet with unknown discriminator " + discriminator + " received!");
	}

	public boolean registerHandler(String discriminator, IDataPacketHandler handler)
	{
		if (handlers.containsKey(discriminator))
		{
			IDataPacketHandler old = handlers.get(discriminator);
			throw new RuntimeException("Unable to register packet handler " + discriminator + " - conflict " + handler + ":" + old);
		}
		if(discriminator.getBytes().length > 255)
			throw new RuntimeException("Unable to register packet handler " + discriminator + " - name too long");
		handlers.put(discriminator, handler);
		return true;
	}
}
