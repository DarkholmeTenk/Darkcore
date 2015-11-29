package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class UniqueSwordRenderer implements IItemRenderer
{
	private static ResourceLocation pommel = new ResourceLocation(DarkcoreMod.modName, "textures/pommel.png");
	private static ResourceLocation blade = new ResourceLocation(DarkcoreMod.modName, "textures/blade.png");
	private static ResourceLocation deco = new ResourceLocation(DarkcoreMod.modName, "textures/deco.png");
	private static ResourceLocation hilt = new ResourceLocation(DarkcoreMod.modName, "textures/hilt.png");
	private static IModelCustom model;


	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcoreMod.modName, "models/keening.obj"));
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		// TODO Auto-generated method stub
		return true;
	}

	private void renderBit(ResourceLocation tex, String... names)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(tex);
		model.renderOnly(names);
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		double upSize = 1.2;
		double doSize = 0.85;
		if(type != ItemRenderType.EQUIPPED)
			GL11.glScaled(doSize, doSize, doSize);
		else
			GL11.glScaled(upSize, upSize, upSize);
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glRotated(250, 0, 1, 0);
			GL11.glRotated(-15, 0, 0, 1);
			GL11.glTranslated(0.4, 0.5, 0);
		}
		else if(type == ItemRenderType.EQUIPPED)
		{
			GL11.glRotated(-35, 0, 1, 0);
			GL11.glRotated(58, 0, 0, 1);
			GL11.glRotated(-10, 1, 0, 0);
			GL11.glTranslated(0.9, -0.65, 0);
		}
		else
		{
			doSize = 0.75;
			GL11.glScaled(doSize, doSize, doSize);
			if(type == ItemRenderType.INVENTORY)
				GL11.glTranslated(0, -0.5, 0);
			else
				GL11.glTranslated(0, 0.4, 0);
		}
		renderBit(deco, "Deco_Cube.003");
		renderBit(hilt, "Hilt_Cube.001");
		renderBit(pommel, "Pommel_Cube.002");
		String pl = null;
		for(Object o : data)
		{
			if(o instanceof EntityPlayer)
			{
				pl = ServerHelper.getUsername((EntityPlayer) o);

			}
		}
		double r = UniqueSwordItem.getRed(pl);
		double g = UniqueSwordItem.getGre(pl);
		double b = UniqueSwordItem.getBlu(pl);
		GL11.glColor4d(r, g, b, 0.75);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		renderBit(blade, "Blade_Cube");
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4d(1, 1, 1, 1);
	}

}
