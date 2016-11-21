package io.darkcraft.darkcore.mod.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.darkcraft.darkcore.mod.abstracts.AbstractWorldDataStore;
import io.darkcraft.darkcore.mod.handlers.containers.EntityContainerHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;

public class PlayerHelper
{
	public static EntityPlayerMP getCurrentPlayer(EntityPlayerMP pl)
	{
		if(!pl.isDead) return pl;
		EntityPlayerMP np = getPlayer(getUsername(pl));
		if((np == null) || (np.playerNetServerHandler == null) || (np.playerNetServerHandler.netManager == null)) return null;
		return np;
	}

	public static EntityPlayerMP getPlayer(String username)
	{
		return ServerHelper.getPlayer(username);
	}

	public static String getUsername(EntityPlayer player)
	{
		return ServerHelper.getUsername(player);
	}

	public static String getUsername(UUID uuid)
	{
		return store.getUsername(uuid);
	}

	public static boolean validForNetwork(EntityPlayerMP pl)
	{
		if(pl.isDead) return false;
		try
		{
			pl.playerNetServerHandler.netManager.channel().attr(NetworkDispatcher.FML_DISPATCHER).get();
		}
		catch(NullPointerException e)
		{
			return false;
		}
		return true;
	}

	public static PlayerHelper		i			= new PlayerHelper();
	private static UUIDStore		store		= null;
	private static Set<ItemStack>	joinStacks	= new HashSet<ItemStack>();

	private static UUIDStore getStore()
	{
		if (store == null)
		{
			store = new UUIDStore();
			store.load();
		}
		return store;
	}

	public static UUID getUUID(EntityPlayer pl)
	{
		GameProfile profile = pl.getGameProfile();
		if(profile != null)
			return profile.getId();
		else
		{
			NBTTagCompound plNbt = pl.getEntityData();
			if(plNbt == null) return null;
			long least = plNbt.getLong("UUIDLeast");
			long most = plNbt.getLong("UUIDMost");
			if((least != 0) && (most != 0))
				return new UUID(most,least);
		}
		return null;
	}

	public static UUID getUUID(String un)
	{
		return getStore().getUUID(un);
	}

	/**
	 * @param oldName
	 *            the name which we think might be the old name of the player
	 * @param pl
	 *            the player who we are checking
	 * @return true if pl is the same person as the person who had the username oldName; false otherwise
	 */
	public static boolean isEqual(String oldName, EntityPlayer pl)
	{
		if (ServerHelper.getUsername(pl).equals(oldName))
		{
			getStore().addUser(pl);
			return true;
		}
		String storedName = getStore().getUsername(pl);
		if (oldName.equals(storedName))
		{
			getStore().addUser(pl);
			return true;
		}
		return false;
	}

	/**
	 * @param pl
	 *            the player to check
	 * @return true if the players name has changed from the datastore
	 * @return false if the players name has not changed or was not in the datastore
	 */
	public static boolean nameChanged(EntityPlayer pl)
	{
		String newName = ServerHelper.getUsername(pl);
		String storedName = getStore().getUsername(pl);
		if (storedName == null)
		{
			getStore().addUser(pl);
			return false;
		}
		if (newName.equals(storedName)) return false;
		return true;
	}

	public static void registerJoinItem(ItemStack is)
	{
		joinStacks.add(is);
	}

	public static String[] getAllUsernames()
	{
		return ServerHelper.getServer().getAllUsernames();
	}

	public static List<EntityPlayer> getAllPlayers()
	{
		if(ServerHelper.isClient()) return null;
		return ServerHelper.getConfigManager().playerEntityList;
	}

	public static Team getTeam(EntityLivingBase ent)
	{
		return ent.getTeam();
	}

	public static Scoreboard getScoreboard(int dim)
	{
		World w = WorldHelper.getWorld(dim);
		if(w == null) return null;
		return w.getScoreboard();
	}

	public static Team getTeam(int dim,String id)
	{
		Scoreboard sb = getScoreboard(dim);
		if(sb == null) return null;
		return sb.getTeam(id);
	}

	public static boolean swordsEnabled()
	{
		return getStore().swordsEnabled;
	}

	public static void toggleSwords()
	{
		UUIDStore st = getStore();
		st.swordsEnabled = !st.swordsEnabled;
	}

	/*
	 *
	 * UUID Storage system for detecting player name changes
	 * @author dark
	 *
	 */
	public static class UUIDStore extends AbstractWorldDataStore
	{
		private HashMap<UUID, String>	uuidMap	= new HashMap<UUID, String>();
		public boolean swordsEnabled = true;

		public UUIDStore()
		{
			super("dc_uuidstore");
		}

		public UUIDStore(String s)
		{
			this();
		}

		@Override
		public int getDimension()
		{
			return 0;
		}

		public void addUser(EntityPlayer ent)
		{
			try
			{
				UUID uuid = ent.getGameProfile().getId();
				EntityContainerHandler.getContainer(ent);
				String name = ServerHelper.getUsername(ent);
				String prev = null;
				synchronized (uuidMap)
				{
					prev = uuidMap.put(uuid, name);
				}
				if (!name.equals(prev))
				{
					markDirty();
					save();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		public String getUsername(EntityPlayer ent)
		{
			UUID uuid = PlayerHelper.getUUID(ent);
			return getUsername(uuid);
		}

		public String getUsername(UUID uuid)
		{
			synchronized (uuidMap)
			{
				return uuidMap.get(uuid);
			}
		}

		public UUID getUUID(String un)
		{
			if (un == null) return null;
			synchronized (uuidMap)
			{
				for (UUID uuid : uuidMap.keySet())
					if (un.equals(uuidMap.get(uuid))) return uuid;
			}
			return null;
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt)
		{
			synchronized (uuidMap)
			{
				uuidMap.clear();
				int i = 0;
				while (nbt.hasKey("uuid" + i))
				{
					try
					{
						String key = nbt.getString("uuid" + (i++));
						String[] splitKey = key.split("\\|", 2);
						UUID uuid = UUID.fromString(splitKey[0]);
						String name = splitKey[1];
						uuidMap.put(uuid, name);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			swordsEnabled = nbt.hasKey("swen") ? nbt.getBoolean("swen") : true;
		}

		@Override
		public void writeToNBT(NBTTagCompound nbt)
		{
			synchronized (uuidMap)
			{
				int i = 0;
				for (UUID uuid : uuidMap.keySet())
				{
					nbt.setString("uuid" + (i++), uuid.toString() + "|" + uuidMap.get(uuid));
				}
			}
			nbt.setBoolean("swen", swordsEnabled);
		}

	}

	@ForgeSubscribe
	public void playerSpawn(PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
		if(!getStore().uuidMap.containsKey(getUUID(player)))
			for(ItemStack is : joinStacks)
				WorldHelper.giveItemStack(player, is.copy());
		getStore().addUser(player);
	}

	public static void reset()
	{
		store = null;
	}
}
