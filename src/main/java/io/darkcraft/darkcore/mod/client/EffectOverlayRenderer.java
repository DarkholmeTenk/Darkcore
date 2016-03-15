package io.darkcraft.darkcore.mod.client;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EffectOverlayRenderer  extends Gui
{
	private static ConfigFile config;
	private static boolean displayUnlimited = true;
	private static boolean displayOver1m = true;
	private static double xo = 0.35;
	private static double yo = 0.005;
	private static double scale = 1;

	public static void refreshConfigs()
	{
		if(config == null) config = DarkcoreMod.configHandler.registerConfigNeeder("EffectOverlay");
		displayUnlimited = config.getBoolean("Display Unlimited", true, "If true, effects with no duration will be visible in the overlay");
		displayOver1m = config.getBoolean("Display Over 1 m", true, "If true, effects with durations longer than 1m will be visible in the overlay");
		xo = config.getDouble("X Offset", 0.35, "The x offset relative to screen resolution that the overlay will render at");
		yo = config.getDouble("Y Offset", 0.005, "The y offset relative to screen resolution that the overlay will render at");
		scale = config.getDouble("Scale", 1, "The scale of the effect renderer");
	}
	public static EffectOverlayRenderer i = new EffectOverlayRenderer();
	private FontRenderer fr;

	@SubscribeEvent
	public void handlerEvent(RenderGameOverlayEvent event)
	{
		if(event.isCanceled() || (event.type != ElementType.HOTBAR)) return;
		if(fr == null) fr = Minecraft.getMinecraft().fontRenderer;
		render(event.resolution);
		GL11.glColor3f(1, 1, 1);
	}

	private void face(double x, double y, double w, double h, double u, double v, double U, double V)
	{
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y+h, zLevel, u, V);
		tess.addVertexWithUV(x+w, y+h, zLevel, U, V);
		tess.addVertexWithUV(x+w, y, zLevel, U, v);
		tess.addVertexWithUV(x, y, zLevel, u, v);
		tess.draw();
	}

	private String getDuration(AbstractEffect effect)
	{
		if(effect.duration == -1) return "";
		int sl = (effect.duration - effect.getTT())/20;
		if(sl < 3600)
		{
			int ml = sl / 60;
			sl = sl % 60;
			return String.format("%02d:%02d", ml,sl);
		}
		else
		{
			int hl = sl / 3600;
			sl %= 3600;
			int ml = sl / 60;
			sl %= 60;
			return String.format("%02d:%02d:%02d", hl,ml,sl);
		}
	}

	private void renderEffects()
	{
		EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
		if((pl == null) || pl.isDead) return;
		EntityEffectStore ees = EffectHandler.getEffectStore(pl);
		if(ees == null) return;
		Collection<AbstractEffect> effects = ees.getEffects();
		int i = 0;
		for(AbstractEffect effect : effects)
		{
			if(!effect.visible) continue;
			renderEffect(effect,i++);
		}
	}

	private void renderEffect(AbstractEffect effect, int i)
	{
		RenderHelper.bindTexture(effect.getIcon());
		UVStore uv = effect.getIconLocation();
		if(uv == null) uv = UVStore.defaultUV;
		if((effect.duration >= 1200) && !displayOver1m) return;
		if((effect.duration == -1) && !displayUnlimited) return;
		GL11.glPushMatrix();
		int is = 16;
		int xo = 45;
		int yo = 32;
		int x = i % 5;
		int y = i / 5;
		GL11.glColor3f(1, 1, 1);
		face(x*xo,y*yo,is,is,uv.u,uv.v,uv.U,uv.V);
		fr.drawString(getDuration(effect), x * xo, (y * yo)+is+5, 0);
		GL11.glPopMatrix();
	}

	private void render(ScaledResolution res)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(res.getScaledWidth_double()*xo, res.getScaledHeight_double()*yo, 0);
		GL11.glScaled(scale, scale, 1);
		renderEffects();
		GL11.glPopMatrix();
	}
}
