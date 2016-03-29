package io.darkcraft.darkcore.mod.abstracts;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public abstract class AbstractBlockRenderer extends TileEntitySpecialRenderer
{
	protected static FontRenderer							fr;

	public boolean handleLighting()
	{
		return true;
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
	{
		if(fr == null)
			fr = func_147498_b();
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// This will move our renderer so that it will be on proper place in the world
		GL11.glTranslatef((float) d0, (float) d1, (float) d2);

		World w = tileEntity.getWorldObj();
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;

		Tessellator tessellator = Tessellator.instance;

		if(handleLighting())
		{
			float brightness = w.getBlockLightValue(x, y, z);
			int l = w.getLightBrightnessForSkyBlocks(x, y, z, 0);
			int l1 = l % 65536;
			int l2 = l / 65536;
			tessellator.setColorOpaque_F(brightness, brightness, brightness);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);
		}
		/* Note that true tile entity coordinates (tileEntity.xCoord, etc) do not match to render coordinates (d, etc) that are calculated as [true coordinates] - [player coordinates (camera coordinates)] */
		renderBlock(tessellator, tileEntity, x, y, z);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	public long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public abstract AbstractBlock getBlock();

	public abstract void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z);
}
