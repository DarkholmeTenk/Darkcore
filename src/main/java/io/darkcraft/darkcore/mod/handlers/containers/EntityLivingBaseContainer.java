package io.darkcraft.darkcore.mod.handlers.containers;

import net.minecraft.entity.EntityLivingBase;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

public class EntityLivingBaseContainer extends EntityContainer<EntityLivingBase>
{

	public EntityLivingBaseContainer(EntityLivingBase e)
	{
		super(e);
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
