package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.helpers.PlayerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.ICommandSender;

/**
 * A command class which allows for subcommands easily
 * @author dark
 *
 */
public abstract class AbstractCommandNew extends AbstractCommand
{
	private static final List<String> emptyStringList = new ArrayList<String>();
	private final AbstractCommandNew[] subCommands;
	private final List<String> allSubArgs;

	public AbstractCommandNew(AbstractCommandNew... subComs)
	{
		if(subComs == null)
			subCommands = new AbstractCommandNew[0];
		else
			subCommands = subComs;
		allSubArgs = new ArrayList<String>();
		for(AbstractCommandNew acn : subComs)
			allSubArgs.addAll(acn.getCommandAliases());
	}

	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return getCommandName();
	}

	public void getCommandUsage(ICommandSender sender, String totalCommand){ getCommandUsage(sender);}

	private List<String> aliasList;
	@Override
	public List getCommandAliases()
	{
		if(aliasList == null)
		{
			aliasList = new ArrayList<String>();
			aliasList.add(getCommandName());
			getAliases(aliasList);
		}
		return aliasList;
	}

	@Override
	public void addAliases(List<String> list)
	{
		getAliases(aliasList);
	}

	@Override
	public void commandBody(ICommandSender sen, String[] strs)
	{
		List<String> strList = new ArrayList<String>();
		String s;
		for(int i = 0; i < strs.length; i++)
		{
			s = strs[i];
			if(s.startsWith("\""))
			{
				if(s.endsWith("\""))
				{
					strList.add(s.substring(1, s.length()-1));
					continue;
				}
				String n = s.substring(1);
				boolean f = false;
				for(int j = i+1;j<strs.length; j++)
				{
					n+= " " + strs[j];
					if(n.endsWith("\""))
					{
						n = n.substring(0,n.length()-1);
						i = j;
						f = true;
						break;
					}
				}
				strList.add(f ? n : s);
			}
			else
				strList.add(s);
		}
		String tCom = "/"+getCommandName();
		commandBody(sen,tCom,strList);
	}

	public boolean commandBody(ICommandSender sen, String totalCommand, List<String> strs)
	{
		if(strs.size() > 0)
		{
			String possibleSubName = strs.get(0);
			List<String> nextList = new ArrayList(strs);
			nextList.remove(0);
			comLoop:
			for(AbstractCommandNew com : subCommands)
			{
				List<String> aliases = com.getCommandAliases();
				for(String alias : aliases)
					if(possibleSubName.equalsIgnoreCase(alias))
					{
						if(com.canCommandSenderUseCommand(sen))
						{
							if(com.commandBody(sen, totalCommand + " " + alias, nextList))
								return true;
							else
								continue comLoop;
						}
						else
							continue comLoop;
					}
			}
		}
		if(process(sen, strs)) return true;
		sendString(sen,"------------");
		sendString(sen, totalCommand);
		if(subCommands.length > 0)
		{
			sendString(sen,"Valid subcommands:");
			for(AbstractCommandNew sc : subCommands)
				sendString(sen,"    "+sc.getCommandName());
		}
		else
		{
			sendString(sen,"------------");
			sendString(sen,"Usage:");
			getCommandUsage(sen,totalCommand);
		}
		return true;
	}

	public List<String> match(String arg, List<String> toMatch)
	{
		if((arg == null) || arg.isEmpty()) return toMatch;
		String lowerArg = arg.toLowerCase();
		ArrayList<String> data = new ArrayList<String>(toMatch);
		Iterator<String> iter = data.iterator();
		while(iter.hasNext())
		{
			String lower = iter.next().toLowerCase();
			if(!lower.startsWith(lowerArg))
				iter.remove();
		}
		return data;
	}

	public List<String> getPlayerList(String arg)
	{
		return match(arg, Arrays.asList(PlayerHelper.getAllUsernames()));
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sen, String[] args)
	{
		if(isUsernameIndex(args,args.length))
			return getPlayerList(args[args.length-1]);
		if(subCommands.length != 0)
		{
			if(args.length > 0)
			{
				for(AbstractCommandNew acn : subCommands)
					for(Object o : acn.getCommandAliases())
					{
						if(!(o instanceof String)) continue;
						if(((String)o).equalsIgnoreCase(args[0]))
						{
							String[] subArgs = new String[args.length-1];
							for(int i = 1; i < args.length; i++)
								subArgs[i-1]=args[i];
							return acn.addTabCompletionOptions(sen, subArgs);
						}
					}
				return match(args[0],allSubArgs);
			}
			return allSubArgs;
		}
		return emptyStringList;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
	{
		return false;
	}

	public abstract void getAliases(List<String> list);

	public abstract boolean process(ICommandSender sen, List<String> strList);

}
