package io.darkcraft.darkcore.mod.multiblock;

public interface IMultiBlockStructure
{
	/**
	 * @return a BlockState[][][] which contains the definition of the multiblock. Null is assumed to be air. All 4 possible rotations should be tried
	 */
	public IBlockState[][][] getStructureDefinition();

	/**
	 * These 3 methods should return the X, Y and Z coordinates of the multiblock core in the structure definition above. Structure will be accessed in [x][y][z] order where z is assumed to be height.
	 */
	public int getCoreX();

	public int getCoreY();

	public int getCoreZ();
}
