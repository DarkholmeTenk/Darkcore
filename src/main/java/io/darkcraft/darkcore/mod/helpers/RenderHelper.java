package io.darkcraft.darkcore.mod.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class RenderHelper
{
	public static void bindTexture(ResourceLocation rl)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(rl);
	}
}
