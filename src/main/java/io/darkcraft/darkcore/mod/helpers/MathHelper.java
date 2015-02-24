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
}
