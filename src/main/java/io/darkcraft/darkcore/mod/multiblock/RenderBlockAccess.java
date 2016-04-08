package io.darkcraft.darkcore.mod.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderBlockAccess implements IBlockAccess
{
	private final IMultiBlockStructure struct;
	private final IBlockState[][][] s;
	private final TileEntity[][][] te;
	public RenderBlockAccess(IMultiBlockStructure _struct)
	{
		struct = _struct;
		s = struct.getStructureDefinition();
		te = new TileEntity[s.length][][];
		for(int y = 0; y < s.length; y++)
		{
			if(s[y] == null) continue;
			te[y] = new TileEntity[s[y].length][];
			for(int x = 0; x < s[y].length; x++)
			{
				if(s[y][x] == null) continue;
				te[y][x] = new TileEntity[s[y][x].length];
				for(int z = 0; z<s[y][x].length; z++)
				{
					if(s[y][x][z] == null) continue;
					Block b = s[y][x][z].getDefaultBlock();
					if(b instanceof ITileEntityProvider)
						te[y][x][z] = ((ITileEntityProvider)b).createNewTileEntity(null, s[y][x][z].getDefaultMeta());
					else
						te[y][x][z] = null;
				}
			}
		}
	}

	@Override
	public Block getBlock(int x, int y, int z)
	{
		if((y < 0) || (y >= s.length) || (s[y] == null)) return Blocks.air;
		if((x < 0) || (x >= s[y].length) || (s[y][x] == null)) return Blocks.air;
		if((z < 0) || (z >= s[y][x].length) || (s[y][x][z] == null)) return Blocks.air;
		return s[y][x][z].getDefaultBlock();
	}

	@Override
	public TileEntity getTileEntity(int x, int y, int z)
	{
		if((y < 0) || (y >= s.length) || (s[y] == null)) return null;
		if((x < 0) || (x >= s[y].length) || (s[y][x] == null)) return null;
		if((z < 0) || (z >= s[y][x].length) || (s[y][x][z] == null)) return null;
		return te[y][x][z];
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_)
	{
		return 15728704;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z)
	{
		if((y < 0) || (y >= s.length) || (s[y] == null)) return 0;
		if((x < 0) || (x >= s[y].length) || (s[y][x] == null)) return 0;
		if((z < 0) || (z >= s[y][x].length) || (s[y][x][z] == null)) return 0;
		return s[y][x][z].getDefaultMeta();
	}

	@Override
	public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_)
	{
		return 0;
	}

	@Override
	public boolean isAirBlock(int x, int y, int z)
	{
		if((y < 0) || (y >= s.length) || (s[y] == null)) return true;
		if((x < 0) || (x >= s[y].length) || (s[y][x] == null)) return true;
		if((z < 0) || (z >= s[y][x].length) || (s[y][x][z] == null)) return true;
		return s[y][x][z] instanceof AirBlockState;
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_)
	{
		return BiomeGenBase.plains;
	}

	@Override
	public int getHeight()
	{
		return 0;
	}

	@Override
	public boolean extendedLevelsInChunkCache()
	{
		return false;
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
	{
		Block block = getBlock(x, y, z);
		if(block == Blocks.air) return _default;
        return block.isSideSolid(this, x, y, z, side);
	}

}
