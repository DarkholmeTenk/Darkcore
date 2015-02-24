package io.darkcraft.darkcore.mod.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import net.minecraft.world.ChunkCoordIntPair;

public interface IChunkLoader
{
	public boolean shouldChunkload();
	
	public SimpleCoordStore coords();
	
	public ChunkCoordIntPair[] loadable();
}
