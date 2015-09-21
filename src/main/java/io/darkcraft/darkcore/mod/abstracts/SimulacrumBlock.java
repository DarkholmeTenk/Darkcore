package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.interfaces.IColorableBlock;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SimulacrumBlock extends AbstractBlock implements IColorableBlock
{
	protected final AbstractBlock sim;

	public SimulacrumBlock(String modName, AbstractBlock simulating)
	{
		super(modName);
		sim = simulating;
		setBlockName(sim.getUnlocalizedNameForIcon() + ".Simulacrum");
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
	public boolean colorBlock(World w, int x, int y, int z, EntityPlayer pl, ItemStack is, int color, int depth)
	{
		return sim.colorBlock(w, x, y, z, pl, is, color, depth);
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
}
