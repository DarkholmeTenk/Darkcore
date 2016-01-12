package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommand;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.handlers.packets.MessagePacketHandler;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;

import java.util.List;
import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

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
	public boolean canCommandSenderUseCommand(ICommandSender comSen)
	{
		if(super.canCommandSenderUseCommand(comSen))
			return true;
		return UniqueSwordItem.isValid(comSen);
	}

	@Override
	public void commandBody(ICommandSender sen, String[] astring)
	{
		if(astring.length == 0)
		{
			sendString(sen, getCommandUsage(sen));
			sendString(sen, "Valid Types:");
			sendString(sen, "ChunkLoading");
			sendString(sen, "Message");
			return;
		}
		String type = astring[0];
		String typeLC = type.toLowerCase();
		if(super.canCommandSenderUseCommand(sen))
		{
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
					String filter = null;
					if(astring.length == 3)
						filter = astring[2];
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
				if(com.equals("clear"))
				{
					DarkcoreMod.chunkLoadingHandler.clear();
					sendString(sen,"Chunk loading list cleared");
				}
			}
			else if(typeLC.equals("message") || typeLC.equals("mess") || typeLC.equals("m"))
			{
				if(astring.length < 2)
				{
					sendString(sen, "/dcdebug message <message> [time] [<resource domain> <resource path>]");
					return;
				}
				String s = astring[1];
				if(astring.length == 2)
					MessageHelper.sendToAll(s);
				else if(astring.length == 3)
					MessageHelper.sendToAll(s, MathHelper.toInt(astring[2],MessagePacketHandler.secondsDefault));
				else if(astring.length == 4)
					MessageHelper.sendToAll(new ResourceLocation(astring[2],astring[3]), s);
				else if(astring.length == 5)
					MessageHelper.sendToAll(new ResourceLocation(astring[3],astring[4]), s,
							MathHelper.toInt(astring[2],MessagePacketHandler.secondsDefault));

			}
		}
		if(typeLC.equals("sword"))
		{
			if(UniqueSwordItem.isValid(sen))
			{
				EntityPlayer pl = (EntityPlayer) sen;
				ItemStack sword = new ItemStack(DarkcoreMod.uniqueSword,1);
				if(DarkcoreMod.authName.equals(ServerHelper.getUsername(pl)))
					sword.addEnchantment(Enchantment.looting, 5);
				WorldHelper.giveItemStack(pl, sword);
			}
		}
	}

}
