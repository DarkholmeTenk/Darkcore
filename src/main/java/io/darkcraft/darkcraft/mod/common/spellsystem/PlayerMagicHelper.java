package io.darkcraft.darkcraft.mod.common.spellsystem;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class PlayerMagicHelper
{
	public static boolean canCast(EntityPlayer pl, double cost)
	{
		// TODO: Write this function
		return true;
	}

	private static double distanceFromLine(Vec3 source, Vec3 dest, Vec3 point, double denominator)
	{
		Vec3 part1 = source.subtract(point);
		Vec3 part2 = dest.subtract(point);
		return (part2.crossProduct(part1).lengthVector()) / denominator;
	}

	private static EntityLivingBase raytraceEnts(EntityLivingBase source, Vec3 dest)
	{
		if (dest == null)
			return null;
		double yO = ServerHelper.isServer() ? source.getEyeHeight() : 0;
		double x = Math.min(source.posX, dest.xCoord);
		double X = Math.max(source.posX, dest.xCoord);
		double y = Math.min(source.posY + yO, dest.yCoord);
		double Y = Math.max(source.posY + yO, dest.yCoord);
		double z = Math.min(source.posZ, dest.zCoord);
		double Z = Math.max(source.posZ, dest.zCoord);
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, X, Y, Z);
		List ents = source.worldObj.getEntitiesWithinAABBExcludingEntity(source, box);
		Vec3 sourceVec = Vec3.createVectorHelper(source.posX, source.posY + yO, source.posZ);
		double denom = sourceVec.subtract(dest).lengthVector(); // |hitVec-sourceVec|
		double bestDistanceToSource = Integer.MAX_VALUE;
		EntityLivingBase closest = null;
		for (Object o : ents)
		{
			if (o instanceof EntityLivingBase)
			{
				EntityLivingBase ent = (EntityLivingBase) o;
				Vec3 pos = Vec3.createVectorHelper(ent.posX, ent.posY + yO, ent.posZ);
				double distance = distanceFromLine(sourceVec, dest, pos, denom);
				if (distance < 1)
				{
					double distanceToSource = sourceVec.distanceTo(pos);
					if (distanceToSource < bestDistanceToSource)
					{
						bestDistanceToSource = distanceToSource;
						closest = ent;
					}
				}
			}
		}
		return closest;
	}

	public static MovingObjectPosition rayTrace(EntityLivingBase ent, double maxDist)
	{
		if (ent == null)
			return null;
		float partialTickTime = 1;
		Vec3 vec3 = ent.getPosition(partialTickTime);
		if (ServerHelper.isServer())
			vec3.yCoord += ent.getEyeHeight();
		Vec3 vec31 = ent.getLook(partialTickTime);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * maxDist, vec31.yCoord * maxDist, vec31.zCoord * maxDist);
		MovingObjectPosition mop = ent.worldObj.func_147447_a(vec3, vec32, true, true, true);
		if (mop != null && mop.entityHit == null)
			mop.entityHit = raytraceEnts(ent, mop.hitVec);
		if (mop == null)
		{
			EntityLivingBase hit = raytraceEnts(ent,
					Vec3.createVectorHelper(vec31.xCoord * maxDist, vec31.yCoord * maxDist, vec31.zCoord * maxDist));
			if(hit != null)
				return new MovingObjectPosition(hit);
		}
		return mop;
	}

	public static SimpleDoubleCoordStore getAimingAt(EntityLivingBase ent, double maxDist)
	{
		if (ent == null)
			return null;
		MovingObjectPosition mop = rayTrace(ent, maxDist);
		if (mop != null)
		{
			if (mop.entityHit != null)
			{
				if (mop.entityHit instanceof EntityLivingBase && mop.entityHit != ent)
					return new SimpleDoubleCoordStore((EntityLivingBase) mop.entityHit);
			}
			Vec3 hit = mop.hitVec;
			if (hit != null)
				return new SimpleDoubleCoordStore(ent.worldObj, hit.xCoord, hit.yCoord, hit.zCoord);
		}
		return null;
	}
}
