package io.darkcraft.darkcore.mod.handlers;

import java.util.ArrayList;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommand;

public class CommandHandler
{
	private ArrayList<AbstractCommand>	coms		= new ArrayList<AbstractCommand>();
	private boolean						registered	= false;

	public static void registerCommand(AbstractCommand c)
	{
		DarkcoreMod.comHandler.addCommand(c);
	}

	public void addCommand(AbstractCommand c)
	{
		if (!registered) coms.add(c);
	}

	public void registerCommands(FMLServerStartingEvent event)
	{
		for (AbstractCommand a : coms)
			event.registerServerCommand(a);
		registered = true;
	}
}
