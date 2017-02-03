package io.darkcraft.darkcore.mod.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

public class MathHelper
{
	private static double[] sinData;
	private static int sinNum = 10;
	static
	{
		sinData = new double[360*sinNum];
		for(int i = 0; i < sinData.length; i++)
			sinData[i] = Math.sin(Math.toRadians(i/(float)sinNum));
	}

	public static double sin(double degrees)
	{
		while(degrees < 0) degrees += 36000;
		return sinData[((int) Math.round(degrees * sinNum)) % sinData.length];
	}

	public static double cos(double degrees)
	{
		return sin(degrees + 90);
	}

	public static float interpolate(float a, float b, float mix)
	{
		return (a * mix) + (b * (1-mix));
	}

	public static double interpolate(double a, double b, double mix)
	{
		return (a * mix) + (b * (1-mix));
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
		HashMap<Integer, SimpleDoubleCoordStore> magicStore = new HashMap<>();
		for (SimpleDoubleCoordStore input : inputSet)
		{
			int hash = input.hashCodeTolerance(tolerance);
			magicStore.put(hash, input);
		}
		HashSet<SimpleDoubleCoordStore> coords = new HashSet<>();
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

	public static void reverseObjArray(Object[] arr, int start, int end)
	{
		int l = end - start;
		end--;
		for(int i = 0; i < (l/2); i++)
		{
			Object t = arr[start+i];
			arr[start+i] = arr[end-i];
			arr[end-i] = t;
		}
	}

	public static void shiftObjArray(Object[] arr, int left)
	{
		reverseObjArray(arr,0,left);
		reverseObjArray(arr,left,arr.length);
		reverseObjArray(arr,0,arr.length);
	}

	public static void shiftObjArrayR(Object[] arr, int right)
	{
		int x = arr.length - right;
		shiftObjArray(arr,x);
	}

	public static Vec3 getVecBetween(SimpleDoubleCoordStore from, SimpleDoubleCoordStore to)
	{
		if((from == null) || (to == null)) return null;
		if(from.world != to.world) return null;
		return Vec3.createVectorHelper(to.x-from.x,to.y-from.y,to.z-from.z);
	}

	private static LoadingCache<Integer, int[]> binomialCache = CacheBuilder.newBuilder()
			.weigher(new Weigher<Integer,int[]>() {
				@Override
				public int weigh(Integer key, int[] value)
				{
					return key;
				}
			})
			.maximumWeight(200)
			.build(new CacheLoader<Integer, int[]>() {

				@Override
				public int[] load(Integer key) throws Exception
				{
					if(key <= 1) return new int[]{1};
					int[] previous = binomialCache.getIfPresent(key - 1);
					if(previous == null) previous = load(key - 1);
					int[] newArray = new int[key];
					newArray[0] = 1;
					int mid = (key + 1)/2;
					for(int i = 1; i < mid; i++)
						newArray[i] = previous[i-1] + previous[i];
					for(int i = mid; i < key; i++)
						newArray[i] = newArray[key-i - 1];
					return newArray;
				}

			});

	public static int[] getBinomialCoefficient(int depth)
	{
		return binomialCache.getUnchecked(depth);
	}

	public static double[] getCoefficients(double t, double mt, int size)
	{
		double[] array = new double[size];
		for(int i = 0; i < size; i++) array[i] = 1;
		int lastPos = size - 1;
		for(int i = 0; i < lastPos; i++)
		{
			array[0] *= t;
			array[lastPos] *= mt;
			if(i < (lastPos - 1))
			{
				array[lastPos - i - 1] *= array[0];
				array[i + 1] *= array[lastPos];
			}
		}
		return array;
	}

	public static double castHeight(Entity e)
	{
		if(e instanceof EntityPlayer)
			return ((EntityPlayer)e).eyeHeight;
		else
			return e.height/2;
	}

	public static final ForgeDirection[] horizontal = new ForgeDirection[]{ForgeDirection.NORTH,ForgeDirection.EAST,ForgeDirection.SOUTH,ForgeDirection.WEST};
}
