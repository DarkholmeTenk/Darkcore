package io.darkcraft.darkcore.mod.impl.command;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

public class DCDWorldCommand extends AbstractCommandNew
{
	public DCDWorldCommand()
	{
		super(new RenameCommand(), new GetNameCommand());
	}

	@Override
	public String getCommandName(){return "world";}

	@Override
	public void getAliases(List<String> list){}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		return false;
	}

	private static class RenameCommand extends AbstractCommandNew
	{

		@Override
		public String getCommandName(){return "rename";}

		@Override
		public void getAliases(List<String> list)
		{
			list.add("ren");
			list.add("set");
			list.add("setname");
		}

		@Override
		public void getCommandUsage(ICommandSender sen, String tc)
		{
			sendString(sen,tc+" [worldID] [newName|null for default]");
		}

		@Override
		public boolean process(ICommandSender sen, List<String> strList)
		{
			if(strList.size() == 2)
			{
				try
				{
					World w = WorldHelper.getWorld(Integer.parseInt(strList.get(0)));
					if(w == null)
					{
						sendString(sen,"World " + strList.get(0) + " not found");
						return false;
					}
					String newName = strList.get(1);
					WorldHelper.setDimensionName(w, newName);
					sendString(sen,"Dimension name changed to " + WorldHelper.getDimensionName(w));
					return true;
				}
				catch(NumberFormatException e)
				{
					sendString(sen,strList.get(0) + " does not appear to be a number");
					return false;
				}
			}
			return false;
		}
	}

	private static class GetNameCommand extends AbstractCommandNew
	{

		@Override
		public String getCommandName(){return "getname";}

		@Override
		public void getAliases(List<String> list)
		{
			list.add("get");
			list.add("g");
		}

		@Override
		public void getCommandUsage(ICommandSender sen, String tc)
		{
			sendString(sen,tc+" [worldID]");
		}

		@Override
		public boolean process(ICommandSender sen, List<String> strList)
		{
			if(strList.size() == 1)
			{
				try
				{
					World w = WorldHelper.getWorld(Integer.parseInt(strList.get(0)));
					if(w == null)
					{
						sendString(sen,"World " + strList.get(0) + " not found");
						return false;
					}
					sendString(sen, "World " + strList.get(0) + ": " + WorldHelper.getDimensionName(w));
					return true;
				}
				catch(NumberFormatException e)
				{
					sendString(sen,strList.get(0) + " does not appear to be a number");
					return false;
				}
			}
			return false;
		}

	}
}
