package io.darkcraft.darkcore.mod.handlers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IChunkLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.event.world.WorldEvent;

import com.google.common.collect.ImmutableSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;

public class ChunkLoadingHandler implements LoadingCallback
{
	HashSet<Ticket>						tickets					= new HashSet<Ticket>();
	HashSet<SimpleCoordStore>			waitingToLoad			= new HashSet<SimpleCoordStore>();
	HashMap<SimpleCoordStore, Ticket>	monitorableChunkLoaders	= new HashMap<SimpleCoordStore, Ticket>();
	private int							tickCount				= 0;
	private boolean						forceCheck				= false;
	private boolean						ticked					= false;

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for (Ticket t : tickets)
		{
			if (t.getModData().hasKey("coords"))
			{
				SimpleCoordStore pos = SimpleCoordStore.readFromNBT(t.getModData().getCompoundTag("coords"));
				if (!monitorableChunkLoaders.containsKey(pos))
					waitingToLoad.add(pos);
			}
			release(t,true);
		}
	}

	public void loadMe(IChunkLoader chunkLoader)
	{
		SimpleCoordStore scs = chunkLoader.coords();
		if (!monitorableChunkLoaders.containsKey(scs))
		{
			World w = scs.getWorldObj();
			monitorableChunkLoaders.put(chunkLoader.coords(), getTicket(chunkLoader, w));
		}
	}

	private void loadLoadables(Ticket t, IChunkLoader te, SimpleCoordStore pos)
	{
		if ((t == null) || (te == null)) return;
		if (t.world == null) return;
		ImmutableSet<ChunkCoordIntPair> alreadyLoaded = t.getChunkList();
		ChunkCoordIntPair[] loadable = te.loadable();
		if (loadable != null)
		{
			for (ChunkCoordIntPair load : loadable)
			{
				if ((alreadyLoaded == null) || !alreadyLoaded.contains(load))
				{
					try
					{
						ForgeChunkManager.forceChunk(t, load);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			ChunkCoordIntPair tePos = pos.toChunkCoords();
			if ((alreadyLoaded == null) || !alreadyLoaded.contains(tePos))
			{
				try
				{
					ForgeChunkManager.forceChunk(t, tePos);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		NBTTagCompound nbt = t.getModData();
		if (nbt != null)
		{
			SimpleCoordStore coords = te.coords();
			if (coords != null) nbt.setTag("coords", pos.writeToNBT());
		}
	}

	private Ticket getTicket(IChunkLoader te, World world)
	{
		Ticket t = ForgeChunkManager.requestTicket(DarkcoreMod.i, world, ForgeChunkManager.Type.NORMAL);
		tickets.add(t);
		return t;
	}

	private Ticket getTicket(SimpleCoordStore pos)
	{
		TileEntity te = pos.getTileEntity();
		if(te instanceof IChunkLoader)
			return getTicket((IChunkLoader)te,pos.getWorldObj());
		return null;
	}

	private void release(Ticket t, boolean clear)
	{
		try
		{
			ForgeChunkManager.releaseTicket(t);
			if(clear && tickets.contains(t))
				tickets.remove(t);
		}
		catch(NullPointerException npe){};
	}

	private void removeBadTickets()
	{
		Iterator<Ticket> iter = tickets.iterator();
		while(iter.hasNext())
		{
			Ticket t = iter.next();
			if(!monitorableChunkLoaders.containsValue(t))
			{
				release(t, false);
				iter.remove();
			}
		}
	}

	private void validateChunkLoaders()
	{
		if (!ticked) return;
		Iterator<SimpleCoordStore> keyIter = monitorableChunkLoaders.keySet().iterator();
		while (keyIter.hasNext())
		{
			SimpleCoordStore pos = keyIter.next();
			Ticket t = monitorableChunkLoaders.get(pos);
			if(t == null)
			{
				if(DarkcoreMod.reloadNullTicket)
				{
					t = getTicket(pos);
					if(t != null)
						monitorableChunkLoaders.put(pos, t);
				}

				if(t == null)
				{
					keyIter.remove();
					continue;
				}
			}
			TileEntity te = pos.getTileEntity();
			World w = pos.getWorldObj();
			if ((te != null) && (te instanceof IChunkLoader))
			{
				if(te instanceof AbstractTileEntity)
					if(!((AbstractTileEntity)te).init)
					{
						loadLoadables(t, (IChunkLoader) te, pos);
						continue;
					}
				IChunkLoader cl = (IChunkLoader) te;
				if (!cl.shouldChunkload())
				{
					release(t,true);
					keyIter.remove();
					continue;
				}
				else
					loadLoadables(t, (IChunkLoader) te, pos);
			}
			else
			{
				if(t != null)
					release(t,true);
				keyIter.remove();
				continue;
			}
		}
		for(Iterator<SimpleCoordStore> iter = waitingToLoad.iterator(); iter.hasNext();)
		{
			SimpleCoordStore pos = iter.next();
			if(monitorableChunkLoaders.containsKey(pos))
			{
				iter.remove();
				continue;
			}
			TileEntity te = pos.getTileEntity();
			if(te instanceof IChunkLoader)
			{
				Ticket t = getTicket((IChunkLoader)te, pos.getWorldObj());
				loadLoadables(t, (IChunkLoader)te, pos);
				monitorableChunkLoaders.put(pos, t);
			}
			iter.remove();
		}
		removeBadTickets();
	}

	@SubscribeEvent
	public void handleTick(ServerTickEvent event)
	{
		// TardisOutput.print("TCLM", "Server tick");
		if (event.side.equals(Side.SERVER) && event.phase.equals(TickEvent.Phase.END)) tickEnd();
	}

	private void tickEnd()
	{
		if (((tickCount++ % DarkcoreMod.chunkLoadCheckTime) == 1) || forceCheck)
		{
			// TardisOutput.print("TCLM", "Handling chunks");
			forceCheck = false;
			validateChunkLoaders();
		}
		ticked = true;
	}

	@SubscribeEvent
	public void handleWorldUnload(WorldEvent.Unload unloadEvent)
	{
		if(ServerHelper.isClient()) return;
		World w = unloadEvent.world;
		int id = WorldHelper.getWorldID(w);
		for(SimpleCoordStore pos : monitorableChunkLoaders.keySet())
		{
			if(pos.world != id) continue;
			Ticket t = monitorableChunkLoaders.get(pos);
			if(t == null) continue;
			release(t,true);
			monitorableChunkLoaders.put(pos, null);
		}
		removeBadTickets();
	}

	public Set<SimpleCoordStore> getLoadables()
	{
		return monitorableChunkLoaders.keySet();
	}

	public void clear()
	{
		monitorableChunkLoaders.clear();
	}

}
