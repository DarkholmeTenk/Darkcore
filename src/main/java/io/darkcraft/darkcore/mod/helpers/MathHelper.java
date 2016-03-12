package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MathHelper
{
	private static double[] sinData;
	private static int sinNum = 4;
	static
	{
		sinData = new double[360*sinNum];
		for(int i = 0; i < sinData.length; i++)
			sinData[i] = Math.sin(Math.toRadians(i/(float)sinNum));
	}

	public static double sin(double degrees)
	{
		if(degrees < 0) degrees += 36000;
		return sinData[((int) Math.round(degrees * sinNum)) % sinData.length];
	}

	public static double cos(double degrees)
	{
		return sin(degrees + 90);
	}

	/**
	 * Clamps an integer value so that min <= value <= max
	 */
	public static int clamp(int value, int min, int max)
	{
		return Math.min(max, Math.max(min, value));
	}

	public static double clamp(double value, double min, double max)
	{
		return Math.min(max, Math.max(min, value));
	}

	public static int floor(double in)
	{
		return (int) Math.floor(in);
	}

	public static int ceil(double in)
	{
		return (int) Math.ceil(in);
	}

	public static int round(double in)
	{
		return (int) Math.round(in);
	}

	public static int cycle(int val, int min, int max)
	{
		if (val < min) return max;
		if (val > max) return min;
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
		HashMap<Integer, SimpleDoubleCoordStore> magicStore = new HashMap<Integer, SimpleDoubleCoordStore>();
		for (SimpleDoubleCoordStore input : inputSet)
		{
			int hash = input.hashCodeTolerance(tolerance);
			magicStore.put(hash, input);
		}
		HashSet<SimpleDoubleCoordStore> coords = new HashSet<SimpleDoubleCoordStore>();
		coords.addAll(magicStore.values());
		return coords;
	}

	public static int getNextUpDownInteger(int current)
	{
		if(current == 0)
			return 1;
		if(current > 0)
			return -current;
		return (-current)+1;
	}

	public static String getTimeString(int seconds)
	{
		if(seconds < 3600)
		{
			int ml = seconds / 60;
			seconds = seconds % 60;
			return String.format("%02d:%02d", ml,seconds);
		}
		else
		{
			int hl = seconds / 3600;
			seconds %= 3600;
			int ml = seconds / 60;
			seconds %= 60;
			return String.format("%02d:%02d:%02d", hl,ml,seconds);
		}
	}
}
