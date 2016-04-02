package io.darkcraft.darkcore.mod.handlers.packets;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PreciseRightClickHandler implements IDataPacketHandler
{
	public static final String disc = "core.preciseright";

	public static void handle(World w, int x, int y, int z, EntityPlayer pl, int s, float i, float j, float k)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if(ServerHelper.isClient())
		{
			nbt.setInteger("w", WorldHelper.getWorldID(w));
			nbt.setInteger("x", x);
			nbt.setInteger("y", y);
			nbt.setInteger("z", z);
			nbt.setInteger("s", s);
			nbt.setFloat("i", i);
			nbt.setFloat("j", j);
			nbt.setFloat("k", k);
			nbt.setString("pl", PlayerHelper.getUsername(pl));
			DataPacket dp = new DataPacket(nbt,disc);
			DarkcoreMod.networkChannel.sendToServer(dp);
		}
	}

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(ServerHelper.isServer())
		{
			World w = WorldHelper.getWorld(data.getInteger("w"));
			EntityPlayer pl = PlayerHelper.getPlayer(data.getString("pl"));
			if((w == null) || (pl == null)) return;
			int x = data.getInteger("x");
			int y = data.getInteger("y");
			int z = data.getInteger("z");
			int s = data.getInteger("s");
			float i = data.getFloat("i");
			float j = data.getFloat("j");
			float k = data.getFloat("k");
			Block b = w.getBlock(x, y, z);
			TileEntity te = w.getTileEntity(x, y, z);
			if(!((b instanceof IActivatablePrecise) || (te instanceof IActivatablePrecise))) return;
			if(b instanceof IActivatablePrecise) ((IActivatablePrecise)b).activate(pl, s, x+Math.max(i,0.9999f), y+Math.max(j,0.9999f), z+Math.max(k,0.9999f));
			if(te instanceof IActivatablePrecise) ((IActivatablePrecise)te).activate(pl, s, i,j,k);
		}
	}

}
