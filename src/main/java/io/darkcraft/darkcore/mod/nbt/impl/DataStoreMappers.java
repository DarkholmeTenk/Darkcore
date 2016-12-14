package io.darkcraft.darkcore.mod.nbt.impl;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.handlers.containers.EntityContainer;
import io.darkcraft.darkcore.mod.handlers.containers.EntityContainerHandler;
import io.darkcraft.darkcore.mod.handlers.containers.EntityLivingBaseContainer;
import io.darkcraft.darkcore.mod.handlers.containers.IEntityContainer;
import io.darkcraft.darkcore.mod.handlers.containers.PlayerContainer;
import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;

public class DataStoreMappers
{
	public static void register()
	{
		NBTHelper.register(PlayerContainer.class, new PlayerContainerMapper());
		NBTHelper.register(EntityContainer.class, new EntityContainerMapper());
		NBTHelper.register(EntityLivingBaseContainer.class, new EntityLBContainerMapper());
	}

	public static class PlayerContainerMapper extends Mapper<PlayerContainer>
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, Object o)
		{
			if(o == null) return;
			PlayerContainer pc = (PlayerContainer) o;
			BasicMappers.uuidMapper.writeToNBT(nbt, "uuid", pc.getUUID());
		}

		@Override
		public PlayerContainer fillFromNBT(NBTTagCompound nbt, Object t)
		{
			UUID uuid = BasicMappers.uuidMapper.readFromNBT(nbt, "uuid");
			if((t == null) || !((PlayerContainer)t).getUUID().equals(uuid))
				return createFromNBT(nbt);
			return (PlayerContainer) t;
		}

		@Override
		public PlayerContainer createFromNBT(NBTTagCompound nbt, Object... arguments)
		{
			UUID uuid = BasicMappers.uuidMapper.readFromNBT(nbt, "uuid");
			return EntityContainerHandler.getContainer(uuid);
		}
	}

	public static class EntityContainerMapper extends PrimMapper<EntityContainer>
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t)
		{
			((EntityContainer)t).writeToNBT(nbt, id);
		}

		@Override
		public EntityContainer readFromNBT(NBTTagCompound nbt, String id)
		{
			IEntityContainer cont = EntityContainer.readFromNBT(nbt.getCompoundTag(id));
			if(cont instanceof EntityContainer)
				return (EntityContainer) cont;
			return null;
		}
	}

	public static class EntityLBContainerMapper extends PrimMapper<EntityLivingBaseContainer>
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Object t)
		{
			((EntityLivingBaseContainer)t).writeToNBT(nbt, id);
		}

		@Override
		public EntityLivingBaseContainer readFromNBT(NBTTagCompound nbt, String id)
		{
			IEntityContainer cont = EntityContainer.readFromNBT(nbt.getCompoundTag(id));
			if(cont instanceof EntityLivingBaseContainer)
				return (EntityLivingBaseContainer) cont;
			return null;
		}
	}
}
