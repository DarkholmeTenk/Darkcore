package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.config.CType;
import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.config.ConfigItem;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IChunkLoader;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockCore;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class AbstractTileEntity extends TileEntity
{
	public boolean				init				= false;
	public int					tt					= 0;
	private static int			updateInterval		= 0;
	private static int			updateCounterMax	= 0;
	private static int			multiBlockInterval	= 80;
	private volatile int		updateCounter		= 0;
	private volatile int		lastUpdateTT		= 0;
	private volatile boolean	updateQueued		= false;
	public static Random		rand				= new Random();
	public SimpleCoordStore		coords				= null;

	private boolean				pscl				= false;

	static
	{
		refreshConfigs();
	}

	public static void refreshConfigs()
	{
		ConfigFile cf = DarkcoreMod.configHandler.registerConfigNeeder("network");
		updateInterval = cf.getConfigItem(new ConfigItem("update interval", CType.INT, 5, "The minimum number of ticks between a single tile entity sending updates", "Default: 5")).getInt();
		updateCounterMax = cf.getConfigItem(new ConfigItem("update counter max", CType.INT, 20, "The maximum number of times a single tile entity can send updates in a short time period", "Default: 20")).getInt();
		multiBlockInterval = cf.getConfigItem(new ConfigItem("multi block interval", CType.INT, 80, "How often a multi block core checks for a valid multiblock structure", "Default: 20")).getInt();
	}

	protected boolean softBlock(World w, int x, int y, int z)
	{
		return WorldHelper.softBlock(w, x, y, z);
	}

	protected boolean softBlock(SimpleDoubleCoordStore pos)
	{
		return WorldHelper.softBlock(pos);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeTransmittable(tag);
		writeTransmittableOnly(tag);
		Packet p = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, tag);
		return p;
	}

	public void updateNeighbours()
	{
		Block b = worldObj.getBlock(xCoord, yCoord, zCoord);
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			Block d = worldObj.getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if (d != null) d.onNeighborBlockChange(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, b);
		}
	}

	private boolean canSendUpdate()
	{
		return ((lastUpdateTT + updateInterval) <= tt) && (updateCounter < updateCounterMax);
	}

	public void sendUpdate()
	{
		if (ServerHelper.isClient()) return;
		if (worldObj == null)
		{
			updateQueued = true;
			return;
		}
		if ((worldObj.playerEntities == null) || (worldObj.playerEntities.size() == 0)) return;
		if (canSendUpdate())
		{
			if (DarkcoreMod.debugText) System.out.println("[ATE]Sending update " + getClass().getSimpleName());
			updateQueued = false;
			updateCounter++;
			lastUpdateTT = tt;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		else
		{
			if (DarkcoreMod.debugText) System.out.println("[ATE]Update sending blocked to prevent spam " + getClass().getSimpleName());
			updateQueued = true;
		}
	}

	public void queueUpdate()
	{
		updateQueued = true;
	}

	public void init()
	{
	}

	@Override
	public void updateEntity()
	{
		if (coords == null) coords = new SimpleCoordStore(this);
		tt++;

		if (((tt % 11) == 0) && (updateCounter > 0)) updateCounter--;

		if (updateQueued && canSendUpdate()) sendUpdate();

		if (ServerHelper.isServer() && ((tt % multiBlockInterval) == 0) && (this instanceof IMultiBlockCore))
		{
			if (((IMultiBlockCore) this).keepRechecking()) ((IMultiBlockCore) this).recheckValidity();
		}

		if (ServerHelper.isServer() && (this instanceof IChunkLoader))
		{
			IChunkLoader icl = (IChunkLoader) this;
			if(icl.shouldChunkload())
				DarkcoreMod.chunkLoadingHandler.loadMe(icl);
		}

		if (!init)
		{
			init = true;
			init();
			queueUpdate();
		}
		tick();
	}

	public void tick()
	{

	}

	public SimpleCoordStore coords()
	{
		if (coords == null) coords = new SimpleCoordStore(this);
		return coords;
	}

	public void sendDataPacket()
	{
		if (ServerHelper.isServer())
		{
			if (DarkcoreMod.debugText) System.out.println("[ATE]Called sendDataPacket");
			Packet p = getDescriptionPacket();
			MinecraftServer serv = MinecraftServer.getServer();
			if (serv == null) return;
			ServerConfigurationManager conf = serv.getConfigurationManager();
			if (conf == null) return;
			conf.sendToAllNear(xCoord, yCoord, zCoord, 160, worldObj.provider.dimensionId, p);
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound nbt = packet.func_148857_g();
		readTransmittable(nbt);
		readTransmittableOnly(nbt);
		super.onDataPacket(net, packet);
	}

	public void writeTransmittable(NBTTagCompound nbt)
	{
	}

	public void readTransmittable(NBTTagCompound nbt)
	{
	}

	public void writeTransmittableOnly(NBTTagCompound nbt)
	{
	}

	public void readTransmittableOnly(NBTTagCompound nbt)
	{
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey("placed")) super.readFromNBT(nbt);
		readTransmittable(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		writeTransmittable(nbt);
	}
}
