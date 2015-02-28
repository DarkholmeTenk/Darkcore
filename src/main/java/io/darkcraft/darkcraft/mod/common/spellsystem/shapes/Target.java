package io.darkcraft.darkcraft.mod.common.spellsystem.shapes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.PlayerMagicHelper;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

public class Target implements ISpellShape
{

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SimpleCoordStore> getAffectedBlocks(SimpleDoubleCoordStore pos)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyModifiers(List<ISpellModifier> modifiers)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getDuration()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getID()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISpellShape create()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
