package io.darkcraft.darkcore.mod.helpers;

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
}
