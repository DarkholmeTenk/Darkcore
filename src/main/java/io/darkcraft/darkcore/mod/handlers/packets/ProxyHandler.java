package io.darkcraft.darkcore.mod.handlers.packets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.darkcore.mod.proxy.BaseProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class ProxyHandler implements IDataPacketHandler
{
	public static final String disc = "core.proxyhandler";
	
	public static void sendToAll(NBTTagCompound data)
	{
		DataPacket dp = new DataPacket(data, disc);
		DarkcoreMod.networkChannel.sendToAll(dp);
	}
	
	public static void sendToDimension(int dimension, NBTTagCompound data)
	{
		DataPacket dp = new DataPacket(data, disc);
		DarkcoreMod.networkChannel.sendToDimension(dp, dimension);
	}
	
	public static void sendToPlayer(EntityPlayerMP player, NBTTagCompound data)
	{
		DataPacket dp = new DataPacket(data, disc);
		DarkcoreMod.networkChannel.sendTo(dp, player);
	}

	@Override
	public void handleData(NBTTagCompound data)
	{
		String clazz = data.getString("myClass");
		String method = data.getString("method");
		
		BaseProxy bp = BaseProxy.getProxy(clazz);
		for(Method m : bp.getClass().getMethods())
		{
			if(!m.getName().equals(method))
				continue;
			Object[] objects = NBTHelper.deserialise(SerialisableType.TRANSMIT, data);
			
			try
			{
				m.invoke(bp, objects);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				System.err.println("Exception occured during proxy call with data: " + data);
				e.printStackTrace();
			}
			return;
		}
	}

}
