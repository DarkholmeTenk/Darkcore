package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractWorldDataStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldHelper
{


	private static HashMap<Integer, String> worldNameMap = new HashMap();

	public static void clearWorldNameMap()
	{
		worldNameMap.clear();
	}

	/**
	 * Attempts to get the world specified by id. If this is run on the client, it will either return the client side world or null.
	 *
	 * @return either the world or null
	 */
	public static World getWorld(int id)
	{
		if (ServerHelper.isClient())
			return DarkcoreMod.proxy.getWorld(id);
		else
			return getWorldServer(id);
	}

	/**
	 * Will get the {@link WorldServer} specified by the id, will print an error to the console if the world doesn't exist
	 * @param id
	 * @return
	 */
	public static WorldServer getWorldServer(int id)
	{
		try
		{
			MinecraftServer s = MinecraftServer.getServer();
			if (s != null) return s.worldServerForDimension(id);
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
		}
		if(DarkcoreMod.debugText)
			Thread.dumpStack();
		return null;
	}

	/**
	 * @param w the world to get the ID of
	 * @return the id of w
	 */
	public static int getWorldID(World w)
	{
		return w.provider.dimensionId;
	}

	/**
	 * @return the id of te's world
	 */
	public static int getWorldID(TileEntity te)
	{
		return getWorldID(te.getWorldObj());
	}

	/**
	 * @param ent
	 * @return the id of ent's world
	 */
	public static int getWorldID(Entity ent)
	{
		return getWorldID(ent.worldObj);
	}

	/**
	 * @return the client world
	 */
	@SideOnly(Side.CLIENT)
	private static World getCW()
	{
		return Minecraft.getMinecraft().theWorld;
	}

	/**
	 * @return the id of the client world
	 */
	public static int getClientWorldID()
	{
		if(ServerHelper.isClient())
			return getWorldID(getCW());
		return 0;
	}

	/**
	 * @param worldID the ID of the world to get the name of
	 * @return the name of worldID
	 */
	public static String getDimensionName(int worldID)
	{
		if(worldNameMap.containsKey(worldID))
			return worldNameMap.get(worldID);
		World w = getWorld(worldID);
		return getDimensionName(w);
	}

	/**
	 * @param w the world to get the name of
	 * @return the name of w
	 */
	public static String getDimensionName(World w)
	{
		int id = getWorldID(w);
		if(worldNameMap.containsKey(id))
			return worldNameMap.get(id);
		if (w != null)
		{
			String name = w.provider.getDimensionName();
			worldNameMap.put(id, name);
			return name;
		}
		return "Unknown";
	}

	public static void setDimensionName(World w, String newName)
	{
		int id = getWorldID(w);
		WorldNameStore.i.update(w,newName);
		if((newName == null) || newName.isEmpty() || newName.equals("null"))
			worldNameMap.remove(id);
		else
			worldNameMap.put(id, newName);
	}

	public static boolean sameItem(ItemStack a, ItemStack b)
	{
		if ((a == null) ^ (b == null)) return false;
		if (a.getItem() != null) return a.getItem().equals(b.getItem()) && (a.getItemDamage() == b.getItemDamage());
		return false;
	}

	/**
	 * Spawns an entity item containing is at pl's location with 0 pickup delay, effectively giving it to them
	 * @param pl
	 * @param is
	 */
	public static void giveItemStack(EntityPlayer pl, ItemStack is)
	{
		if((is == null) || (pl == null)) return;
		if(ServerHelper.isClient())
		{
			Exception e = new RuntimeException("Mod is trying to give item stack client side!");
			e.printStackTrace();
			return;
		}
		EntityItem ie = new EntityItem(pl.worldObj, pl.posX, pl.posY, pl.posZ, is);
		ie.delayBeforeCanPickup = 0;
		pl.worldObj.spawnEntityInWorld(ie);
	}

	/**
	 * Drops an entity item containing is at the {@link SimpleDoubleCoordStore} specified
	 * @param is the itemstack to be in the entity
	 * @param sdcs the location to drop the item
	 */
	public static void dropItemStack(ItemStack is, SimpleDoubleCoordStore sdcs)
	{
		if ((is == null) || (sdcs == null)) return;
		if(ServerHelper.isClient())
		{
			Exception e = new RuntimeException("Mod is trying to drop item stack client side!");
			e.printStackTrace();
			return;
		}
		World w = sdcs.getWorldObj();
		if (w == null) return;
		EntityItem ie = new EntityItem(w, sdcs.x, sdcs.y, sdcs.z, is);
		ie.delayBeforeCanPickup = 2;
		w.spawnEntityInWorld(ie);
	}

	/**
	 * Puts is into dest
	 * @param is the itemstack to put
	 * @param dest the IInventory to put it into
	 * @return any remaining items which could not be transferred, or null if transfer was completely successful
	 */
	public static ItemStack transferItemStack(ItemStack is, IInventory dest)
	{
		int size = dest.getSizeInventory();
		ItemStack remaining = is.copy();
		for (int i = 0; (i < size) && (remaining.stackSize > 0); i++)
		{
			if (dest.isItemValidForSlot(i, remaining))
			{
				ItemStack inSlot = dest.getStackInSlot(i);
				if (inSlot == null)
				{
					dest.setInventorySlotContents(i, remaining);
					return null;
				}
				else if (sameItem(remaining, inSlot))
				{
					int am = Math.min(remaining.stackSize, inSlot.getMaxStackSize() - inSlot.stackSize);
					inSlot.stackSize += am;
					remaining.stackSize -= am;
				}
			}
		}
		if (remaining.stackSize > 0) return remaining;
		return null;
	}

	public static boolean softBlock(World w, int x, int y, int z)
	{
		if(w == null)
			return false;
		Block b = w.getBlock(x, y, z);
		if (b == null) return w.isAirBlock(x, y, z);
		Boolean valid = w.isAirBlock(x, y, z) || b.isFoliage(w, x, y, z) || b.isReplaceable(w, x, y, z) || (b instanceof BlockFire);
		if (valid) return valid;
		if (b.getCollisionBoundingBoxFromPool(w, x, y, z) == null) return true;
		return false;
	}

	public static boolean softBlock(SimpleDoubleCoordStore pos)
	{
		if(pos == null)
			return false;
		return softBlock(pos.getWorldObj(), pos.iX, pos.iY, pos.iZ);
	}

	/**
	 * @param wID the world to get the time for
	 * @return the time in the world scaled between 0(12am) and 0.9999...(11:59:59...pm)
	 */
	public static double getWorldTime(int wID)
	{
		int length = 24000;
		World w = getWorld(wID);
		return ((w.getWorldTime() + (length / 4)) % length) / (double) length;
	}

	/**
	 * @param wID the world ID to get the time of
	 * @return a string representing the time in world wID
	 */
	public static String getWorldTimeString(int wID)
	{
		double t = getWorldTime(wID);
		double timeScaled = t * 24;
		double mins = (timeScaled % 1) * 60;
		int h = (int) Math.floor(timeScaled);
		String m = String.format("%02d",(int) Math.floor(mins));
		if(DarkcoreMod.splitTime)
		{
			String s;
			if(h == 0)
				s = "12";
			else if(h > 12)
				s = (h - 12)+"";
			else
				s = h+"";
			return s + ":" + m + (h>=12?"PM":"AM");
		}
		else
			return h + ":" + m;
	}

	public static boolean removeTE(World w, TileEntity te)
	{
		if((w == null) || (te == null))
			return false;
		w.setBlockToAir(te.xCoord, te.yCoord, te.zCoord);
		return true;
	}

	public static class WorldNameStore extends AbstractWorldDataStore
	{
		private static WorldNameStore i;

		public static void refreshWorldNameStore()
		{
			i = new WorldNameStore();
			i.load();
			i.save();
		}

		private HashMap<Integer,String> nameMap = new HashMap();

		public WorldNameStore(){super("dcDimNameStore",0);}

		public WorldNameStore(String s){super(s);}

		public void update(World w, String newName)
		{
			int id = getWorldID(w);
			if((newName == null) || newName.isEmpty() || newName.equals("null"))
				nameMap.remove(id);
			else
			nameMap.put(id, newName);
			markDirty();
		}

		private void copyToWorldNameMap()
		{
			for(Entry<Integer,String> ent : nameMap.entrySet())
				worldNameMap.put(ent.getKey(), ent.getValue());
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt)
		{
			nameMap.clear();
			for(int i = 0; nbt.hasKey("wi"+i); i++)
				nameMap.put(nbt.getInteger("wi"+i), nbt.getString("wn"+i));
			copyToWorldNameMap();
		}

		@Override
		public void writeToNBT(NBTTagCompound nbt)
		{
			int i = 0;
			for(Entry<Integer, String> ent : nameMap.entrySet())
			{
				nbt.setInteger("wi"+i, ent.getKey());
				nbt.setString("wn"+i, ent.getValue());
				i++;
			}
		}

	}
}
