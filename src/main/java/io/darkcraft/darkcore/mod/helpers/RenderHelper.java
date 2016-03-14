package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class RenderHelper
{
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
}
