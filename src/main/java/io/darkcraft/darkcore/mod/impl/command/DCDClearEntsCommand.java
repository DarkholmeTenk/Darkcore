package io.darkcraft.darkcore.mod.impl.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;

public class DCDClearEntsCommand extends AbstractCommandNew
{

	@Override
	public String getCommandName()
	{
		return "killents";
	}

	@Override
	public void getAliases(List<String> list)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		if(!(sen instanceof EntityPlayer)) return false;
		World w = ((EntityPlayer)sen).worldObj;
		if(w == null) return false;
		Set<String> entNames = new HashSet<>();
		for(String s : strList)
			entNames.add(s.toLowerCase());
		for(Object entO : w.loadedEntityList)
		{
			if(!(entO instanceof Entity)) continue;
			Entity ent = (Entity) entO;
			Class c = ent.getClass();
			if(entNames.contains(c.getName().toLowerCase())
					|| entNames.contains(c.getSimpleName().toLowerCase()))
			{
				w.removeEntity(ent);
			}
		}
		return strList.size() > 0;
	}

}
