package io.darkcraft.darkcore.mod.client.gui.interfaces;

public interface IMouseOver
{
	/**
	 * Called when the object is moused over
	 * @param x the x position of the cursor relative to object root
	 * @param y the y position of the cursor relative to object root
	 * @return true if this object is being moused over, preventing the event from propogating to the parent
	 */
	public boolean mouseOver(int x, int y);
}
