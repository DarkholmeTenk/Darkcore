package io.darkcraft.darkcore.mod.impl.command;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.impl.UniqueSwordItem;

import java.util.List;

import net.minecraft.command.ICommandSender;

public class DebugCommand extends AbstractCommandNew
{
	public DebugCommand()
	{
		super(new DCDChunkLoadingCommand(), new DCDSwordCommand(), new DCDMessageCommand(), new DCDEffectCommand(), new DCDWorldCommand());
	}

	@Override
	public String getCommandName()
	{
		return "dcdebug";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender comSen)
	{
		if(super.canCommandSenderUseCommand(comSen))
			return true;
		return UniqueSwordItem.isValid(comSen);
	}

	@Override
	public void getAliases(List<String> list)
	{
		list.add("dcd");
	}

	@Override
	public boolean process(ICommandSender sen, List<String> astring)
	{
		return false;
	}

}
