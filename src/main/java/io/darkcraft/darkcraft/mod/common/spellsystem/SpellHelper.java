package io.darkcraft.darkcraft.mod.common.spellsystem;

import io.darkcraft.darkcraft.mod.common.registries.BaseSpellRegistry;
import io.darkcraft.darkcraft.mod.common.registries.SpellComponentRegistry;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.ArrayList;
import java.util.LinkedList;

import net.minecraft.nbt.NBTTagCompound;

public class SpellHelper
{
	public static void writeToNBT(NBTTagCompound nbt, LinkedList<ISpellShape> shps, ArrayList<ISpellEffect> effs,
			ArrayList<ISpellModifier> mdrs)
	{
		StringBuilder shapes =  new StringBuilder();
		for(ISpellShape shape : shps)
			if(shape != null)
				shapes.append(shape.getID()).append(':');
		nbt.setString("shapes", shapes.toString());
		
		StringBuilder effects = new StringBuilder();
		for(ISpellEffect effect : effs)
			if(effect != null)
				effects.append(effect.getID()).append(':');
		nbt.setString("effects", effects.toString());
		
		StringBuilder modifiers = new StringBuilder();
		for(ISpellModifier modifier : mdrs)
			if(modifier != null)
				modifiers.append(modifier.getID()).append(':');
		nbt.setString("modifiers", modifiers.toString());
	}
	
	public static LinkedList<ISpellShape> readShapes(String baseData)
	{
		LinkedList<ISpellShape> shapes = new LinkedList<ISpellShape>();
		if(baseData == null)
			return shapes;
		if(BaseSpellRegistry.shapeRegistry.containsKey(baseData))
			return BaseSpellRegistry.shapeRegistry.get(baseData);
		String[] individualEffects = baseData.split(":");
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisShape = individualEffects[i];
			if(thisShape.length() < 1)
				continue;
			ISpellShape shape = SpellComponentRegistry.getShape(thisShape);
			if(shape != null)
				shapes.addLast(shape);
		}
		return shapes;
	}
	
	public static LinkedList<ISpellShape> readShapes(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("shapes");
		return readShapes(baseData);
	}
	
	public static ArrayList<ISpellEffect> readEffects(String baseData)
	{
		ArrayList<ISpellEffect> effects = new ArrayList<ISpellEffect>();
		if(baseData == null)
			return effects;
		String[] individualEffects = baseData.split(":");
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisEffect = individualEffects[i];
			if(thisEffect.length() < 1)
				continue;
			ISpellEffect effect = SpellComponentRegistry.getEffect(thisEffect);
			if(effect != null)
				effects.add(effect);
		}
		return effects;
	}
	
	public static ArrayList<ISpellEffect> readEffects(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("effects");
		return readEffects(baseData);
	}
	
	public static ArrayList<ISpellModifier> readModifiers(String baseData)
	{
		ArrayList<ISpellModifier> modifiers = new ArrayList<ISpellModifier>();
		if(baseData == null)
			return modifiers;
		String[] individualEffects = baseData.split(":");
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisEffect = individualEffects[i];
			if(thisEffect.length() < 1)
				continue;
			ISpellModifier modifier = SpellComponentRegistry.getModifier(thisEffect);
			if(modifier != null)
				modifiers.add(modifier);
		}
		return modifiers;
	}
	
	public static ArrayList<ISpellModifier> readModifiers(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("modifiers");
		return readModifiers(baseData);
	}
}
