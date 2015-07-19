package io.darkcraft.darkcore.mod.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import net.minecraft.world.Explosion;

public interface IExplodable
{
	public void explode(SimpleCoordStore pos, Explosion explosion);
}
