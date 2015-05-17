package io.darkcraft.darkcore.mod.interfaces;

import net.minecraft.block.Block;

/**
 * Interface to apply to TileEntities whose block extends AbstractBlock which need to be notified of a block update which happens next to them.
 * 
 * @author DarkholmeTenk
 */
public interface IBlockUpdateDetector
{
	public void blockUpdated(Block updatedBlock);
}
