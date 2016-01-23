package io.darkcraft.darkcore.mod.client.gui.interfaces;

import io.darkcraft.darkcore.mod.client.gui.GuiObject;

import java.util.List;

public interface IGuiObjectContainer
{
	/**
	 * @return a list of all the children this object contains
	 */
	public List<GuiObject> getChildren();

	/**
	 * Add a child to the gui object to be displayed however
	 * @param child the child gui object to add to the container
	 * @return true if child was added, false otherwise
	 */
	public boolean addChild(GuiObject child);
}
