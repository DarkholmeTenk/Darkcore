package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MathHelper
{
	/**Clamps an integer value so that min <= value <= max
	 */
	public static int clamp(int value, int min, int max)
	{
		return Math.min(max, Math.max(min,value));
	}

	public static double clamp(double value, double min, double max)
	{
		return Math.min(max, Math.max(min,value));
	}

	public static int floor(double in)
	{
		return (int)Math.floor(in);
	}

	public static int ceil(double in)
	{
		return (int)Math.ceil(in);
	}

	public static int round(double in)
	{
		return (int)Math.round(in);
	}

	public static int cycle(int val, int min, int max)
	{
		if (val < min)
			return max;
		if (val > max)
			return min;
		return val;
	}

	public static int toInt(String str, int def)
	{
		try
		{
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
		}
		return def;
	}

	public static double toDouble(String str, double def)
	{
		try
		{
			return Double.parseDouble(str);
		}
		catch (NumberFormatException e)
		{
		}
		return def;
	}

	public static HashSet<SimpleDoubleCoordStore> removeDuplicateLocations(Set<SimpleDoubleCoordStore> inputSet, double tolerance)
	{
		HashMap<Integer,SimpleDoubleCoordStore> magicStore = new HashMap<Integer,SimpleDoubleCoordStore>();
		for(SimpleDoubleCoordStore input : inputSet)
		{
			int hash = input.hashCodeTolerance(tolerance);
			magicStore.put(hash, input);
		}
		HashSet<SimpleDoubleCoordStore> coords = new HashSet<SimpleDoubleCoordStore>();
		coords.addAll(magicStore.values());
		return coords;
	}
}
