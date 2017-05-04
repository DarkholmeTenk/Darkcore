package io.darkcraft.darkcore.mod.abstracts;

import static io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType.TRANSMIT;
import static io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType.WORLD;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;

@NBTSerialisable
public class AbstractTileEntitySer extends AbstractTileEntity
{
	protected final Mapper<AbstractTileEntitySer> WORLD_MAPPER = NBTHelper.getMapper(this, WORLD);
	protected final Mapper<AbstractTileEntitySer> TRANS_MAPPER = NBTHelper.getMapper(this, TRANSMIT);

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		if(TRANS_MAPPER != null)
			TRANS_MAPPER.writeToNBT(tag, this);
		Packet p = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, tag);
		return p;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound nbt = packet.func_148857_g();
		if(TRANS_MAPPER != null)
			TRANS_MAPPER.fillFromNBT(nbt, this);
		super.onDataPacket(net, packet);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey("placed")) super.readFromNBT(nbt);
		if(WORLD_MAPPER != null)
			WORLD_MAPPER.fillFromNBT(nbt, this);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(WORLD_MAPPER != null)
			WORLD_MAPPER.writeToNBT(nbt, this);
	}
}
