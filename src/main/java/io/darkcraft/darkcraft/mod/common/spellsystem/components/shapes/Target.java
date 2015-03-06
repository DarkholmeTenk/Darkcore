package io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.PlayerMagicHelper;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

public class Target implements ISpellShape
{
	private static final double dist = 0.75;

	@Override
	public double getCostCoefficient(int exponent)
	{
		switch(exponent)
		{
			case 1 : return 0.25;
			default : return 0;
		}
	}

	@Override
	public Set<SimpleDoubleCoordStore> getNewLocations(SimpleDoubleCoordStore originalLocation)
	{
		Set<SimpleDoubleCoordStore> nl = new HashSet<SimpleDoubleCoordStore>();
		nl.add(originalLocation);
		return nl;
	}

	@Override
	public Set<SimpleDoubleCoordStore> getLocations(EntityLivingBase ent)
	{
		Set<SimpleDoubleCoordStore> locs = new HashSet<SimpleDoubleCoordStore>();
		SimpleDoubleCoordStore aimingAt = PlayerMagicHelper.getAimingAt(ent, 30);
		if(aimingAt != null)
			locs.add(PlayerMagicHelper.getAimingAt(ent, 30));
		return locs;
	}

	@Override
	public Set<EntityLivingBase> getAffectedEnts(SimpleDoubleCoordStore pos)
	{
		HashSet<EntityLivingBase> ents = new HashSet<EntityLivingBase>();
		if(pos == null)
			return ents;
		World w = pos.getWorldObj();
		List possibleEnts = w.getEntitiesWithinAABBExcludingEntity(null, pos.getAABB(dist));
		for(Object o : possibleEnts)
		{
			System.out.println("::"+o.toString());
			if(o instanceof EntityLivingBase)
			{
				System.out.println("E:"+pos.distance((EntityLivingBase)o));
				if(pos.distance((EntityLivingBase)o) < dist)
					ents.add((EntityLivingBase)o);
			}
		}
		return ents;
	}

	@Override
	public Set<SimpleCoordStore> getAffectedBlocks(SimpleDoubleCoordStore pos)
	{
		HashSet<SimpleCoordStore> blocks = new HashSet<SimpleCoordStore>();
		SimpleCoordStore block = new SimpleCoordStore(pos);
		World w = block.getWorldObj();
		if(!w.isAirBlock(block.x, block.y, block.z))
			blocks.add(block);
		else
		{
			for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
			{
				SimpleCoordStore nearby = block.getNearby(d);
				if(!w.isAirBlock(nearby.x, nearby.y, nearby.z))
					blocks.add(nearby);
			}
		}
		return blocks;
	}

	@Override
	public void applyModifiers(List<ISpellModifier> modifiers)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getDuration()
	{
		return 1;
	}

	@Override
	public String getID()
	{
		return "tar";
	}

	@Override
	public ISpellShape create()
	{
		return new Target();
	}

}
