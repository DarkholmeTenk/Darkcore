package io.darkcraft.darkcore.mod.helpers;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RaytraceHelper
{
	private static final double defaultExpansion = 0.2;

	public static MovingObjectPosition rayIntersectEntity(Entity ent, Vec3 origin, Vec3 end)
	{
		return rayIntersectEntity(ent, origin, end, defaultExpansion);
	}

	public static MovingObjectPosition rayIntersectEntity(Entity ent, Vec3 origin, Vec3 end, double expansion)
	{
		AxisAlignedBB aabb = ent.boundingBox.expand(expansion, expansion, expansion);
		//aabb = aabb.addCoord(ent.posX, ent.posY, ent.posZ);
		return aabb.calculateIntercept(origin, end);
	}

	public static AxisAlignedBB getAABB(Vec3 s, Vec3 e)
	{
		double mx = Math.min(s.xCoord, e.xCoord);
		double my = Math.min(s.yCoord, e.yCoord);
		double mz = Math.min(s.zCoord, e.zCoord);
		double mX = Math.max(s.xCoord, e.xCoord);
		double mY = Math.max(s.yCoord, e.yCoord);
		double mZ = Math.max(s.zCoord, e.zCoord);
		return AxisAlignedBB.getBoundingBox(mx,my,mz,mX,mY,mZ);
	}

	public static MovingObjectPosition rayTrace(Entity tracer, Vec3 end, boolean liquids, Class<? extends Entity> entClass)
	{
		Vec3 start = Vec3.createVectorHelper(tracer.posX, tracer.posY, tracer.posZ);
		if(ServerHelper.isServer() && (tracer instanceof EntityPlayer))
			start = Vec3.createVectorHelper(tracer.posX, tracer.posY + tracer.getEyeHeight(), tracer.posZ);
		World w = tracer.worldObj;
		if((tracer == null) || (w == null)) return null;
		MovingObjectPosition mop = w.rayTraceBlocks(start, end, liquids);
		double dist = Double.MAX_VALUE;
		if(mop != null)
			dist = start.distanceTo(mop.hitVec);
		List entityList = w.getEntitiesWithinAABBExcludingEntity(tracer, getAABB(start, end).expand(0.1, 0.1, 0.1));
		for(Object o : entityList)
		{
			if(!(o instanceof Entity)) continue;
			Entity e = (Entity) o;
			if(!e.canBeCollidedWith()) continue;
			if((entClass != null) && !entClass.isInstance(e)) continue;
			MovingObjectPosition entityMop = rayIntersectEntity(e, start, end);
			if(entityMop == null)
			{
				continue;
			};
			entityMop.entityHit = e;
			entityMop.typeOfHit = MovingObjectType.ENTITY;
			double entDist = start.distanceTo(entityMop.hitVec);
			if(entDist < dist)
			{
				mop = entityMop;
				dist = entDist;
			}
		}
		return mop;
	}

	public static MovingObjectPosition rayTrace(Entity tracer, double dist, boolean liquids, Class<? extends Entity> entClass)
	{
		Vec3 end = tracer.getLookVec();
		if(dist != 1)
			end = Vec3.createVectorHelper(end.xCoord*dist, end.yCoord*dist, end.zCoord*dist);
		return rayTrace(tracer,end,liquids, entClass);
	}

	public static MovingObjectPosition rayTrace(Entity tracer, boolean liquids, Class<? extends Entity> entClass)
	{
		Vec3 end = Vec3.createVectorHelper(tracer.posX+tracer.motionX, tracer.posY+tracer.motionY, tracer.posZ+tracer.motionZ);
		return rayTrace(tracer,end,liquids, entClass);
	}
}
