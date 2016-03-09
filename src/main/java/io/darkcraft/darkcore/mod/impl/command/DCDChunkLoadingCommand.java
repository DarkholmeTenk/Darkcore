package io.darkcraft.darkcore.mod.impl.command;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

import java.util.List;
import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;

public class DCDChunkLoadingCommand extends AbstractCommandNew
{

	@Override
	public String getCommandName()
	{
		return "chunkloading";
	}

	@Override
	public void getCommandUsage(ICommandSender sen, String tc)
	{
		sendString(sen,tc + " [list/clear]");
	}

	@Override
	public void getAliases(List<String> list)
	{
		list.add("cl");
		list.add("chunkloaders");
		list.add("chunkload");
		list.add("chunk");
	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		if(strList.size() < 1)
		{
			return false;
		}
		String com = strList.get(0);
		if(com.equals("list"))
		{
			String filter = null;
			if(strList.size() == 2)
				filter = strList.get(1);
			Set<SimpleCoordStore> points = DarkcoreMod.chunkLoadingHandler.getLoadables();
			sendString(sen,"Loaded stuff:");
			for(SimpleCoordStore scs : points)
			{
				TileEntity te = scs.getTileEntity();
				String name = te.getClass().getSimpleName();
				if((filter == null) || name.equals(filter))
					sendString(sen,scs + " - " + te);
			}
		}
		else if(com.equals("clear"))
		{
			DarkcoreMod.chunkLoadingHandler.clear();
			sendString(sen,"Chunk loading list cleared");
		}
		else
			return false;
		return true;
	}

}
