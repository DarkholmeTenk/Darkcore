package io.darkcraft.darkcore.mod.impl.command;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.impl.UniqueSwordItem;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DCDSwordCommand extends AbstractCommandNew
{

	@Override
	public String getCommandName(){return "sword";}

	@Override
	public void getAliases(List<String> list){}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender comSen)
	{
		return UniqueSwordItem.isValid(comSen);
	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		if(DarkcoreMod.authName.equals(sen.getCommandSenderName()))
		{
			if((strList.size() == 1) && strList.get(0).equals("toggle"))
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
		return true;
	}

}
