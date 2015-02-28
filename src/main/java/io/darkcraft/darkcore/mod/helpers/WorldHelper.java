package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class WorldHelper
{
	/**Attempts to get the world specified by id. If this is run on the client, it will either return
	 * the client side world or null.
	 * @return either the world or null
	 */
	public static World getWorld(int id)
	{
		if(!ServerHelper.isServer())
			return DarkcoreMod.proxy.getWorld(id);
		else
			return getWorldServer(id);
	}
	
	public static World getWorldServer(int id)
	{
		MinecraftServer s = MinecraftServer.getServer();
		if(s != null)
			return s.worldServerForDimension(id);
		return null;
	}

	public static int getWorldID(World w)
	{
		return w.provider.dimensionId;
	}

	public static int getWorldID(TileEntity te)
	{
		return getWorldID(te.getWorldObj());
	}
	
	public static int getWorldID(EntityLivingBase ent)
	{
		return getWorldID(ent.worldObj);
	}
}
