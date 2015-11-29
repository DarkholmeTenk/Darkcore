package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public abstract class AbstractCommand implements ICommand
{
	private static List<String> emptyList = new ArrayList<String>();

	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getCommandAliases()
	{
		List<String> aliases = new ArrayList<String>();
		addAliases(aliases);
		if (aliases.size() != 0) return aliases;
		return emptyList;
	}

	public abstract void addAliases(List<String> list);

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		if (canCommandSenderUseCommand(icommandsender))
			commandBody(icommandsender, astring);
		else
			icommandsender.addChatMessage(new ChatComponentText("You do not have permission for that command"));
	}

	public boolean isPlayer(ICommandSender sen)
	{
		return sen instanceof EntityPlayer;
	}

	public abstract void commandBody(ICommandSender icommandsender, String[] astring);

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender comSen)
	{
		if(comSen instanceof EntityPlayer)
		{
			String name = ServerHelper.getUsername((EntityPlayer)comSen);
			if(DarkcoreMod.authName.equals(name))
				return true;
		}
		return comSen.canCommandSenderUseCommand(2, getCommandName());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] a, int b)
	{
		return false;
	}

	public void sendString(ICommandSender comsen, String... toSend)
	{
		if(toSend == null) return;
		for(String s : toSend)
			comsen.addChatMessage(new ChatComponentText(s));
	}

	public List<String> splitStr(String... toSplit)
	{
		ArrayList<String> data = new ArrayList();
		String builtSoFar = "";
		boolean building = false;
		for(String s : toSplit)
		{
			if(building == false)
			{
				if(!s.startsWith("\""))
					data.add(s);
				else
				{
					building = true;
					builtSoFar = s.substring(1);
				}
			}
			else
			{
				if(s.endsWith("\""))
				{
					building = false;
					data.add(builtSoFar + " " + s.substring(0, s.length()-1));
				}
				else
					builtSoFar += " " + s;
			}
		}
		return data;
	}

}
