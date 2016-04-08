package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.multiblock.BlockState;
import io.darkcraft.darkcore.mod.multiblock.IBlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockCore;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockPart;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;

import java.util.HashSet;

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
		IBlockState[][][] blockStates = structure.getStructureDefinition();
		int xO = dir.offsetX;
		int zO = dir.offsetZ;
		int xToCheck = x;
		int zToCheck = z;
		HashSet<IMultiBlockPart> partSet = new HashSet<IMultiBlockPart>();
		for (int yI = 0; yI < blockStates.length; yI++)
		{
			int yToCheck = y + (yI - structure.getCoreY());
			IBlockState[][] row = blockStates[yI];
			if (row == null) continue;
			for (int xI = 0; xI < row.length; xI++)
			{
				IBlockState[] cells = row[xI];
				if (cells == null) continue;
				for (int zI = 0; zI < cells.length; zI++)
				{
					IBlockState cell = cells[zI];
					if (cell != null)
					{
						zToCheck = z + (xO != 0 ? xO * (structure.getCoreZ() - zI) : zO * (structure.getCoreX() - xI));
						xToCheck = x + (xO != 0 ? xO * (structure.getCoreX() - xI) : zO * (structure.getCoreZ() - zI));
						if (!cell.equals(w, xToCheck, yToCheck, zToCheck))
						{
							return false;
						}
						TileEntity te = w.getTileEntity(xToCheck, yToCheck, zToCheck);
						if(te instanceof IMultiBlockPart)
							partSet.add((IMultiBlockPart) te);
					}
				}
			}
		}
		if(core instanceof IMultiBlockCore)
			for(IMultiBlockPart p : partSet)
				p.setMultiBlockCore((IMultiBlockCore) core);

		return true;
	}

	public static void generateStructure(IMultiBlockStructure structure, World w, SimpleCoordStore pos, ForgeDirection dir)
	{
		int x = pos.x;
		int y = pos.y;
		int z = pos.z;
		IBlockState[][][] blockStates = structure.getStructureDefinition();
		int xO = dir.offsetX;
		int zO = dir.offsetZ;
		int xToCheck = x;
		int zToCheck = z;
		for (int yI = 0; yI < blockStates.length; yI++)
		{
			int yToCheck = y + (yI - structure.getCoreY());
			IBlockState[][] row = blockStates[yI];
			if (row == null) continue;
			for (int xI = 0; xI < row.length; xI++)
			{
				if (xO != 0)
					xToCheck = (x + (xO * (structure.getCoreX() - xI)));
				else
					zToCheck = (z + (zO * (structure.getCoreX() - xI)));
				IBlockState[] cells = row[xI];
				if (cells == null) continue;
				for (int zI = 0; zI < cells.length; zI++)
				{
					IBlockState cell = cells[zI];
					if (cell != null)
					{
						if (xO != 0)
							zToCheck = (z + (xO * (structure.getCoreZ() - zI)));
						else
							xToCheck = (x + (zO * (structure.getCoreZ() - zI)));
						cell.set(w, xToCheck, yToCheck, zToCheck);
					}
				}
			}
		}
	}

	public static void generateAtFloor(IMultiBlockStructure structure, World w, SimpleCoordStore pos, ForgeDirection dir)
	{
		boolean found = false;
		IBlockState[][] floor = structure.getStructureDefinition()[structure.getCoreY()];
		for (int y = pos.y; (y > 1) && !found; y--)
		{
			boolean xfound = true;
			for (int x = 0; (x < floor.length) && xfound; x++)
			{
				IBlockState[] row = floor[x];
				if (row == null) continue;
				for (int z = 0; (z < row.length) && xfound; z++)
				{
					IBlockState cell = row[z];
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

	public static IMultiBlockStructure getSingleBlockStructure(Block b)
	{
		return new SingleBlockStructure(b);
	}

	private static class SingleBlockStructure implements IMultiBlockStructure
	{
		private final Block b;
		private final IBlockState[][][] s;
		public SingleBlockStructure(Block _b)
		{
			b = _b;
			s = new IBlockState[][][]{{{new BlockState(b)}}};
		}
		@Override
		public IBlockState[][][] getStructureDefinition(){return s;}
		@Override
		public int getCoreX(){return 0;}
		@Override
		public int getCoreY(){return 0;}
		@Override
		public int getCoreZ(){return 0;}
	}
}
