package io.darkcraft.darkcore.mod.proxy;

import io.darkcraft.apt.ClientMethod;
import io.darkcraft.apt.ClientMethod.Broadcast;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

@io.darkcraft.apt.CommonProxy
public class CommonProxy extends BaseProxy
{
	public void init()
	{

	}

	public World getWorld(int id)
	{
		return WorldHelper.getWorldServer(id);
	}

	public void postInit()
	{
	}

	public void register(AbstractBlockContainer b){}

	public void register(AbstractItem i){}
	
	@ClientMethod
	public void test(){}
	
	@ClientMethod(broadcast = Broadcast.DIMENSION)
	public void testDimension(int dim, String par1){}
	
	@ClientMethod(broadcast = Broadcast.PLAYER)
	public void testPlayer(EntityPlayerMP test, String par2, int test3){}
}
