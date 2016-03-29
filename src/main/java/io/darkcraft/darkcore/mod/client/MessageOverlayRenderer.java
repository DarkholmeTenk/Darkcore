package io.darkcraft.darkcore.mod.client;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MessageOverlayRenderer extends Gui
{
	public static MessageOverlayRenderer i = new MessageOverlayRenderer();
	private ResourceLocation background = new ResourceLocation(DarkcoreMod.modName, "textures/gui/messagebox.png");
	private static FontRenderer fr;

	private static class Message
	{
		public final String m;
		public final ResourceLocation rl;
		public final long arrivalTime;
		public final int secs;
		public final UVStore uv;

		public Message(String message, ResourceLocation icon, int s, long arr, UVStore uv)
		{
			if((message == null) || message.isEmpty())
				m = "";
			else
			{
				String[] split = message.split(" ");
				String x = "";
				for(String str : split)
					x += StatCollector.translateToLocal(str) + " ";
				m = x.trim();
			}
			rl = icon;
			secs = s;
			arrivalTime = arr;
			this.uv = uv;
		}
	}

	private static ArrayList<Message> messageList = new ArrayList();
	public static void addMessage(String s, ResourceLocation rl, int secs, long arr, UVStore uv)
	{
		synchronized(messageList)
		{
			messageList.add(new Message(s,rl,secs,arr / 1000, uv));
		}
	}

	private static List<Message> getMessages()
	{
		long t = System.currentTimeMillis() / 1000;
		Iterator<Message> mIter = messageList.iterator();
		while(mIter.hasNext())
		{
			Message m = mIter.next();
			if((m.arrivalTime + m.secs) < t)
				mIter.remove();
		}
		return messageList;
	}

	int h = 16;
	int w = h * 10;
	int th = h >> 2;
	int mh = h >> 1;

	@SubscribeEvent
	public void handlerEvent(RenderGameOverlayEvent event)
	{
		if(event.isCanceled() || (event.type != ElementType.CHAT)) return;
		if(fr == null) fr = Minecraft.getMinecraft().fontRenderer;
		render(event.resolution);
	}

	private void face(Tessellator tess, int x, int y, int w, int h, double u, double v, double uw, double vh)
	{
		tess.addVertexWithUV(x, y+h, zLevel, u, v);
		tess.addVertexWithUV(x+w, y+h, zLevel, u+uw, v);
		tess.addVertexWithUV(x+w, y, zLevel, u+uw, v+vh);
		tess.addVertexWithUV(x, y, zLevel, u, v+vh);
	}

	private void renderBox(int numLines)
	{
		RenderHelper.bindTexture(background);
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		face(tess,0,0,w,th, 0,0.75f,1f,0.25f);
		for(int i = 0; i < numLines; i++)
			face(tess,0,(mh*i)+th,w,mh,0,0.25f, 1f,0.5f);
		face(tess,0,(mh*numLines)+th,w,th, 0,0f, 1f,0.25f);
		tess.draw();
	}

	private int renderMessage(Message m)
	{
		List<String> lines;
		int h = 1;
		float scale = 0.6875f;
		int actualWidth =  MathHelper.floor((w - mh) / scale);
		if(m.rl != null)
		{
			actualWidth = MathHelper.floor((w - (2 * mh) - th) / scale);
			h = 2;
		}
		lines = fr.listFormattedStringToWidth(m.m, actualWidth);
		h = Math.max(h, lines.size());
		renderBox(h);
		if(m.rl != null)
		{
			Tessellator tess = Tessellator.instance;
			tess.startDrawingQuads();
			RenderHelper.bindTexture(m.rl);
			RenderHelper.uiFace(th, th, mh*2, mh*2, 1, m.uv, false);
			//face(Tessellator.instance,th,th,mh*2,mh*2,m.uv.u,m.uv.v,m.uv.U,m.uv.V);
			tess.draw();
		}
		{
			GL11.glPushMatrix();
			GL11.glScalef(scale, scale, 1);
			int th = (int) Math.ceil(this.th / scale);
			int mh = (int) Math.ceil(this.mh / scale);
			for(int i = 0; i < lines.size(); i++)
			{
				fr.drawString(lines.get(i), m.rl == null ? th : mh*3, th + (mh * i), 0);
			}
			GL11.glPopMatrix();
			GL11.glColor3f(1, 1, 1);
		}
		return ((h + 1) * mh);
	}

	private void renderMessages()
	{
		synchronized(messageList)
		{
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			for(Message m : getMessages())
			{
				GL11.glTranslated(0, renderMessage(m), 0);
			}
			GL11.glPopMatrix();
		}
	}

	private void render(ScaledResolution res)
	{
		renderMessages();
	}
}
