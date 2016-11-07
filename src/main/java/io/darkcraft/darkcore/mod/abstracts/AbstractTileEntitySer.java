package io.darkcraft.darkcore.mod.abstracts;

import static io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType.TRANSMIT;
import static io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType.WORLD;

import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

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
		Packet p = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, tag);
		return p;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound nbt = packet.func_148857_g();
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
