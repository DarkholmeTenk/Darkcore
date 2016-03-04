package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.handlers.packets.MessagePacketHandler;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
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

public class DebugCommand extends AbstractCommandNew
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
		int aSize = astring.size();
		if(aSize == 0)
		{
			sendString(sen, getCommandUsage(sen));
			sendString(sen, "Valid Types:");
			sendString(sen, "ChunkLoading");
			sendString(sen, "Message");
			return false;
		}
		String type = astring.get(0);
		String typeLC = type.toLowerCase();
		if(super.canCommandSenderUseCommand(sen))
		{
			if(typeLC.equals("cl") || typeLC.equals("chunkloading") || typeLC.equals("chunkload"))
			{
				if(aSize < 2)
				{
					sendString(sen, "/dcdebug cl <command>", "Valid commands:", "list", "clear");
					return false;
				}
				String com = astring.get(1).toLowerCase();
				if(com.equals("list"))
				{
					String filter = null;
					if(aSize == 3)
						filter = astring.get(2);
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
				if(aSize < 2)
				{
					sendString(sen, "/dcdebug message <message> [time] [<resource domain> <resource path>]");
					return false;
				}
				String s = astring.get(1);
				if(aSize == 2)
					MessageHelper.sendToAll(s);
				else if(aSize == 3)
					MessageHelper.sendToAll(s, MathHelper.toInt(astring.get(2),MessagePacketHandler.secondsDefault));
				else if(aSize == 4)
					MessageHelper.sendToAll(new ResourceLocation(astring.get(2),astring.get(3)), s);
				else if(aSize == 5)
					MessageHelper.sendToAll(new ResourceLocation(astring.get(3),astring.get(4)), s,
							MathHelper.toInt(astring.get(2),MessagePacketHandler.secondsDefault));

			}
		}
		if(typeLC.equals("sword"))
		{
			if(DarkcoreMod.authName.equals(sen.getCommandSenderName()))
			{
				if((aSize == 2) && astring.get(1).equals("toggle"))
				{
					PlayerHelper.toggleSwords();
					MessageHelper.sendMessage(sen, "Swords Enabled: " + PlayerHelper.swordsEnabled());
				}
			}
			if(PlayerHelper.swordsEnabled() && UniqueSwordItem.isValid(sen))
			{
				EntityPlayer pl = (EntityPlayer) sen;
				ItemStack sword = new ItemStack(DarkcoreMod.uniqueSword,1);
				if(DarkcoreMod.authName.equals(ServerHelper.getUsername(pl)))
					sword.addEnchantment(Enchantment.looting, 5);
				WorldHelper.giveItemStack(pl, sword);
			}
		}
		return true;
	}

}
