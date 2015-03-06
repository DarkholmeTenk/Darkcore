package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	
	public static void giveItemStack(EntityPlayer pl, ItemStack is)
	{
		EntityItem ie = new EntityItem(pl.worldObj, pl.posX, pl.posY, pl.posZ, is);
		ie.delayBeforeCanPickup = 0;
		pl.worldObj.spawnEntityInWorld(ie);
	}
	
	public static void dropItemStack(ItemStack is, SimpleDoubleCoordStore sdcs)
	{
		if(is == null || sdcs == null)
			return;
		World w = sdcs.getWorldObj();
		if(w == null)
			return;
		EntityItem ie = new EntityItem(w, sdcs.x, sdcs.y, sdcs.z, is);
		ie.delayBeforeCanPickup = 0;
		w.spawnEntityInWorld(ie);
	}
}
