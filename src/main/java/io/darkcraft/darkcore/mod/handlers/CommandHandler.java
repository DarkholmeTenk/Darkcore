package io.darkcraft.darkcore.mod.handlers;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommand;

import java.util.ArrayList;

import cpw.mods.fml.common.event.FMLServerStartingEvent;


public class CommandHandler
{
	private ArrayList<AbstractCommand> coms = new ArrayList<AbstractCommand>();
	private boolean registered = false;
	
	public void addCommand(AbstractCommand c)
	{
		if(!registered)
			coms.add(c);
	}
	
	public void registerCommands(FMLServerStartingEvent event)
	{
		for(AbstractCommand a : coms)
			event.registerServerCommand(a);
		registered = true;
	}
}
