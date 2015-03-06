package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockCore;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockPart;
import io.darkcraft.darkcore.mod.multiblock.BlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiBlockHelper
{
	private static ForgeDirection[] rotDirs = new ForgeDirection[]{
			ForgeDirection.NORTH,ForgeDirection.EAST,
			ForgeDirection.SOUTH,ForgeDirection.WEST};
	public static boolean isMultiblockValid(TileEntity core, IMultiBlockStructure structure)
	{
		//if(!(core instanceof IMultiBlockCore))
		//	return false;
		//System.out.println("[MBH]Checking if multiblock is valid!");
		boolean valid = false;
		for(ForgeDirection dir : rotDirs)
		{
			//System.out.println("Checking dir " + dir.toString());
			valid = valid || isMultiblockValid(dir,core,structure);
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
		for(int xI = 0; xI<blockStates.length; xI++)
		{
			if(xO != 0)
				xToCheck = (x + (xO*(structure.getCoreX() - xI)));
			else
				zToCheck = (z + (zO*(structure.getCoreX() - xI)));
			BlockState[][] row = blockStates[xI];
			for(int yI = 0; yI < row.length; yI ++)
			{
				int yToCheck = y + (yI - structure.getCoreY());
				BlockState[] cells = row[yI];
				for(int zI = 0; zI < cells.length; zI++)
				{
					BlockState cell = cells[zI];
					if(cell != null)
					{
						if(xO != 0)
							zToCheck = (z + (xO*(structure.getCoreZ() - zI)));
						else
							xToCheck = (x + (zO*(structure.getCoreZ() - zI)));
						//System.out.println("Block at "+xToCheck +","+yToCheck+","+zToCheck);
						if(!cell.equals(w, xToCheck, yToCheck, zToCheck))
						{
							return false;
						}
						
					}
				}
			}
		}
		
		for(int xI = 0; xI<blockStates.length; xI++)
		{
			if(xO != 0)
				xToCheck = (x + (xO*(structure.getCoreX() - xI)));
			else
				zToCheck = (z + (zO*(structure.getCoreX() - xI)));
			BlockState[][] row = blockStates[xI];
			for(int yI = 0; yI < row.length; yI ++)
			{
				int yToCheck = y + (yI - structure.getCoreY());
				BlockState[] cells = row[yI];
				for(int zI = 0; zI < cells.length; zI++)
				{
					BlockState cell = cells[zI];
					if(cell != null)
					{
						if(xO != 0)
							zToCheck = (z + (xO*(structure.getCoreZ() - zI)));
						else
							xToCheck = (x + (zO*(structure.getCoreZ() - zI)));
						TileEntity te = w.getTileEntity(xToCheck, yToCheck, zToCheck);
						if(te instanceof IMultiBlockPart)
							((IMultiBlockPart)te).setMultiBlockCore((IMultiBlockCore)core);
					}
				}
			}
		}
		return true;
	}
	
	public static boolean doesCoreExist(IMultiBlockCore core)
	{
		SimpleCoordStore scs = core.getCoords();
		World w = scs.getWorldObj();
		int x = scs.x;
		int y = scs.y;
		int z = scs.z;
		TileEntity te = w.getTileEntity(x, y, z);
		if(te instanceof IMultiBlockCore)
		{
			if(te.equals(core))
				return true;
		}
		return false;
	}
}
