package io.darkcraft.darkcore.mod.handlers.packets;

import io.darkcraft.darkcore.mod.client.MessageOverlayRenderer;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class MessagePacketHandler implements IDataPacketHandler
{
	public static final String disc = "core.mess";
	public static final int secondsDefault = 4;

	public static DataPacket getDataPacket(ResourceLocation rl, String message, int seconds, UVStore uv)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if(rl != null)
		{
			nbt.setString("rld", rl.getResourceDomain());
			nbt.setString("rlp", rl.getResourcePath());
			if((uv != null) && (uv != UVStore.defaultUV))
				uv.writeToNBT(nbt);
		}
		nbt.setString("m", message);
		nbt.setInteger("s", seconds);
		return new DataPacket(nbt, disc);
	}

	public static DataPacket getDataPacket(ResourceLocation rl, String message, int seconds)
	{
		return getDataPacket(rl, message, seconds, null);
	}

	@Override
	public void handleData(NBTTagCompound data)
	{
		String mess = data.getString("m");
		int secs = data.getInteger("s");
		ResourceLocation rl = null;
		UVStore uv = UVStore.readFromNBT(data);

		if(data.hasKey("rld"))
			rl = new ResourceLocation(data.getString("rld"),data.getString("rlp"));
		MessageOverlayRenderer.addMessage(mess, rl, secs, System.currentTimeMillis(), uv);
	}
}
