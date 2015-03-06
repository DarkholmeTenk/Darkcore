package io.darkcraft.darkcraft.mod.common.tileent;

import net.minecraft.entity.player.EntityPlayer;
import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MultiBlockHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockCore;
import io.darkcraft.darkcore.mod.multiblock.BlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import io.darkcraft.darkcraft.mod.DarkcraftMod;

public class MultiBlockCoreTE extends AbstractTileEntity implements IMultiBlockCore, IMultiBlockStructure, IActivatable
{
	boolean validMB = false;

	@Override
	public boolean isValid()
	{
		return validMB;
	}

	@Override
	public void recheckValidity()
	{
		//System.out.println("Checking MB VALID");
		validMB = MultiBlockHelper.isMultiblockValid(this, this);
	}

	
	@Override
	public void activate(EntityPlayer ent, int side)
	{
		recheckValidity();
		System.out.println("I am @ " + xCoord +","+yCoord+","+zCoord);
		System.out.println("Valid mb :" + validMB);
	}

	private static BlockState part = new BlockState(DarkcraftMod.multiBlockBaseBlock,-1);
	private static BlockState core = new BlockState(DarkcraftMod.multiBlockCoreBlock,-1);
	private static BlockState[][][] struct = new BlockState[][][]{
		{{null,null,null,null,null},{null,null,part,null,null}},
		{{part,null,part,null,part},{null,part,core,part,null}},
		{{null,null,null,null,null},{null,null,null,null,null}}
	};
	
	@Override
	public BlockState[][][] getStructureDefinition()
	{
		return struct;
	}

	@Override
	public int getCoreX()
	{
		return 1;
	}

	@Override
	public int getCoreY()
	{
		return 1;
	}

	@Override
	public int getCoreZ()
	{
		return 2;
	}

	@Override
	public SimpleCoordStore getCoords()
	{
		return new SimpleCoordStore(this);
	}

}
