package io.darkcraft.darkcore.mod.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;

public abstract class Gui extends GuiScreen
{
	private List<GuiObject> children = new ArrayList();
}
