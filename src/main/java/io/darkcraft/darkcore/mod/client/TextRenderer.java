package io.darkcraft.darkcore.mod.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;

public class TextRenderer
{
	private static final FontRenderer fr = RenderHelper.getFontRenderer();
	private static final int textHeight = fr.FONT_HEIGHT;

	private final int w;
	private final int h;

	private int s = 1;

	private List<String> input = null;
	private List<String> calculated = null;
	private boolean shadow = false;
	private Colour colour = Colour.white;

	public TextRenderer(int w, int h)
	{
		this.w = w;
		this.h = h;
	}

	public void setText(String... newText)
	{
		setText(Arrays.asList(newText));
	}

	private static final List<String> empty = new ArrayList<>();

	public void setText(List<String> newText)
	{
		if(newText == null) newText = empty;
		if(Objects.equals(newText, input))
			return;
		input = newText;
		s = 1;
		List<String> output;
		while(true)
		{
			output = new ArrayList<>();
			for(String str : newText)
				output.addAll(fr.listFormattedStringToWidth(str, w * s));
			if((output.size() * textHeight) < (h * s))
				break;
			s++;
		}
		calculated = output;
	}

	public void setColour(Colour c)
	{
		colour = c;
	}

	public void setShadow(boolean shadow)
	{
		this.shadow = shadow;
	}

	public void render()
	{
		float ns = 1f/s;
		int y = 0;
		GL11.glPushMatrix();
		GL11.glScaled(ns, ns, ns);
		for(String q : calculated)
		{
			float tw = (float)(w*s) / fr.getStringWidth(q);
			boolean ow = (tw < 1);
			if(ow)
			{
				GL11.glPushMatrix();
				GL11.glScaled(tw, 1, tw);
			}
			fr.drawString(q, 0, textHeight * (y++), colour.asInt, shadow);
			if(ow)
				GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
}
