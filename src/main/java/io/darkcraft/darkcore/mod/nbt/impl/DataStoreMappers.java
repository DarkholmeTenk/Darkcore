package io.darkcraft.darkcore.mod.nbt.impl;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

import io.darkcraft.darkcore.mod.handlers.containers.EntityContainer;
import io.darkcraft.darkcore.mod.handlers.containers.EntityContainerHandler;
import io.darkcraft.darkcore.mod.handlers.containers.EntityLivingBaseContainer;
import io.darkcraft.darkcore.mod.handlers.containers.IEntityContainer;
import io.darkcraft.darkcore.mod.handlers.containers.PlayerContainer;
import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;

public class DataStoreMappers
{
	public static void register()
	{
		NBTHelper.register(PlayerContainer.class, new PlayerContainerMapper());
		NBTHelper.register(EntityContainer.class, new EntityContainerMapper());
		NBTHelper.register(EntityLivingBaseContainer.class, new EntityLBContainerMapper());
		NBTHelper.register(Entity.class, new EntityMapper());
		NBTHelper.register(Vec3.class, new Vec3Mapper());
	}

	public static class PlayerContainerMapper extends Mapper<PlayerContainer>
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, PlayerContainer o)
		{
			if(o == null) return;
			PlayerContainer pc = o;
			BasicMappers.uuidMapper.writeToNBT(nbt, "uuid", pc.getUUID());
		}

		@Override
		public PlayerContainer fillFromNBT(NBTTagCompound nbt, PlayerContainer t)
		{
			UUID uuid = BasicMappers.uuidMapper.readFromNBT(nbt, "uuid");
			if((t == null) || !t.getUUID().equals(uuid))
				return createFromNBT(nbt);
			return t;
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
		public void writeToNBT(NBTTagCompound nbt, String id, EntityContainer t)
		{
			t.writeToNBT(nbt, id);
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
		public void writeToNBT(NBTTagCompound nbt, String id, EntityLivingBaseContainer t)
		{
			t.writeToNBT(nbt, id);
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

	public static class EntityMapper extends PrimMapper<Entity>
	{
		private final Mapper<IEntityContainer> mapper = NBTHelper.getMapper(IEntityContainer.class, SerialisableType.TRANSMIT);

		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Entity t)
		{
			IEntityContainer e = EntityContainerHandler.getContainer(t);
			mapper.writeToNBT(nbt, id, e);
		}

		@Override
		public Entity readFromNBT(NBTTagCompound nbt, String id)
		{
			IEntityContainer e = mapper.readFromNBT(nbt, id);
			if(e != null)
				return e.getEntity();
			return null;
		}
	}

	public static class Vec3Mapper extends PrimMapper<Vec3>
	{
		@Override
		public void writeToNBT(NBTTagCompound nbt, String id, Vec3 t)
		{
			NBTTagCompound snbt = new NBTTagCompound();
			snbt.setDouble("x", t.xCoord);
			snbt.setDouble("y", t.yCoord);
			snbt.setDouble("z", t.zCoord);
			nbt.setTag(id,snbt);
		}

		@Override
		public Vec3 readFromNBT(NBTTagCompound nbt, String id)
		{
			NBTTagCompound snbt = nbt.getCompoundTag(id);
			double x = snbt.getDouble("x");
			double y = snbt.getDouble("y");
			double z = snbt.getDouble("z");
			return Vec3.createVectorHelper(x, y, z);
		}

	}
}
