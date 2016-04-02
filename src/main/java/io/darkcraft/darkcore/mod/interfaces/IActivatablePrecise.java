package io.darkcraft.darkcore.mod.interfaces;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement on {@linkplain io.darkcraft.darkcore.mod.abstracts.AbstractBlock AbstractBlock}
 * or a TileEntity contained within {@linkplain io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer AbstractBlockContainer} to have
 * it respond to right clicks.
 *
 * Prevents the onBlockActivated from returning false due to needing to send data to the server!
 * @author dark
 *
 */
public interface IActivatablePrecise
{
	/**
	 * Your thing has been right clicked
	 * @param ent the player doing the right clicking
	 * @param side the side they right clicked on
	 * @param x if on a TE, this is just 0-1 representing where they right clicked<br>
	 * if on a block, this is 0-1 + the x coord of the block
	 * @param y x if on a TE, this is just 0-1 representing where they right clicked<br>
	 * if on a block, this is 0-1 + the y coord of the block
	 * @param z x if on a TE, this is just 0-1 representing where they right clicked<br>
	 * if on a block, this is 0-1 + the z coord of the block
	 * @return true to cancel all further processing
	 */
	public boolean activate(EntityPlayer ent, int side, float x, float y, float z);
}
