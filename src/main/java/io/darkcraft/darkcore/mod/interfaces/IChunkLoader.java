package io.darkcraft.darkcore.mod.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

/**
 * Implement on your {@linkplain io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity AbstractTileEntity} to add chunkloading functionality
 * @author dark
 *
 */
public interface IChunkLoader
{
	/**
	 * @return whether or not the tile entity should chunkload
	 */
	public boolean shouldChunkload();

	/**
	 * The coords of the tile entity itself. Implemented by {@linkplain io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity AbstractTileEntity}.
	 */
	public SimpleCoordStore coords();

	/**
	 * Simplest return value is simply new ChunkCoordIntPair[]{coords().toChunkCoords()};
	 * @return An array of {@link ChunkCoordIntPair} which this chunk loader should keep active.<br>
	 */
	public ChunkCoordIntPair[] loadable();
}
