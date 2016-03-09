package io.darkcraft.darkcore.mod.client;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
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
	public static EffectOverlayRenderer i = new EffectOverlayRenderer();
	private FontRenderer fr;

	@SubscribeEvent
	public void handlerEvent(RenderGameOverlayEvent event)
	{
		if(event.isCanceled() || (event.type != ElementType.CHAT)) return;
		if(fr == null) fr = Minecraft.getMinecraft().fontRenderer;
		render(event.resolution);
	}

	private void face(double x, double y, double w, double h, double u, double v, double U, double V)
	{
		Tessellator tess = Tessellator.instance;
		tess.addVertexWithUV(x, y+h, zLevel, u, v);
		tess.addVertexWithUV(x+w, y+h, zLevel, U, v);
		tess.addVertexWithUV(x+w, y, zLevel, U, V);
		tess.addVertexWithUV(x, y, zLevel, u, V);
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
		GL11.glPushMatrix();
		int x = i % 6;
		int y = i / 6;
		RenderHelper.bindTexture(effect.getIcon());
		UVStore uv = effect.getIconLocation();
		face(x,y,0.1,0.1,uv.u,uv.v,uv.U,uv.V);
		GL11.glPopMatrix();
	}

	private void render(ScaledResolution res)
	{
		renderEffects();
	}
}
