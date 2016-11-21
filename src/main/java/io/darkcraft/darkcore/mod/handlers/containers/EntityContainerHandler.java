package io.darkcraft.darkcore.mod.handlers.containers;

import java.util.UUID;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.darkcraft.darkcore.mod.helpers.NBTHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class EntityContainerHandler
{
	public static IEntityContainer getContainer(EntityLivingBase ent)
	{
		if((ent instanceof EntityPlayer) && !(ent instanceof FakePlayer))
		{
			UUID uuid = PlayerHelper.getUUID((EntityPlayer)ent);
			PlayerContainer pc;
			if(uuid != null)
			{
				pc = getCache(ent.worldObj.isRemote).getUnchecked(uuid);
				pc.setEntity((EntityPlayer) ent);
				return pc;
			}
			else
				return null;
		}
		else
		{
			return new EntityLivingBaseContainer(ent);
		}
	}

	public static IEntityContainer getContainer(NBTTagCompound nbt, String id)
	{
		NBTTagCompound snbt = nbt.getCompoundTag(id);
		if(snbt.hasNoTags()) return null;
		byte t = snbt.getByte("t");
		if(t == 1)
		{
			PlayerContainer pc = getCache(ServerHelper.isServer()).getUnchecked(NBTHelper.readUUIDFromNBT(snbt, "uuid"));
			return pc;
		}
		else
			return EntityLivingBaseContainer.readFromNBT(snbt);
	}

	public static PlayerContainer getContainer(UUID uuid)
	{
		return getCache(ServerHelper.isServer()).getUnchecked(uuid);
	}


	private static LoadingCache<UUID,PlayerContainer> pcCacheSer;
	private static LoadingCache<UUID,PlayerContainer> pcCacheCli;
	private static LoadingCache<UUID,PlayerContainer> getCache(boolean serverSide)
	{
		if(serverSide)
			return pcCacheSer;
		else
			return pcCacheCli;
	}


	static
	{
		CacheLoader<UUID,PlayerContainer> loader = new CacheLoader<UUID,PlayerContainer>(){
			@Override
			public PlayerContainer load(UUID key) throws Exception
			{
				return new PlayerContainer(key);
			}
		};
		pcCacheSer = CacheBuilder.newBuilder().build(loader);
		pcCacheCli = CacheBuilder.newBuilder().build(loader);
	}

	public static void clear()
	{
		pcCacheSer.invalidateAll();
		pcCacheCli.invalidateAll();
	}
}
