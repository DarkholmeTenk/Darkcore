package io.darkcraft.darkcore.mod;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DarkcoreTeleporter extends Teleporter
{
	public static DarkcoreTeleporter	i;

	public DarkcoreTeleporter(WorldServer w)
	{
		super(w);
		i = this;
	}

	@Override
	public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
	{
		int i = MathHelper.floor_double(par1Entity.posX);
		int j = MathHelper.floor_double(par1Entity.posY) - 1;
		int k = MathHelper.floor_double(par1Entity.posZ);

		par1Entity.setLocationAndAngles(i, j, k, par1Entity.rotationYaw, 0.0F);
		par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
	}

	/**
	 * Place an entity in a nearby portal which already exists.
	 */
	@Override
	public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
	{
		return false;
	}

	@Override
	public boolean makePortal(Entity par1Entity)
	{
		return false;
	}

	@Override
	public void removeStalePortalLocations(long p_85189_1_)
	{

	}

}
