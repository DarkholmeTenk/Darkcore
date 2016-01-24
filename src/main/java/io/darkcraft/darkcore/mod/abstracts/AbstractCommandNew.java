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

	public AbstractCommandNew(AbstractCommandNew... subComs)
	{
		if(subComs == null)
			subCommands = new AbstractCommandNew[0];
		else
			subCommands = subComs;
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

	private List<String> aliasList;
	@Override
	public List getCommandAliases()
	{
		if(aliasList == null)
		{
			aliasList = new ArrayList<String>();
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
		for(String s : strs)
			strList.add(s);
		commandBody(sen,strList);
	}

	public boolean commandBody(ICommandSender sen, List<String> strs)
	{
		if(strs.size() > 0)
		{
			String possibleSubName = strs.get(0);
			List<String> nextList = new ArrayList(strs);
			nextList.remove(0);
			comLoop:
			for(AbstractCommandNew com : subCommands)
			{
				if(possibleSubName.equalsIgnoreCase(com.getCommandName()))
				{
					if(com.commandBody(sen, nextList))
						return true;
					else
						continue comLoop;
				}
				List<String> aliases = com.getCommandAliases();
				for(String alias : aliases)
					if(possibleSubName.equalsIgnoreCase(alias))
					{
						if(com.commandBody(sen, nextList))
							return true;
						else
							continue comLoop;
					}
			}
		}
		return process(sen, strs);
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
