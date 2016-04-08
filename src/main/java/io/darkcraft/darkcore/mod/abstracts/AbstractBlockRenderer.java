package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public abstract class AbstractBlockRenderer extends TileEntitySpecialRenderer implements IItemRenderer
{
	protected static FontRenderer							fr;

	public boolean handleLighting()
	{
		return true;
	}

	@Override
	public void bindTexture(ResourceLocation rl)
	{
		if(field_147501_a == null)
			RenderHelper.bindTexture(rl);
		else
			super.bindTexture(rl);
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

		if(handleLighting() && (w!= null))
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

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	protected void handleItemRenderType(ItemRenderType type)
	{
		double upSize = 1.2;
		double doSize = 0.85;
		if(type != ItemRenderType.EQUIPPED)
			GL11.glScaled(doSize, doSize, doSize);
		else
			GL11.glScaled(upSize, upSize, upSize);
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			doSize = 0.75;
			GL11.glScaled(doSize, doSize, doSize);
			GL11.glRotated(250, 0, 1, 0);
			GL11.glRotated(-15, 0, 0, 1);
			GL11.glTranslated(0.4, 0.5, 0);
		}
		else if(type == ItemRenderType.EQUIPPED)
		{
			doSize = 0.75;
			GL11.glScaled(doSize, doSize, doSize);
			GL11.glRotated(-35, 0, 1, 0);
			GL11.glRotated(58, 0, 0, 1);
			GL11.glRotated(-10, 1, 0, 0);
			GL11.glTranslated(0.9, -1, 0);
		}
		else
		{
			doSize = 0.75;
			GL11.glScaled(doSize, doSize, doSize);
			if(type != ItemRenderType.INVENTORY)
				GL11.glTranslated(0, 0.4, 0);
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		handleItemRenderType(type);

		renderBlock(Tessellator.instance, null, 0,0,0);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
}
