package io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.util.ForgeDirection;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

public class Area implements ISpellShape
{
	public final static double dist = 2;
	@Override
	public String getID()
	{
		return "area";
	}

	@Override
	public ISpellComponent create()
	{
		return new Area();
	}

	@Override
	public double getCostCoefficient(int exponent)
	{
		switch(exponent)
		{
			case 2 : return 0.5;
			case 1 : return 0.25;
			default : return 0;
		}
	}

	@Override
	public Set<SimpleDoubleCoordStore> getNewLocations(SimpleDoubleCoordStore oL)
	{
		HashSet<SimpleDoubleCoordStore> positions = new HashSet<SimpleDoubleCoordStore>();
		positions.add(oL);
		for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			double oX = d.offsetX * dist;
			double oY = d.offsetY * dist;
			double oZ = d.offsetZ * dist;
			positions.add(new SimpleDoubleCoordStore(oL.world,oL.x+oX,oL.y+oY,oL.z+oZ));
		}
		return positions;
	}

	@Override
	public Set<SimpleDoubleCoordStore> getLocations(EntityLivingBase ent)
	{
		SimpleDoubleCoordStore base = new SimpleDoubleCoordStore(ent);
		return getNewLocations(base);
	}

	@Override
	public Set<EntityLivingBase> getAffectedEnts(SimpleDoubleCoordStore pos)
	{
		HashSet<EntityLivingBase> ents = new HashSet<EntityLivingBase>();
		List possibleEnts = pos.getWorldObj().getEntitiesWithinAABBExcludingEntity(null, pos.getAABB(dist));
		for(Object o : possibleEnts)
		{
			if(o instanceof EntityLivingBase)
			{
				if(pos.distance((EntityLivingBase)o) <= dist)
					ents.add((EntityLivingBase)o);
			}
		}
		return ents;
	}

	@Override
	public Set<SimpleCoordStore> getAffectedBlocks(SimpleDoubleCoordStore pos)
	{
		int max = (int)Math.floor(dist);
		double distSquare = dist * dist;
		SimpleCoordStore base = pos.floor();
		HashSet<SimpleCoordStore> blocks = new HashSet<SimpleCoordStore>();
		for (int x = -max; x <= max; x++)
			for (int y = -max; y <= max; y++)
				for (int z = -max; z <= max; z++)
					if (((x * x) + (y * y) + (z * z)) <= distSquare)
						blocks.add(new SimpleCoordStore(base.world, x + base.x, y + base.y, z + base.z));
		return blocks;
	}

	@Override
	public void applyModifiers(List<ISpellModifier> modifiers)
	{
	}

	@Override
	public int getDuration()
	{
		return 1;
	}

}
