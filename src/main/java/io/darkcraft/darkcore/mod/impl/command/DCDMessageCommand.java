package io.darkcraft.darkcore.mod.impl.command;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.handlers.packets.MessagePacketHandler;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;

public class DCDMessageCommand extends AbstractCommandNew
{

	@Override
	public String getCommandName(){return "message";}

	@Override
	public void getAliases(List<String> list)
	{
		list.add("mess");
		list.add("m");
	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		if(strList.size() < 1)
		{
			sendString(sen, "/dcdebug message <message> [time] [<resource domain> <resource path>]");
			return false;
		}
		int lSize = strList.size();
		String s = strList.get(0);
		if(lSize == 1)
			MessageHelper.sendToAll(s);
		else if(lSize == 2)
			MessageHelper.sendToAll(s, MathHelper.toInt(strList.get(1),MessagePacketHandler.secondsDefault));
		else if(lSize == 3)
			MessageHelper.sendToAll(new ResourceLocation(strList.get(1),strList.get(2)), s);
		else if(lSize == 4)
			MessageHelper.sendToAll(new ResourceLocation(strList.get(2),strList.get(3)), s,
					MathHelper.toInt(strList.get(1),MessagePacketHandler.secondsDefault));
		return false;
	}

}
