package io.darkcraft.darkcore.mod.network;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class DataPacket extends FMLProxyPacket
{
	public enum PacketType
	{
		SOUND, PARTICLE;

		public static PacketType find(int i)
		{
			PacketType[] vals = PacketType.values();
			if (i >= 0 && i < vals.length)
				return vals[i];
			return null;
		}
	}

	public ByteBuf	buffer;

	public DataPacket(ByteBuf payload, String channel)
	{
		super(payload, channel);
		buffer = payload;
	}

	public DataPacket(ByteBuf payload, NBTTagCompound nbt, byte discriminator)
	{
		super(payload, "tardis");
		payload.writeByte(discriminator);
		payload.writerIndex(1);
		try
		{
			ByteBufOutputStream stream = new ByteBufOutputStream(payload);
			ServerHelper.writeNBT(nbt, stream);
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public DataPacket(NBTTagCompound nbt, byte discriminator)
	{
		this(Unpooled.buffer(),nbt,discriminator);
	}

	public DataPacket(ByteBuf payload)
	{
		super(payload, "darkcore");
		buffer = payload;
	}

	public NBTTagCompound getNBT()
	{
		ByteBufInputStream in = new ByteBufInputStream(buffer);
		NBTTagCompound nbt = ServerHelper.readNBT(in);
		try
		{
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return nbt;
	}

}
