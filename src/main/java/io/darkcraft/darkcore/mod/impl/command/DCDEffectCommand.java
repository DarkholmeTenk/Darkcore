package io.darkcraft.darkcore.mod.impl.command;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class DCDEffectCommand extends AbstractCommandNew
{
	public DCDEffectCommand()
	{
		super(new AddEffect(), new RemoveEffect());
	}

	@Override
	public String getCommandName(){return "effect";}

	@Override
	public void getAliases(List<String> list)
	{
		list.add("eff");
	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		return false;
	}

	private static class AddEffect extends AbstractCommandNew
	{

		@Override
		public String getCommandName(){return "add";}

		@Override
		public void getAliases(List<String> list){list.add("create");}

		@Override
		public boolean process(ICommandSender sen, List<String> strList)
		{
			if(strList.size() == 0)
				return false;
			EntityPlayer pl = null;
			if(sen instanceof EntityPlayer)
				pl = (EntityPlayer) sen;
			if(strList.size() > 1)
				pl = PlayerHelper.getPlayer(strList.get(0));
			if(pl == null)
			{
				sendString(sen,"No player "+strList.get(0)+" found!");
				return false;
			}
			String nbtStr = strList.size() > 1 ? strList.get(1) : strList.get(0);
			try
			{
				NBTBase nbt = JsonToNBT.func_150315_a(nbtStr);
				if(!(nbt instanceof NBTTagCompound))
				{
					sendString(sen,"Invalid nbt");
					return false;
				}
				AbstractEffect eff = EffectHandler.getEffect(pl, (NBTTagCompound) nbt);
				if(eff != null)
				{
					EntityEffectStore ees = EffectHandler.getEffectStore(pl);
					ees.addEffect(eff);
					sendString(sen,"Effect " + eff.toString() + " applied");
					return true;
				}
				sendString(sen,"Invalid effect nbt");
				return false;
			}
			catch (NBTException e)
			{
				sendString(sen,"Malformed NBT");
				sendString(sen,e.getMessage());
				return false;
			}
		}
	}

	private static class RemoveEffect extends AbstractCommandNew
	{

		@Override
		public String getCommandName(){return "remove";}

		@Override
		public void getAliases(List<String> list){list.add("rem");}

		@Override
		public boolean process(ICommandSender sen, List<String> strList)
		{
			if(strList.size() == 0)
				return false;
			EntityPlayer pl = null;
			if(sen instanceof EntityPlayer)
				pl = (EntityPlayer) sen;
			if(strList.size() > 1)
				pl = PlayerHelper.getPlayer(strList.get(0));
			if(pl == null)
			{
				sendString(sen,"No player "+strList.get(0)+" found!");
				return false;
			}
			String effID = strList.size() > 1 ? strList.get(1) : strList.get(0);
			EntityEffectStore ees = EffectHandler.getEffectStore(pl);
			ees.remove(effID);
			sendString(sen,"Effect " + effID + " should be removed");
			return true;
		}

	}


}
