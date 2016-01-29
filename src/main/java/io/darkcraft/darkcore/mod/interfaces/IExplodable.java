package io.darkcraft.darkcore.mod.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import net.minecraft.world.Explosion;

/**
 * Implement on {@linkplain io.darkcraft.darkcore.mod.abstracts.AbstractBlock AbstractBlock}
 * or a TileEntity contained within {@linkplain io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer AbstractBlockContainer} to have
 * it respond to explosions.
 * @author dark
 *
 */
public interface IExplodable
{
	/**
	 * @param pos the position of the block which is being exploded
	 * @param explosion the explosion which it is being affected by
	 */
	public void explode(SimpleCoordStore pos, Explosion explosion);
}
