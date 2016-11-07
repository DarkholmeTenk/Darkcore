package io.darkcraft.darkcore.mod.helpers;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.multiblock.IBlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import io.darkcraft.darkcore.mod.multiblock.RenderBlockAccess;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderHelper
{
	public static final Colour white = new Colour(1,1,1);
	public static final Colour black = new Colour(0,0,0);
	private static RenderBlocks rb = new RenderBlocks();
	private static FontRenderer fr;

	public static void bindTexture(ResourceLocation rl)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(rl);
	}

	public static void face(float x, float y, float z, float X, float Z, UVStore uv, boolean draw)
	{
		Tessellator tess = Tessellator.instance;
		if(draw)
			tess.startDrawingQuads();
		tess.addVertexWithUV(x, y, z, uv.u, uv.v);
		tess.addVertexWithUV(x, y, Z, uv.u, uv.V);
		tess.addVertexWithUV(X, y, Z, uv.U, uv.V);
		tess.addVertexWithUV(X, y, z, uv.U, uv.v);
		if(draw)
			tess.draw();
	}

	public static void uiFace(float x, float y, float w, float h, float zLevel, UVStore uv, boolean draw)
	{
		Tessellator tess = Tessellator.instance;
		if(draw)
			tess.startDrawingQuads();
		tess.addVertexWithUV(x, y, zLevel, uv.u, uv.v);
		tess.addVertexWithUV(x, y+h, zLevel, uv.u, uv.V);
		tess.addVertexWithUV(x+w, y+h, zLevel, uv.U, uv.V);
		tess.addVertexWithUV(x+w, y, zLevel, uv.U, uv.v);
		if(draw)
			tess.draw();
	}

	private static UVStore uvf(UVStore[] uvs, int i)
	{
		if((uvs == null) || (uvs.length == 0)) return UVStore.defaultUV;
		return uvs[i % uvs.length];
	}

	public static void cube(float x, float y, float z, float X, float Y, float Z, boolean draw, UVStore... uv)
	{
		Tessellator tess = Tessellator.instance;
		if(draw)
			tess.startDrawingQuads();
		GL11.glEnable(GL11.GL_CULL_FACE);
		tess.addVertexWithUV(X, y, z, uvf(uv,0).U, uvf(uv,0).v);
		tess.addVertexWithUV(X, y, Z, uvf(uv,0).U, uvf(uv,0).V);
		tess.addVertexWithUV(x, y, Z, uvf(uv,0).u, uvf(uv,0).V);
		tess.addVertexWithUV(x, y, z, uvf(uv,0).u, uvf(uv,0).v);
		tess.addVertexWithUV(x, Y, z, uvf(uv,1).u, uvf(uv,1).v);
		tess.addVertexWithUV(x, Y, Z, uvf(uv,1).u, uvf(uv,1).V);
		tess.addVertexWithUV(X, Y, Z, uvf(uv,1).U, uvf(uv,1).V);
		tess.addVertexWithUV(X, Y, z, uvf(uv,1).U, uvf(uv,1).v);

		tess.addVertexWithUV(x, y, z, uvf(uv,2).u, uvf(uv,2).v);
		tess.addVertexWithUV(x, Y, z, uvf(uv,2).u, uvf(uv,2).V);
		tess.addVertexWithUV(X, Y, z, uvf(uv,2).U, uvf(uv,2).V);
		tess.addVertexWithUV(X, y, z, uvf(uv,2).U, uvf(uv,2).v);
		tess.addVertexWithUV(X, y, Z, uvf(uv,3).U, uvf(uv,3).v);
		tess.addVertexWithUV(X, Y, Z, uvf(uv,3).U, uvf(uv,3).V);
		tess.addVertexWithUV(x, Y, Z, uvf(uv,3).u, uvf(uv,3).V);
		tess.addVertexWithUV(x, y, Z, uvf(uv,3).u, uvf(uv,3).v);

		tess.addVertexWithUV(x, y, z, uvf(uv,4).u, uvf(uv,4).v);
		tess.addVertexWithUV(x, y, Z, uvf(uv,4).u, uvf(uv,4).V);
		tess.addVertexWithUV(x, Y, Z, uvf(uv,4).U, uvf(uv,4).V);
		tess.addVertexWithUV(x, Y, z, uvf(uv,4).U, uvf(uv,4).v);
		tess.addVertexWithUV(X, Y, z, uvf(uv,5).U, uvf(uv,5).v);
		tess.addVertexWithUV(X, Y, Z, uvf(uv,5).U, uvf(uv,5).V);
		tess.addVertexWithUV(X, y, Z, uvf(uv,5).u, uvf(uv,5).V);
		tess.addVertexWithUV(X, y, z, uvf(uv,5).u, uvf(uv,5).v);


		if(draw)
			tess.draw();
	}

	public static void colour(Colour c)
	{
		GL11.glColor4f(c.r, c.g, c.b, c.a);
	}
	public static void resetColour(){ colour(white); }

	public static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void renderMultiBlock(IMultiBlockStructure struct, RenderBlockAccess rba)
	{
		GL11.glPushMatrix();
		IBlockState[][][] s = struct.getStructureDefinition();
		rb.blockAccess=rba;
		Tessellator.instance.startDrawingQuads();
		RenderHelper.bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
		for(int y = 0; y < s.length; y++)
		{
			IBlockState[][] fl = s[y];
			if(fl == null) continue;
			for(int x = 0; x <fl.length; x++)
			{
				IBlockState[] row = fl[x];
				if(row == null) continue;
				for(int z = 0; z < row.length; z++)
				{
					IBlockState bs = row[z];
					if(bs == null) continue;
					Block b = bs.getDefaultBlock();
					if(b == null) continue;
					rb.renderBlockByRenderType(b, x, y, z);
				}
			}
		}
		Tessellator.instance.draw();
		for(int y = 0; y < s.length; y++)
		{
			IBlockState[][] fl = s[y];
			if(fl == null) continue;
			for(int x = 0; x <fl.length; x++)
			{
				IBlockState[] row = fl[x];
				if(row == null) continue;
				for(int z = 0; z < row.length; z++)
				{
					IBlockState bs = row[z];
					if(bs == null) continue;
					TileEntity te = rb.blockAccess.getTileEntity(x, y, z);
					if((te != null) && TileEntityRendererDispatcher.instance.hasSpecialRenderer(te))
						TileEntityRendererDispatcher.instance.renderTileEntityAt(te, x, y, z, 0);
				}
			}
		}
		GL11.glPopMatrix();
	}

	public static FontRenderer getFontRenderer()
	{
		if(fr == null) fr = Minecraft.getMinecraft().fontRenderer;
		return fr;
	}
}
