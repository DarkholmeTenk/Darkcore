package io.darkcraft.darkcore.mod.handlers.containers;

import java.lang.ref.WeakReference;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.NBTHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;

@NBTSerialisable
public final class PlayerContainer implements IEntityContainer<EntityPlayer>
{
	public final boolean serverSide;
	private UUID uuid;
	private WeakReference<EntityPlayer> currentPl;

	protected PlayerContainer(UUID _uuid)
	{
		uuid = _uuid;
		serverSide = ServerHelper.isServer();
	}

	protected PlayerContainer(EntityPlayer pl)
	{
		this(PlayerHelper.getUUID(pl));
		currentPl = new WeakReference(pl);
	}

	@Override
	public EntityPlayer getEntity()
	{
		if(currentPl == null) return null;
		EntityPlayer pl = currentPl.get();
		if(pl == null) return null;
		if(pl.isDead && (pl instanceof EntityPlayerMP))
			pl = PlayerHelper.getCurrentPlayer((EntityPlayerMP) pl);
		return pl;
	}

	public UUID getUUID()
	{
		if(uuid != null) return uuid;
		EntityPlayer pl = getEntity();
		uuid = PlayerHelper.getUUID(pl);
		return uuid;
	}

	public String getUsername()
	{
		return PlayerHelper.getUsername(uuid);
	}

	protected void setEntity(EntityPlayer pl)
	{
		if(!equals(pl)) throw new RuntimeException("Setting wrong player");
		currentPl = new WeakReference(pl);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String tag)
	{
		NBTTagCompound snbt = new NBTTagCompound();
		snbt.setByte("t", (byte)1);
		NBTHelper.writeUUIDToNBT(uuid, snbt, "uuid");
		nbt.setTag(tag, snbt);
	}

	@Override
	public boolean equals(EntityPlayer o)
	{
		if(o == null) return false;
		UUID newID = PlayerHelper.getUUID(o);
		return uuid.equals(newID);
	}

	private int lastAge = -1;
	private SimpleDoubleCoordStore lastPosition = null;
	@Override
	public SimpleDoubleCoordStore getPosition()
	{
		EntityLivingBase ent = getEntity();
		if(ent != null)
		{
			if(ent.getAge() != lastAge)
			{
				lastAge = ent.getAge();
				return lastPosition = new SimpleDoubleCoordStore(ent);
			}
			return lastPosition;
		}
		return null;
	}
}
