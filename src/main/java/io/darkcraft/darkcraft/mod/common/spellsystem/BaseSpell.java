package io.darkcraft.darkcraft.mod.common.spellsystem;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class BaseSpell
{
	private LinkedList<ISpellShape> shapes;
	private ArrayList<ISpellEffect> effects;
	private ArrayList<ISpellModifier> mods;
	private Double cost = null;
	
	private BaseSpell(LinkedList<ISpellShape> _shapes, ArrayList<ISpellEffect> _effects, ArrayList<ISpellModifier> _mods)
	{
		shapes	= _shapes;
		effects	= _effects;
		mods	= _mods;
	}
	
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
	
	public static BaseSpell readFromStrings(String shapeStr, String effectsStr, String modStr)
	{
		LinkedList<ISpellShape> shapes	= SpellHelper.readShapes(shapeStr);
		ArrayList<ISpellEffect> effects	= SpellHelper.readEffects(effectsStr);
		ArrayList<ISpellModifier> mods	= SpellHelper.readModifiers(modStr);
		return new BaseSpell(shapes,effects,mods);
	}
	
	public static BaseSpell readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("shapes") && nbt.hasKey("effects"))
		{
			LinkedList<ISpellShape> shapes	= SpellHelper.readShapes(nbt);
			ArrayList<ISpellEffect> effects	= SpellHelper.readEffects(nbt);
			ArrayList<ISpellModifier> mods	= SpellHelper.readModifiers(nbt);
			return new BaseSpell(shapes,effects,mods);
		}
		return null;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		SpellHelper.writeToNBT(nbt, shapes, effects, mods);
	}
}
