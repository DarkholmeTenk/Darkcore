package io.darkcraft.darkcore.mod.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

public interface IBlockIteratorCondition
{
	/**
	 * This function will be called upon calling BlockIterator's next() function
	 * @param start the starting position for this blockIterator
	 * @param prevPosition the position that will be returned by the current instance of next()
	 * @param newPosition the position to potentially add to the queue
	 * @return true if newPosition should be added to the queue, false if not
	 */
	public boolean isValid(SimpleCoordStore start, SimpleCoordStore prevPosition, SimpleCoordStore newPosition);
}
