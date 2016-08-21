package io.darkcraft.darkcore.mod.handlers.containers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Contains a reference to an entity and a method of writing that entity to NBT.
 *
 * Methods to get IEntityContainers can be found in {@link EntityContainerHandler}
 * @author dark
 *
 */
public interface IEntityContainer<T extends EntityLivingBase>
{
	/**
	 * @return the entity contained within this container.<br>May return null, if the entity no longer exists or the player is offline, etc.
	 */
	public T getEntity();

	public boolean equals(T o);

	public void writeToNBT(NBTTagCompound nbt, String tag);
}
