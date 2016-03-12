package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.darkcore.mod.interfaces.IBlockUpdateDetector;
import io.darkcraft.darkcore.mod.interfaces.IExplodable;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockPart;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public abstract class AbstractBlockContainer extends AbstractBlock implements ITileEntityProvider
{
	private boolean	dropWithData	= false;

	/**
	 * See {@link #AbstractBlockContainer(boolean, boolean, String)}
	 */
	public AbstractBlockContainer(String sm)
	{
		super(sm);
		isBlockContainer = true;
	}

	/**
	 * See {@link #AbstractBlockContainer(boolean, boolean, String)}
	 */
	public AbstractBlockContainer(boolean render, String sm)
	{
		super(render, sm);
		isBlockContainer = true;
	}

	/**
	 * @param visible whether the block should render
	 * @param _dropWithData whether Tile Entities contained by this block should keep their NBT data when the block is broken
	 * @param sm the modid of the mod this block belongs to
	 */
	public AbstractBlockContainer(boolean visible, boolean _dropWithData, String sm)
	{
		super(visible, sm);
		isBlockContainer = true;
		dropWithData = _dropWithData;
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		super.onBlockAdded(par1World, par2, par3, par4);
	}

	@Override
	public void breakBlock(World w, int x, int y, int z, Block par5, int par6)
	{
		TileEntity te = w.getTileEntity(x, y, z);
		if (te instanceof IMultiBlockPart) ((IMultiBlockPart) te).recheckCore();
		super.breakBlock(w, x, y, z, par5, par6);
		w.removeTileEntity(x, y, z);
	}

	@Override
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}

	/**
	 * If you want to add behaviour to this as a block, see {@link IActivatablePrecise}<br>
	 * If you want to add behaviour to the TE this block contains, see {@link IActivatable} or {@link IActivatablePrecise}
	 */
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer pl, int s, float i, float j, float k)
	{
		if (super.onBlockActivated(w, x, y, z, pl, s, i, j, k)) return true;
		if (this instanceof IActivatable) if (((IActivatable) this).activate(pl, s)) return true;
		TileEntity te = w.getTileEntity(x, y, z);
		if ((te instanceof IActivatable) && ((IActivatable) te).activate(pl, s)) return true;
		if (te instanceof IActivatablePrecise) return ((IActivatablePrecise)te).activate(pl, s, i, j, k);
		return false;
	}

	@Override
	protected void dropBlockAsItem(World w, int x, int y, int z, ItemStack is)
	{
		// do not drop items while restoring blockstates, prevents item dupe
		if (ServerHelper.isServer() && w.getGameRules().getGameRuleBooleanValue("doTileDrops") && !w.restoringBlockSnapshots)
		{
			if (dropWithData)
			{
				TileEntity te = w.getTileEntity(x, y, z);
				if (te != null)
				{
					NBTTagCompound nbt;
					if (is.stackTagCompound != null)
						nbt = is.stackTagCompound;
					else
						nbt = new NBTTagCompound();
					te.writeToNBT(nbt);
					is.stackTagCompound = nbt;
				}
			}
			if (captureDrops.get())
			{
				capturedDrops.get().add(is);
				return;
			}
			float f = 0.7F;
			double d0 = (w.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
			double d1 = (w.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
			double d2 = (w.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
			WorldHelper.dropItemStack(is, new SimpleDoubleCoordStore(w, x + d0, y + d1, z + d2));
		}
	}

	@Override
	public void onNeighborBlockChange(World w, int x, int y, int z, Block neighbourBlockID)
	{
		TileEntity te = w.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof IBlockUpdateDetector))
		{
			((IBlockUpdateDetector) te).blockUpdated(neighbourBlockID);
		}
	}

	public abstract Class<? extends TileEntity> getTEClass();

	/**
	 * If you want to add explosion behaviour to this block as a block or the TE this block contains, see {@link IExplodable}
	 */
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{
		super.onBlockExploded(world, x, y, z, explosion);
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof IExplodable)
		{
			SimpleCoordStore scs = new SimpleCoordStore(world, x, y, z);
			((IExplodable)te).explode(scs, explosion);
		}
	}
}
