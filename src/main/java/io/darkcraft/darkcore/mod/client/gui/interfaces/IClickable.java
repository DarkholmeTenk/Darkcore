package io.darkcraft.darkcore.mod.client.gui.interfaces;

public interface IClickable
{
	/**
	 * Called on the object being clicked, by the parent object
	 * @param x the x position of the click, relative to the top left (0,0) of the object
	 * @param y the y position of the click, relative to the top left (0,0) of the object
	 * @return true if the click is valid and should not be propogated to the parent
	 */
	public boolean click(int x, int y);
}
