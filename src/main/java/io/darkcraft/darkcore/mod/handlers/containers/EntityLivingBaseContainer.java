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
			return new SimpleDoubleCoordStore(ent).translate(0, ent.height/2, 0);
		}
		return null;
	}
}
