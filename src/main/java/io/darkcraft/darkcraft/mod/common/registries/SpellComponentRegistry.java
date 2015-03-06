package io.darkcraft.darkcraft.mod.common.registries;

import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.HashMap;

public class SpellComponentRegistry
{
	private static HashMap<String,ISpellShape> shapes = new HashMap<String,ISpellShape>();
	private static HashMap<String,ISpellEffect> effects = new HashMap<String,ISpellEffect>();
	private static HashMap<String,ISpellModifier> modifiers = new HashMap<String,ISpellModifier>();
	
	public static ISpellShape getShape(String id)
	{
		if(shapes.containsKey(id))
			return (ISpellShape) shapes.get(id).create();
		return null;
	}
	
	public static ISpellEffect getEffect(String id)
	{
		if(effects.containsKey(id))
			return (ISpellEffect) effects.get(id).create();
		return null;
	}
	
	public static ISpellModifier getModifier(String id)
	{
		if(modifiers.containsKey(id))
			return (ISpellModifier) modifiers.get(id).create();
		return null;
	}
	
	public static void add(ISpellComponent com)
	{
		if(com instanceof ISpellShape)
			shapes.put(com.getID(),(ISpellShape)com);
		if(com instanceof ISpellEffect)
			effects.put(com.getID(),(ISpellEffect)com);
		if(com instanceof ISpellModifier)
			modifiers.put(com.getID(),(ISpellModifier) com);
	}
	
}
