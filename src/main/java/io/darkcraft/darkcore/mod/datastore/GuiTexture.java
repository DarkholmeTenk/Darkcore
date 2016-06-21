package io.darkcraft.darkcore.mod.datastore;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import net.minecraft.util.ResourceLocation;

public class GuiTexture
{
	public final ResourceLocation tex;
	public final UVStore uv;
	public final int w;
	public final int h;

	public GuiTexture(ResourceLocation texture, int w, int h)
	{
		this(texture, UVStore.defaultUV, w,h);
	}

	public GuiTexture(ResourceLocation texture, UVStore _uv, int width, int height)
	{
		tex = texture;
		uv = _uv;
		w = width;
		h = height;
	}

	public void render(float x, float y, float z, boolean draw)
	{
		RenderHelper.bindTexture(tex);
		RenderHelper.uiFace(x, y, w, h, z, uv, draw);
	}
}
