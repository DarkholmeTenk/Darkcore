package io.darkcraft.darkcore.mod.abstracts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public abstract class AbstractCommand implements ICommand
{

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
		if (aliases.size() != 0)
			return aliases;
		return null;
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

	public void sendString(ICommandSender comsen, String toSend)
	{
		comsen.addChatMessage(new ChatComponentText(toSend));
	}

}
