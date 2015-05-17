package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockCore;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockPart;
import io.darkcraft.darkcore.mod.multiblock.BlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiBlockHelper
{
	private static ForgeDirection[]	rotDirs	= new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST };

	public static boolean isMultiblockValid(TileEntity core, IMultiBlockStructure structure)
	{
		boolean valid = false;
		for (ForgeDirection dir : rotDirs)
		{
			valid = valid || isMultiblockValid(dir, core, structure);
		}
		return valid;
	}

	private static boolean isMultiblockValid(ForgeDirection dir, TileEntity core, IMultiBlockStructure structure)
	{
		World w = core.getWorldObj();
		int x = core.xCoord;
		int y = core.yCoord;
		int z = core.zCoord;
		BlockState[][][] blockStates = structure.getStructureDefinition();
		int xO = dir.offsetX;
		int zO = dir.offsetZ;
		int xToCheck = x;
		int zToCheck = z;
		for (int yI = 0; yI < blockStates.length; yI++)
		{
			int yToCheck = y + (yI - structure.getCoreY());
			BlockState[][] row = blockStates[yI];
			if (row == null) continue;
			for (int xI = 0; xI < row.length; xI++)
			{
				BlockState[] cells = row[xI];
				if (cells == null) continue;
				for (int zI = 0; zI < cells.length; zI++)
				{
					BlockState cell = cells[zI];
					if (cell != null)
					{
						zToCheck = (z + xO) != 0 ? xO * (structure.getCoreZ() - zI) : zO * (structure.getCoreX() - xI);
						xToCheck = (x + xO) != 0 ? xO * (structure.getCoreX() - xI) : zO * (structure.getCoreZ() - zI);
						if (!cell.equals(w, xToCheck, yToCheck, zToCheck)) { return false; }

					}
				}
			}
		}
		for (int yI = 0; yI < blockStates.length; yI++)
		{
			int yToCheck = y + (yI - structure.getCoreY());
			BlockState[][] row = blockStates[yI];
			if (row == null) continue;
			for (int xI = 0; xI < row.length; xI++)
			{
				BlockState[] cells = row[xI];
				if (cells == null) continue;
				for (int zI = 0; zI < cells.length; zI++)
				{
					BlockState cell = cells[zI];
					if (cell != null)
					{
						zToCheck = (z + xO) != 0 ? xO * (structure.getCoreZ() - zI) : zO * (structure.getCoreX() - xI);
						xToCheck = (x + xO) != 0 ? xO * (structure.getCoreX() - xI) : zO * (structure.getCoreZ() - zI);
						TileEntity te = w.getTileEntity(xToCheck, yToCheck, zToCheck);
						if (te instanceof IMultiBlockPart) ((IMultiBlockPart) te).setMultiBlockCore((IMultiBlockCore) core);

					}
				}
			}
		}

		return true;
	}

	public static void generateStructure(IMultiBlockStructure structure, World w, SimpleCoordStore pos, ForgeDirection dir)
	{
		int x = pos.x;
		int y = pos.y;
		int z = pos.z;
		BlockState[][][] blockStates = structure.getStructureDefinition();
		int xO = dir.offsetX;
		int zO = dir.offsetZ;
		int xToCheck = x;
		int zToCheck = z;
		for (int yI = 0; yI < blockStates.length; yI++)
		{
			int yToCheck = y + (yI - structure.getCoreY());
			BlockState[][] row = blockStates[yI];
			if (row == null) continue;
			for (int xI = 0; xI < row.length; xI++)
			{
				if (xO != 0)
					xToCheck = (x + (xO * (structure.getCoreX() - xI)));
				else
					zToCheck = (z + (zO * (structure.getCoreX() - xI)));
				BlockState[] cells = row[xI];
				if (cells == null) continue;
				for (int zI = 0; zI < cells.length; zI++)
				{
					BlockState cell = cells[zI];
					if (cell != null)
					{
						if (xO != 0)
							zToCheck = (z + (xO * (structure.getCoreZ() - zI)));
						else
							xToCheck = (x + (zO * (structure.getCoreZ() - zI)));
						w.setBlock(xToCheck, yToCheck, zToCheck, cell.b, cell.m == -1 ? 0 : cell.m, 3);
					}
				}
			}
		}
	}

	public static void generateAtFloor(IMultiBlockStructure structure, World w, SimpleCoordStore pos, ForgeDirection dir)
	{
		boolean found = false;
		BlockState[][] floor = structure.getStructureDefinition()[structure.getCoreY()];
		for (int y = pos.y; (y > 1) && !found; y--)
		{
			boolean xfound = true;
			for (int x = 0; (x < floor.length) && xfound; x++)
			{
				BlockState[] row = floor[x];
				if (row == null) continue;
				for (int z = 0; (z < row.length) && xfound; z++)
				{
					BlockState cell = row[z];
					if (cell == null) continue;
					int xC = pos.x + (dir.offsetX * (x - structure.getCoreX())) + (dir.offsetZ * (z - structure.getCoreZ()));
					int zC = pos.z + (dir.offsetZ * (x - structure.getCoreX())) + (dir.offsetX * (z - structure.getCoreZ()));
					if (w.isAirBlock(xC, y - 1, zC))
					{
						xfound = false;
						continue;
					}
					Block b = w.getBlock(xC, y - 1, zC);
					if (b.isAir(w, xC, y - 1, zC) || b.isFoliage(w, xC, y, zC) || !b.isNormalCube(w, xC, y - 1, zC)) xfound = false;
				}
			}
			if (xfound)
			{
				generateStructure(structure, w, new SimpleCoordStore(w, pos.x, y, pos.z), dir);
				found = true;
			}
		}
	}

	public static boolean doesCoreExist(IMultiBlockCore core)
	{
		SimpleCoordStore scs = core.getCoords();
		World w = scs.getWorldObj();
		int x = scs.x;
		int y = scs.y;
		int z = scs.z;
		TileEntity te = w.getTileEntity(x, y, z);
		if (te instanceof IMultiBlockCore)
		{
			if (te.equals(core)) return true;
		}
		return false;
	}
}
