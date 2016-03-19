package io.darkcraft.darkcore.mod.proxy;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.client.EffectOverlayRenderer;
import io.darkcraft.darkcore.mod.client.MessageOverlayRenderer;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.impl.UniqueSwordRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		MinecraftForgeClient.registerItemRenderer(DarkcoreMod.uniqueSword, new UniqueSwordRenderer());
		MinecraftForge.EVENT_BUS.register(MessageOverlayRenderer.i);
		MinecraftForge.EVENT_BUS.register(EffectOverlayRenderer.i);
		EffectOverlayRenderer.refreshConfigs();
	}

	@Override
	public World getWorld(int id)
	{
		World w = Minecraft.getMinecraft().theWorld;
		if ((w != null) && (WorldHelper.getWorldID(w) == id)) return w;
		if(DarkcoreMod.debugText)
			Thread.dumpStack();
		return null;
	}
}
