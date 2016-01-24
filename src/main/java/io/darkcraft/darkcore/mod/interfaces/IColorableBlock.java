package io.darkcraft.darkcore.mod.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

public interface IColorableBlock
{
	public IBlockIteratorCondition getColoringIterator(SimpleCoordStore coord);
}
