package io.darkcraft.darkcore.mod.helpers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RaytraceHelper
{
	private static final double defaultExpansion = 0.3;
	private static final Entity[] emptyArr = {};

	public static MovingObjectPosition rayIntersectEntity(Entity ent, Vec3 origin, Vec3 end)
	{
		return rayIntersectEntity(ent, origin, end, 0);
	}

	public static MovingObjectPosition rayIntersectEntity(Entity ent, Vec3 origin, Vec3 end, double expansion)
	{
		AxisAlignedBB aabb = ent.boundingBox.expand(expansion, expansion, expansion);
		//aabb = aabb.addCoord(ent.posX, ent.posY, ent.posZ);
		if(aabb.isVecInside(origin) || aabb.isVecInside(end))
			return new MovingObjectPosition(ent,end);
		MovingObjectPosition mop = aabb.calculateIntercept(origin, end);
		return mop;
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

	public static Vec3 getEndPosition(Entity tracer, int dist)
	{
		Vec3 end = tracer.getLookVec();
		if(dist != 1)
		{
			end = Vec3.createVectorHelper(end.xCoord*dist, end.yCoord*dist, end.zCoord*dist);
		}
		return end.addVector(tracer.posX, tracer.posY + tracer.getEyeHeight(), tracer.posZ);
	}

	public static MovingObjectPosition rayTraceBlocks(World w, Vec3 s, Vec3 e, boolean l)
	{
		Vec3 s2 = Vec3.createVectorHelper(s.xCoord, s.yCoord, s.zCoord);
		Vec3 e2 = Vec3.createVectorHelper(e.xCoord, e.yCoord, e.zCoord);
		return w.rayTraceBlocks(s2, e2, l);
	}

	public static MovingObjectPosition rayTrace(Entity tracer, Vec3 end, boolean liquids, Class<? extends Entity> entClass, boolean doBlocks, boolean doEnts, Entity... skip)
	{
		Vec3 start = Vec3.createVectorHelper(tracer.posX, tracer.posY, tracer.posZ);
		if(ServerHelper.isServer() && (tracer instanceof EntityPlayer))
			start = Vec3.createVectorHelper(tracer.posX, tracer.posY + tracer.getEyeHeight(), tracer.posZ);
		World w = tracer.worldObj;
		if((tracer == null) || (w == null)) return null;
		MovingObjectPosition mop = null;
		double dist = Double.MAX_VALUE;
		if(doBlocks)
		{
			mop = rayTraceBlocks(w, start, end, liquids);
			if(mop != null)
			{
				dist = start.distanceTo(mop.hitVec);
				end = mop.hitVec;
			}
		}
		if(doEnts)
		{
			List<Entity> multiparts = new ArrayList<>();
			List entityList = w.getEntitiesWithinAABBExcludingEntity(tracer, getAABB(start, end).expand(0.8, 0.8, 0.8));

			aLoop:
			for(Object o : entityList)
			{
				for(Entity e : skip) if(o == e) continue aLoop;
				if(!(o instanceof Entity)) continue;
				Entity e = (Entity) o;
				if((e instanceof IEntityMultiPart) && (entClass != null) && entClass.isInstance(e))
					multiparts.add(e);
			}

			oLoop:
			for(Object o : entityList)
			{
				for(Entity e : skip) if(o == e) continue oLoop;
				if(!(o instanceof Entity)) continue;
				Entity e = (Entity) o;
				if(!e.canBeCollidedWith()) continue;
				if((entClass != null) && !entClass.isInstance(e))
				{
					boolean f = false;
					for(Entity mp : multiparts)
						if(e.isEntityEqual(mp))
						{
							f = true;
							break;
						}
					if(!f)
						continue oLoop;
				}
				MovingObjectPosition entityMop = rayIntersectEntity(e, start, end);
				if(entityMop == null) continue;
				entityMop.entityHit = e;
				entityMop.typeOfHit = MovingObjectType.ENTITY;
				double entDist = start.distanceTo(entityMop.hitVec);
				if(entDist < dist)
				{
					mop = entityMop;
					dist = entDist;
				}
			}
		}
		return mop;
	}

	public static MovingObjectPosition rayTrace(Entity tracer, Vec3 end, boolean liquids, Class<? extends Entity> entClass, boolean blocks, boolean ents)
	{
		return rayTrace(tracer,end,liquids,entClass,blocks,ents, emptyArr);
	}

	public static MovingObjectPosition rayTrace(Entity tracer, double dist, boolean liquids, Class<? extends Entity> entClass, boolean blocks, boolean ents, Entity... skip)
	{
		Vec3 end = tracer.getLookVec();
		if(dist != 1)
		{
			double dsq = dist;
			end = Vec3.createVectorHelper(end.xCoord*dsq, end.yCoord*dsq, end.zCoord*dsq);
		}
		end = end.addVector(tracer.posX, tracer.posY + tracer.getEyeHeight(), tracer.posZ);
		return rayTrace(tracer,end,liquids, entClass, blocks, ents, skip);
	}

	public static MovingObjectPosition rayTrace(Entity tracer, double dist, boolean liquids, Class<? extends Entity> entClass, boolean blocks, boolean ents)
	{
		return rayTrace(tracer, dist, liquids, entClass, blocks, ents, emptyArr);
	}

	public static MovingObjectPosition rayTrace(Entity tracer, boolean liquids, Class<? extends Entity> entClass, boolean blocks, boolean ents, Entity... skip)
	{
		Vec3 end = Vec3.createVectorHelper(tracer.posX+tracer.motionX, tracer.posY+tracer.motionY, tracer.posZ+tracer.motionZ);
		return rayTrace(tracer,end,liquids, entClass, blocks, ents, skip);
	}

	public static MovingObjectPosition rayTrace(Entity tracer, boolean liquids, Class<? extends Entity> entClass, boolean blocks, boolean ents)
	{
		return rayTrace(tracer,liquids,entClass,blocks,ents,emptyArr);
	}
}
