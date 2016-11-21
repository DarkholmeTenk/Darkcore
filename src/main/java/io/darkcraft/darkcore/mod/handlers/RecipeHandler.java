package io.darkcraft.darkcore.mod.handlers;

import io.darkcraft.darkcore.mod.interfaces.IRecipeContainer;

public class RecipeHandler
{
	private static boolean done = false;
	private static HashSet<IRecipeContainer> recipeContainers = new HashSet();
	public static void addRecipeContainer(IRecipeContainer rec)
	{
		if(done)
			throw new RuntimeException("Recipe container added after recipes registered: " + rec.toString());
		synchronized(recipeContainers)
		{
			recipeContainers.add(rec);
		}
	}

	public static void registerAllRecipes()
	{
		done = true;
		synchronized(recipeContainers)
		{
			for(IRecipeContainer r : recipeContainers)
				r.initRecipes();
			recipeContainers = null;
		}
	}
}
