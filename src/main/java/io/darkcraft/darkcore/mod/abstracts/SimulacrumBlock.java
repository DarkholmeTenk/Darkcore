package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.interfaces.IBlockIteratorCondition;
import io.darkcraft.darkcore.mod.interfaces.IColorableBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SimulacrumBlock extends AbstractBlock
{
	protected final AbstractBlock sim;

	public SimulacrumBlock(String modName, AbstractBlock simulating)
	{
		super(modName);
		sim = simulating;
		setBlockName(sim.getUnlocalizedNameForIcon() + ".Simulacrum");
		opaque = isOpaqueCube();
		setLightLevel(sim.getLightValue());
		setLightOpacity(sim.getLightOpacity());
	}

	@Override
	public void initData()
	{
	}

	@Override
	public void initRecipes()
	{
	}

	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer pl, int s, float i, float j, float k)
	{
		return sim.onBlockActivated(w, x, y, z, pl, s, i, j, k);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		return sim.getIcon(side, metadata);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
	}

	@Override
	public int getNumSubNames()
	{
		return sim.getNumSubNames();
	}

	@Override
	public String getSubName(int num)
	{
		return sim.getSubName(num);
	}

	@Override
	public boolean colorBlock(World w, int x, int y, int z, EntityPlayer pl, IBlockIteratorCondition cond, ItemStack is, int color, int depth)
	{
		return sim.colorBlock(w, x, y, z, pl, cond, is, color, depth);
	}

	@Override
	public int damageDropped(int damage)
	{
		return this instanceof IColorableBlock ? 15 : damage;
	}

	@Override
	public Class<? extends AbstractItemBlock> getIB()
	{
		return SimulacrumItemBlock.class;
	}

	@Override
	public boolean isOpaqueCube()
	{
		if(sim != null)
			return sim.isOpaqueCube();
		return true;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess w, int s, int x, int y, int z, int ox, int oy, int oz)
	{
		Block a = Block.blocksList[w.getBlockId(x, y, z)];
		Block b = w.getBlock(ox, oy, oz);
		if(a == b)
		{
			int ma = w.getBlockMetadata(x, y, z);
			int mb = w.getBlockMetadata(ox, oy, oz);
			if((ma == mb) && !a.isOpaqueCube())
				return false;
		}
		return super.shouldSideBeRendered(w, s, x, y, z, ox, oy, oz);
	}
}
