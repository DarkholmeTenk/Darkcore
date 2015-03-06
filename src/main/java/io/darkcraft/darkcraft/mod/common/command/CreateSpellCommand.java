package io.darkcraft.darkcraft.mod.common.command;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import io.darkcraft.darkcore.mod.abstracts.AbstractCommand;
import io.darkcraft.darkcraft.mod.common.spellsystem.BaseSpell;

public class CreateSpellCommand extends AbstractCommand
{

	@Override
	public String getCommandName()
	{
		return "darkcraftcreatespell";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return null;
	}

	@Override
	public void addAliases(List<String> list)
	{
		list.add("dccreatespell");
	}

	@Override
	public void commandBody(ICommandSender sender, String[] astring)
	{
		if(astring.length < 2)
			return;
		//2 args: shapes, effects
		if(astring.length == 2)
		{
			if(!isPlayer(sender))
				return;
			BaseSpell b = BaseSpell.readFromStrings(astring[0], astring[1], null);
			if(b == null)
				return;
			NBTTagCompound nbt = new NBTTagCompound();
			b.writeToNBT(nbt);
			EntityPlayer pl = (EntityPlayer) sender;
			ItemStack is = pl.getHeldItem();
			if(is.stackTagCompound == null)
				is.stackTagCompound = new NBTTagCompound();
			is.stackTagCompound.setTag("currentSpell", nbt);
		}
	}

}
