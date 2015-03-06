package io.darkcraft.darkcraft.mod.common.spellsystem.components.effects;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcraft.mod.DarkcraftMod;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;

public class Damage implements ISpellEffect
{
	@Override
	public double getBaseCost()
	{
		return 1;
	}

	@Override
	public void applyEffect(EntityLivingBase ent)
	{
		ent.attackEntityFrom(DamageSource.generic, 10);
	}

	@Override
	public void applyEffect(SimpleCoordStore scs)
	{
	}

	@Override
	public int getInterval()
	{
		return 4;
	}

	@Override
	public void applyModifiers(List<ISpellModifier> modifiers)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getID()
	{
		return "dam";
	}

	@Override
	public ISpellEffect create()
	{
		// TODO Auto-generated method stub
		return new Damage();
	}

}
