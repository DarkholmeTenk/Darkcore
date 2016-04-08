package io.darkcraft.darkcore.mod.handlers;

import gnu.trove.set.hash.THashSet;
import io.darkcraft.darkcore.mod.interfaces.IRecipeContainer;

public class RecipeHandler
{
	private static boolean done = false;
	private static THashSet<IRecipeContainer> recipeContainers = new THashSet();
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
