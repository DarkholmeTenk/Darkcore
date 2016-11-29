package io.darkcraft.darkcore.mod.abstracts;

import static io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType.TRANSMIT;
import static io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType.WORLD;

import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;

public class AbstractTileEntitySer extends AbstractTileEntity
{
	{
		NBTHelper.getMapper(getClass(), TRANSMIT);
		NBTHelper.getMapper(getClass(), WORLD);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		NBTHelper.getMapper(getClass(), TRANSMIT).writeToNBT(tag, this);
		Packet p = new Packet132TileEntityData(xCoord, yCoord, zCoord, 3, tag);
		return p;
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
	{
		NBTTagCompound nbt = packet.data;
		NBTHelper.getMapper(getClass(), TRANSMIT).fillFromNBT(nbt, this);
		super.onDataPacket(net, packet);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey("placed")) super.readFromNBT(nbt);
		NBTHelper.getMapper(getClass(), WORLD).fillFromNBT(nbt, this);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTHelper.getMapper(getClass(), WORLD).writeToNBT(nbt, this);
	}
}
