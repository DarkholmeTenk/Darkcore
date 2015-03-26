package io.darkcraft.darkcore.mod.network;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.CustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class PacketHandler
{
	HashMap<Integer, IDataPacketHandler> handlers = new HashMap<Integer, IDataPacketHandler>();
	
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
		System.out.println("Packet received!");
		FMLProxyPacket p = event.packet;
		int discriminator = p.payload().getByte(0);
		p.payload().readerIndex(1);
		p.payload().discardReadBytes();
		DataPacket dp = new DataPacket(p.payload());
		NBTTagCompound nbt = dp.getNBT();
		if(handlers.containsKey(discriminator))
			handlers.get(discriminator).handleData(nbt);
	}
	
	public boolean registerHandler(int discriminator, IDataPacketHandler handler)
	{
		if(handlers.containsKey(discriminator))
			return false;
		handlers.put(discriminator, handler);
		return true;
	}
}
