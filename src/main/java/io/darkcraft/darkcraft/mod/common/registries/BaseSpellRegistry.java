package io.darkcraft.darkcraft.mod.common.registries;

import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BaseSpellRegistry
{
	public static HashMap<String,LinkedList<ISpellShape>> shapeRegistry		= new HashMap<String,LinkedList<ISpellShape>>();
	public static HashMap<String,ArrayList<ISpellEffect>> effectRegistry	= new HashMap<String,ArrayList<ISpellEffect>>();
}
