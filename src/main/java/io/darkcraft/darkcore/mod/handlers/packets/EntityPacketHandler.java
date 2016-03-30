package io.darkcraft.darkcore.mod.handlers.packets;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.IEntityTransmittable;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityPacketHandler implements IDataPacketHandler
{
	public static final String disc = "core.ent";
	public static void syncEntity(Entity ent)
	{
		if((ent instanceof IEntityTransmittable) && ServerHelper.isServer())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("entid", ent.getEntityId());
			int wid = WorldHelper.getWorldID(ent);
			nbt.setInteger("world", wid);
			((IEntityTransmittable)ent).writeToNBTTransmittable(nbt);
			DataPacket dp = new DataPacket(nbt,disc);
			DarkcoreMod.networkChannel.sendToAllAround(dp, new TargetPoint(wid, ent.posX, ent.posY, ent.posZ, 120));
		}
	}

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(ServerHelper.isServer()) return;
		int world = data.getInteger("world");
		int id = data.getInteger("entid");
		World w = WorldHelper.getWorld(world);
		if(w == null) return;
		Entity ent = w.getEntityByID(id);
		if((ent == null) || !(ent instanceof IEntityTransmittable)) return;
		((IEntityTransmittable)ent).readFromNBTTransmittable(data);
	}

}
