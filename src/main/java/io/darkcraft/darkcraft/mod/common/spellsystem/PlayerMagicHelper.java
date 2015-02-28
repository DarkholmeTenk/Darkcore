package io.darkcraft.darkcraft.mod.common.spellsystem;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class PlayerMagicHelper
{
	public static boolean canCast(EntityPlayer pl, double cost)
	{
		//TODO: Write this function
		return true;
	}
	
	public static MovingObjectPosition rayTrace(EntityLivingBase ent,double maxDist, float partialTickTime)
    {
		if(ent == null)
			return null;
        Vec3 vec3 = ent.getPosition(partialTickTime);
        Vec3 vec31 = ent.getLook(partialTickTime);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * maxDist, vec31.yCoord * maxDist, vec31.zCoord * maxDist);
        return ent.worldObj.func_147447_a(vec3, vec32, false, false, true);
    }
	
	public static SimpleDoubleCoordStore getAimingAt(EntityLivingBase ent, double maxDist)
	{
		if(ent == null)
			return null;
		MovingObjectPosition mop = rayTrace(ent,maxDist,0.1f);
		if(mop.entityHit != null)
		{
			if(mop.entityHit instanceof EntityLivingBase)
				return new SimpleDoubleCoordStore((EntityLivingBase)mop.entityHit);
		}
		Vec3 hit = mop.hitVec;
		if(hit != null)
			return new SimpleDoubleCoordStore(ent.worldObj,hit.xCoord,hit.yCoord,hit.zCoord);
		return null;
	}
}
