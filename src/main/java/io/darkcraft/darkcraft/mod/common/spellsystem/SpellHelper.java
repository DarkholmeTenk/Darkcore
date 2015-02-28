package io.darkcraft.darkcraft.mod.common.spellsystem;

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
			shapes.append(shape.getID()).append(':');
		nbt.setString("shapes", shapes.toString());
		
		StringBuilder effects = new StringBuilder();
		for(ISpellEffect effect : effs)
			effects.append(effect.getID()).append(':');
		nbt.setString("effects", effects.toString());
		
		StringBuilder modifiers = new StringBuilder();
		for(ISpellModifier modifier : mdrs)
			modifiers.append(modifier.getID()).append(':');
		nbt.setString("modifiers", modifiers.toString());
	}
	
	public static LinkedList<ISpellShape> readShapes(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("shapes");
		String[] individualEffects = baseData.split(":");
		LinkedList<ISpellShape> shapes = new LinkedList<ISpellShape>();
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisShape = individualEffects[i];
			if(thisShape.length() < 1)
				continue;
			ISpellShape shape = SpellComponentRegistry.getShape(thisShape);
			shapes.addLast(shape);
		}
		return shapes;
	}
	
	public static ArrayList<ISpellEffect> readEffects(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("effects");
		String[] individualEffects = baseData.split(":");
		ArrayList<ISpellEffect> effects = new ArrayList<ISpellEffect>();
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisEffect = individualEffects[i];
			if(thisEffect.length() < 1)
				continue;
			ISpellEffect effect = SpellComponentRegistry.getEffect(thisEffect);
			effects.add(effect);
		}
		return effects;
	}
	
	public static ArrayList<ISpellModifier> readModifiers(NBTTagCompound nbt)
	{
		String baseData = nbt.getString("modifiers");
		String[] individualEffects = baseData.split(":");
		ArrayList<ISpellModifier> modifiers = new ArrayList<ISpellModifier>();
		for(int i = 0; i< individualEffects.length; i++)
		{
			String thisEffect = individualEffects[i];
			if(thisEffect.length() < 1)
				continue;
			ISpellModifier modifier = SpellComponentRegistry.getModifier(thisEffect);
			modifiers.add(modifier);
		}
		return modifiers;
	}
}
