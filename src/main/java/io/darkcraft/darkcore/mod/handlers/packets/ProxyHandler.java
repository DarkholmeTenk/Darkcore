package io.darkcraft.darkcore.mod.handlers.packets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.handlers.containers.PlayerContainer;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.darkcore.mod.proxy.BaseProxy;

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

	public static void sendToDimension(World world, NBTTagCompound data)
	{
		if((world != null) && (world.provider != null))
			sendToDimension(WorldHelper.getWorldID(world), data);
	}

	public static void sendToDimension(Entity entity, NBTTagCompound data)
	{
		if(entity != null)
			sendToDimension(entity.worldObj, data);
	}

	public static void sendToDimension(SimpleCoordStore scs, NBTTagCompound data)
	{
		if(scs != null)
			sendToDimension(scs.world, data);
	}

	public static void sendToDimension(SimpleDoubleCoordStore scds, NBTTagCompound data)
	{
		if(scds != null)
			sendToDimension(scds.world, data);
	}

	public static void sendToPlayer(EntityPlayerMP player, NBTTagCompound data)
	{
		DataPacket dp = new DataPacket(data, disc);
		DarkcoreMod.networkChannel.sendTo(dp, player);
	}

	public static void sendToPlayer(PlayerContainer player, NBTTagCompound data)
	{
		EntityPlayer pl = player.getEntity();
		if(pl instanceof EntityPlayerMP)
			sendToPlayer((EntityPlayerMP) pl, data);
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
			catch (Throwable e)
			{
				System.err.println(e.getMessage());
				System.err.println("Exception occured during proxy call with data: " + data);
				for(Object o : objects)
					System.out.println(o);
				if(e instanceof InvocationTargetException)
					e = e.getCause();
				e.printStackTrace();
			}
			return;
		}
	}

}
