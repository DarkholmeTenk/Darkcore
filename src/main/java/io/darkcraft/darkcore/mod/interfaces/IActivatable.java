package io.darkcraft.darkcore.mod.interfaces;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement on a TileEntity contained within {@linkplain io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer AbstractBlockContainer} to have
 * it respond to right clicks.
 * @author dark
 *
 */
public interface IActivatable
{
	/**
	 * Called when your TileEntity is right clicked
	 * @param ent the player doing the right clicking
	 * @param side the side of the block they right clicked on
	 * @return true to cancel further processing
	 */
	public boolean activate(EntityPlayer ent, int side);
}
