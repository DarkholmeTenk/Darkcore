package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.interfaces.IColorableBlock;
import io.darkcraft.darkcore.mod.interfaces.IExplodable;
import io.darkcraft.darkcore.mod.multiblock.BlockState;

import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractBlock extends Block
{
	private IIcon				iconBuffer			= null;
	private String				unlocalizedFragment	= "";
	private String[]			subNames			= null;
	private IIcon[]				subIcons			= null;
	protected static IIcon		blankIcon			= null;
	private boolean				renderIcon;
	private String				sm;
	public static int[]			colorArray			= new int[] { 1973019, 11743532, 3887386, 10583369, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };

	private static ConfigFile	config				= null;
	private static boolean		coloredUsesDye		= false;
	private static int			maxColorSpread		= 16;
	private static boolean		colorSpreadDiagonal	= false;

	public static void refreshConfigs()
	{
		if (config == null) config = DarkcoreMod.configHandler.registerConfigNeeder("AbstractBlock");
		coloredUsesDye = config.getBoolean("Colored blocks use up dye", false, "If true, when dying a colored block, the dye will be used up.", "If false, the dye will not be used up");
		maxColorSpread = config.getInt("Max color spread", 16, "How many blocks away from the block you sneak right click should colors spread");
		colorSpreadDiagonal = config.getBoolean("Color spread diagonal", false, "Can colors spread diagonally");
	}

	public AbstractBlock(Material blockMaterial, String sourcemod)
	{
		super(blockMaterial);
		sm = sourcemod;
		CreativeTabs tab = DarkcoreMod.getCreativeTab(sourcemod);
		if (tab != null) setCreativeTab(tab);
		setResistance(6000000.0F);
		setHardness(-1.0f);
		initData();
		renderIcon = true;
		if (this instanceof IColorableBlock)
			setSubNames(ItemDye.field_150923_a);
		else if (subNames == null) setIconArray(1);
	}

	public AbstractBlock(boolean render, String sm)
	{
		this(sm);
		renderIcon = render;
	}

	public AbstractBlock(String sm)
	{
		this(Material.iron, sm);
	}

	public AbstractBlock register()
	{
		Class<? extends AbstractItemBlock> aib = getIB();
		if (aib != null)
			GameRegistry.registerBlock(this, aib, getUnlocalizedName());
		else
			GameRegistry.registerBlock(this, getUnlocalizedName());
		if (this instanceof AbstractBlockContainer) GameRegistry.registerTileEntity(((AbstractBlockContainer) this).getTEClass(), this.getUnlocalizedName());

		return this;
	}

	public Class<? extends AbstractItemBlock> getIB()
	{
		return null;
	}

	public BlockState getBlockState()
	{
		return new BlockState(this, -1);
	}

	public BlockState getBlockState(int m)
	{
		return new BlockState(this, m);
	}

	public ItemStack getIS(int am, int dam)
	{
		return new ItemStack(this, am, dam);
	}

	public void setSubNames(String... subnames)
	{
		if (subnames.length == 0) subNames = null;
		subNames = subnames;
		if (!(this instanceof IColorableBlock)) setIconArray(subNames.length);
	}

	private void setIconArray(int length)
	{
		setIconArray(length, getIconSuffixes());
	}

	public void setIconArray(int names, int suffixes)
	{
		subIcons = new IIcon[names * suffixes];
	}

	public String[] getIconSuffix(int damage)
	{
		return getIconSuffix();
	}

	public String[] getIconSuffix()
	{
		return null;
	}

	public int getIconSuffixes()
	{
		String[] suffixes = getIconSuffix();
		if (suffixes == null) return 1;
		return suffixes.length;
	}

	@Override
	public int damageDropped(int damage)
	{
		if (subNames != null) return MathHelper.clamp(damage, 0, subNames.length - 1);
		return 0;
	}

	public int getNumSubNames()
	{
		if (subNames == null) return 0;
		return subNames.length;
	}

	public String getSubName(int num)
	{
		if (subNames == null) return null;
		if ((num >= 0) && (num < subNames.length)) return subNames[num];
		return "Malformed";
	}

	@Override
	public Block setBlockName(String par1Str)
	{
		unlocalizedFragment = par1Str;
		return super.setBlockName(par1Str);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "tile." + sm + "." + unlocalizedFragment;
	}

	public String getUnlocalizedNameForIcon()
	{
		return unlocalizedFragment;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item itemID, CreativeTabs tab, List itemList)
	{
		int numItems;
		if (this instanceof IColorableBlock)
			itemList.add(new ItemStack(itemID, 1, 15));
		else
		{
			numItems = Math.max(1, getNumSubNames());
			for (int i = 0; i < numItems; i++)
			{
				itemList.add(new ItemStack(itemID, 1, i));
			}
		}
	}

	public String getUnlocalizedName(int damage)
	{
		int numSubnames = getNumSubNames();
		if ((numSubnames == 0) || (this instanceof IColorableBlock))
			return "tile." + sm + "." + unlocalizedFragment;
		else
		{
			return "tile." + sm + "." + unlocalizedFragment + "." + getSubName(damage);
		}
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return false;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		if (blankIcon == null) blankIcon = register.registerIcon(sm + ":blank");
		if (!renderIcon) return;
		if ((subIcons != null) && !(this instanceof IColorableBlock))
		{
			int suffixCount = getIconSuffixes();
			if (subNames != null)
			{
				for (int i = 0; i < subNames.length; i++)
				{
					String[] suffixes = getIconSuffix(i);
					if (suffixes == null)
					{
						subIcons[i * suffixCount] = register.registerIcon(sm + ":" + getUnlocalizedNameForIcon() + "." + subNames[i]);
					}
					else
					{
						for (int j = 0; j < suffixes.length; j++)
						{
							String iconToReg = sm + ":" + getUnlocalizedNameForIcon() + "." + subNames[i] + "." + suffixes[j];
							if (DarkcoreMod.debugText) System.out.println("[AB]Registering " + iconToReg + " in slot " + ((i * suffixCount) + j));
							subIcons[(i * suffixCount) + j] = register.registerIcon(iconToReg);
						}
					}
				}
			}
			else
			{
				String[] suffixes = getIconSuffix();
				if (suffixes == null)
				{
					subIcons[0] = register.registerIcon(sm + ":" + getUnlocalizedNameForIcon());
				}
				else
				{
					for (int j = 0; j < suffixes.length; j++)
					{
						String iconToReg = sm + ":" + getUnlocalizedNameForIcon() + "." + suffixes[j];
						subIcons[j] = register.registerIcon(iconToReg);
					}
				}
			}
		}
		else
			iconBuffer = register.registerIcon(sm + ":" + getUnlocalizedNameForIcon());
	}

	@Override
	public boolean isOpaqueCube()
	{
		return renderIcon;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int s)
	{
		if (!renderIcon) return false;
		int mX = x;
		int mY = y;
		int mZ = z;
		switch (s)
		{
			case 0:
				y++;
				break;
			case 1:
				y--;
				break;
			case 2:
				z++;
				break;
			case 3:
				z--;
				break;
			case 4:
				x++;
				break;
			case 5:
				x--;
				break;
		}
		return shouldSideBeRendered(w, s, x, y, z, mX, mY, mZ);
	}

	public boolean shouldSideBeRendered(IBlockAccess w, int s, int x, int y, int z, int ox, int oy, int oz)
	{
		return super.shouldSideBeRendered(w, ox, oy, oz, s);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if (!renderIcon) return blankIcon;
		int suffixCount = getIconSuffixes();
		if ((subIcons != null) && !(this instanceof IColorableBlock))
		{
			String[] suffixes = getIconSuffix(metadata);
			if (suffixes == null)
			{
				if ((metadata >= 0) && (metadata < subIcons.length)) return subIcons[metadata * suffixCount];
				return subIcons[0];
			}
			else
			{
				int metaBase = metadata * suffixCount;
				int metaAdd = 0;
				for (int j = 0; j < suffixes.length; j++)
				{
					if (suffixes[j].contains("top") && (side == 1))
						return subIcons[metaBase + j];
					else if (suffixes[j].contains("bottom") && (side == 0))
						metaAdd = j;
					else if (suffixes[j].contains("side") && (side > 1)) metaAdd = j;
				}
				return subIcons[metaBase + metaAdd];
			}
		}
		else
		{
			return iconBuffer;
		}
	}

	@Override
	public void onPostBlockPlaced(World w, int x, int y, int z, int m)
	{
		w.scheduleBlockUpdate(x, y, z, this, 1);
	}

	@Override
	public int getMobilityFlag()
	{
		return 2;
	}

	@Override
	public int colorMultiplier(IBlockAccess w, int x, int y, int z)
	{
		if (!(this instanceof IColorableBlock)) return super.colorMultiplier(w, x, y, z);
		int m = w.getBlockMetadata(x, y, z);
		return colorArray[m];
	}

	@Override
	public MapColor getMapColor(int p_149728_1_)
	{
		if (!(this instanceof IColorableBlock)) return super.getMapColor(p_149728_1_);
		return MapColor.getMapColorForBlockColored(p_149728_1_);
	}

	protected boolean colorBlock(World w, int x, int y, int z, EntityPlayer pl, ItemStack is, int color, int depth)
	{
		if(depth >= maxColorSpread) return false;
		int oldMeta = w.getBlockMetadata(x, y, z);
		w.setBlockMetadataWithNotify(x, y, z, color, 3);
		if ((oldMeta != color) && (depth == 0)) return true;
		if (colorSpreadDiagonal)
		{
			topLevel: for (int xo = -1; xo <= 1; xo++)
			{
				for (int yo = -1; yo <= 1; yo++)
				{
					for (int zo = -1; zo <= 1; zo++)
					{
						if ((xo == 0) && (yo == 0) && (zo == 0)) continue;
						if (is.stackSize == 0) break topLevel;
						if ((w.getBlock(x + xo, y + yo, z + zo) != this) || (w.getBlockMetadata(x + xo, y + yo, z + zo) != oldMeta)) continue;
						if (coloredUsesDye && !pl.capabilities.isCreativeMode) is.stackSize--;
						colorBlock(w, x + xo, y + yo, z + zo, pl, is, color, depth+1);
					}
				}
			}
		}
		else
		{
			int loopDepth = 0;
			HashSet<SimpleCoordStore> done = new HashSet<SimpleCoordStore>();
			HashSet<SimpleCoordStore> toDo = new HashSet<SimpleCoordStore>();
			toDo.add(new SimpleCoordStore(w,x,y,z));
			mainLoop:
			while((loopDepth++ < maxColorSpread) && !toDo.isEmpty())
			{
				HashSet<SimpleCoordStore> buffer = new HashSet<SimpleCoordStore>();
				for(SimpleCoordStore pos : toDo)
				{
					done.add(pos);
					for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
					{
						SimpleCoordStore next = pos.getNearby(dir);
						if(done.contains(next)) continue;
						if((next.getBlock() != this) || (next.getMetadata() == color)) continue;
						buffer.add(next);
					}
					if (coloredUsesDye && !pl.capabilities.isCreativeMode) is.stackSize--;
					pos.setMetadata(color,3);
					if(is.stackSize == 0) break mainLoop;
				}
				toDo = buffer;
				buffer = new HashSet<SimpleCoordStore>();
			}
		}
		return true;
	}

	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer pl, int s, float i, float j, float k)
	{
		Block b = w.getBlock(x, y, z);
		if((b != this) && !(b instanceof SimulacrumBlock)) return false;
		if((b instanceof SimulacrumBlock) && (((SimulacrumBlock)b).sim != this)) return false;
		if (pl == null) return false;
		if (this instanceof IColorableBlock)
		{
			ItemStack is = pl.getHeldItem();
			if (is == null) return false;
			Item item = is.getItem();
			if (item instanceof ItemDye)
			{
				int md = is.getItemDamage();
				return colorBlock(w, x, y, z, pl, is, md, 0);
			}
		}
		return false;
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		return 0;
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{
		if(this instanceof IExplodable)
		{
			SimpleCoordStore scs = new SimpleCoordStore(world, x, y, z);
			((IExplodable)this).explode(scs,explosion);
		}
	}

	@Override
	public boolean canDropFromExplosion(Explosion p_149659_1_)
	{
		return false;
	}

	public abstract void initData();

	public abstract void initRecipes();

}
