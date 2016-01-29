package io.darkcraft.darkcore.mod.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

/**
 * Implement on an AbstractBlock or AbstractBlockContainer to make your blocks colourable.<br>
 * Can't be used at the same time as block subnames.
 * @author dark
 *
 */
public interface IColorableBlock
{
	/**
	 * See {@link io.darkcraft.darkcore.mod.helpers.BlockIterator} for examples, such as {@link io.darkcraft.darkcore.mod.helpers.BlockIterator#sameExcMetaNS}
	 * @param coord the coordinates of the
	 * @return
	 */
	public IBlockIteratorCondition getColoringIterator(SimpleCoordStore coord);
}
