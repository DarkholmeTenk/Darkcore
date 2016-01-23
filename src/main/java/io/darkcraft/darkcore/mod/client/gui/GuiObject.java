package io.darkcraft.darkcore.mod.client.gui;

import net.minecraft.client.renderer.Tessellator;

public abstract class GuiObject
{
	public abstract void render(Tessellator tess, int zLevel);
}
