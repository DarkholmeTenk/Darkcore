package io.darkcraft.darkcraft.mod.common.spellsystem;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;

public class BaseSpell
{
	private LinkedList<ISpellShape> shapes;
	private ArrayList<ISpellEffect> effects;
	private ArrayList<ISpellModifier> mods;
	private Double cost = null;
	
	public void cast(EntityPlayer pl)
	{
		ISpellShape head = shapes.getFirst();
		Set<SimpleDoubleCoordStore> locs = head.getLocations(pl);
		for(SimpleDoubleCoordStore loc : locs)
			new SpellInstance(this,loc);
	}
	
	public double getCost()
	{
		if(cost != null)
			return cost;
		double totalCost = 0;
		for(ISpellEffect eff : effects)
			totalCost += eff.getBaseCost();
		for(ISpellShape sh : shapes)
		{
			double tC = 0;
			tC += sh.getCostCoefficient(0);
			tC += (1+Math.abs(sh.getCostCoefficient(1))) * totalCost;
			tC += sh.getCostCoefficient(2) * Math.pow(totalCost, 2);
			totalCost = tC;
		}
		return (cost = totalCost);
	}
	
	protected LinkedList<ISpellShape> getShapes()
	{
		return shapes;
	}
	
	protected ArrayList<ISpellEffect> getEffects()
	{
		return effects;
	}
	
	protected ArrayList<ISpellModifier> getMods()
	{
		return mods;
	}
}
