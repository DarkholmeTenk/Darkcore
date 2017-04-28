package io.darkcraft.darkcore.mod.helpers;

public class DCReflectionHelper
{
	public static <T> T newInstance(Class<T> clazz)
	{
		try
		{
			return clazz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
