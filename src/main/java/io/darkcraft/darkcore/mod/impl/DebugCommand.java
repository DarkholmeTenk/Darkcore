package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommand;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

import java.util.List;
import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;

public class DebugCommand extends AbstractCommand
{

	@Override
	public String getCommandName()
	{
		return "dcdebug";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return "/dcdebug <type> [args]";
	}

	@Override
	public void addAliases(List<String> list)
	{
		list.add("dcd");
	}

	@Override
	public void commandBody(ICommandSender sen, String[] astring)
	{
		if(astring.length == 0)
		{
			sendString(sen, getCommandUsage(sen));
			sendString(sen, "Valid Types:");
			sendString(sen, "ChunkLoading");
			return;
		}
		String type = astring[0];
		String typeLC = type.toLowerCase();
		if(typeLC.equals("cl") || typeLC.equals("chunkloading") || typeLC.equals("chunkload"))
		{
			if(astring.length < 2)
			{
				sendString(sen, "/dcdebug cl <command>", "Valid commands:", "list", "clear");
				return;
			}
			String com = astring[1].toLowerCase();
			if(com.equals("list"))
			{
				Set<SimpleCoordStore> points = DarkcoreMod.chunkLoadingHandler.getLoadables();
				sendString(sen,"Loaded stuff:");
				for(SimpleCoordStore scs : points)
				{
					TileEntity te = scs.getTileEntity();
					sendString(sen,scs + " - " + te);
				}
			}
			if(com.equals("clear"))
			{
				DarkcoreMod.chunkLoadingHandler.clear();
				sendString(sen,"Chunk loading list cleared");
			}
		}
	}

}
