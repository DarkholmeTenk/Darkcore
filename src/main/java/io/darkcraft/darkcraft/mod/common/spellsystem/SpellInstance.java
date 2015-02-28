package io.darkcraft.darkcraft.mod.common.spellsystem;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcraft.mod.DarkcraftMod;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class SpellInstance
{
	private final LinkedList<ISpellShape> remainingShapes;
	private final ArrayList<ISpellEffect> effects;
	private final ArrayList<ISpellModifier> mods;
	private final SimpleDoubleCoordStore pos;
	private boolean dead = false;
	private int age = 0;
	
	public SpellInstance(BaseSpell base,SimpleDoubleCoordStore _pos)
	{
		remainingShapes = base.getShapes();
		effects = base.getEffects();
		mods = base.getMods();
		pos = _pos;
		DarkcraftMod.spellInstanceRegistry.registerSpellInstance(this);
	}
	
	private SpellInstance(LinkedList<ISpellShape> shapes, SpellInstance parent, SimpleDoubleCoordStore _pos)
	{
		remainingShapes = shapes;
		effects = parent.effects;
		mods = parent.mods;
		pos = _pos;
		DarkcraftMod.spellInstanceRegistry.registerSpellInstance(this);
	}
	
	private SpellInstance(LinkedList<ISpellShape> shapes, ArrayList<ISpellEffect> e, ArrayList<ISpellModifier> m, int a, SimpleDoubleCoordStore _pos)
	{
		remainingShapes = shapes;
		effects = e;
		mods = m;
		age = a;
		pos = _pos;
		DarkcraftMod.spellInstanceRegistry.registerSpellInstance(this);
	}
	
	public void die()
	{
		dead = true;
	}
	
	public boolean isDead(){ return dead; }
	
	private ISpellShape headShape()
	{
		return remainingShapes.getFirst();
	}
	
	private ISpellShape nextShape()
	{
		if(remainingShapes.size() > 1)
			return remainingShapes.get(1);
		return null;
	}
	
	private void createChildren()
	{
		LinkedList<ISpellShape> newShapes = (LinkedList<ISpellShape>) remainingShapes.clone();
		newShapes.removeFirst();
		ISpellShape head = newShapes.getFirst();
		Set<SimpleDoubleCoordStore> locs = head.getNewLocations(pos);
		for(SimpleDoubleCoordStore loc : locs)
			new SpellInstance(newShapes,this,loc);
	}
	
	public void tick()
	{
		age++;
		ISpellShape myShape = headShape();
		int myDur = myShape.getDuration();
		if(age >= myDur)
		{
			die();
			return;
		}
		
		ISpellShape child = nextShape();
		if(child != null)
		{
			int childDur = Math.max(1,child.getDuration());
			int timeLeft = childDur - (age % childDur);
			if(timeLeft > (age - myDur))
			{
				die();
				return;
			}
			if(age % childDur == 0)
				createChildren();
		}
		else
		{
			boolean apply = false;
			for(ISpellEffect e : effects)
				if(age % e.getInterval() == 0)
					apply = true;
			if(!apply)	//None of the effects will actually do anything this tick so skip it
				return;
			Set<EntityLivingBase> ents = myShape.getAffectedEnts(pos);
			Set<SimpleCoordStore> blocks = myShape.getAffectedBlocks(pos);
			for(ISpellEffect e : effects)
			{
				if(age % e.getInterval() != 0)
					continue;
				for(EntityLivingBase ent : ents)
					e.applyEffect(ent);
				for(SimpleCoordStore scs : blocks)
					e.applyEffect(scs);
			}
		}
	}
	
	public static SpellInstance readFromNBT(NBTTagCompound nbt)
	{
		LinkedList<ISpellShape> shapes = SpellHelper.readShapes(nbt);
		ArrayList<ISpellEffect> effects = SpellHelper.readEffects(nbt);
		ArrayList<ISpellModifier> mods = SpellHelper.readModifiers(nbt);
		for(ISpellShape shape : shapes)
			shape.applyModifiers(mods);
		for(ISpellEffect effect : effects)
			effect.applyModifiers(mods);
		int age = nbt.getInteger("age");
		SimpleDoubleCoordStore pos = SimpleDoubleCoordStore.readFromNBT(nbt);
		return new SpellInstance(shapes,effects,mods,age,pos);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		SpellHelper.writeToNBT(nbt,remainingShapes,effects,mods);
		nbt.setInteger("age", age);
		pos.writeToNBT(nbt);
	}
}
