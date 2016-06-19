package io.darkcraft.darkcore.mod.proxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import cpw.mods.fml.client.registry.ClientRegistry;
import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockContainer;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.client.EffectOverlayRenderer;
import io.darkcraft.darkcore.mod.client.MessageOverlayRenderer;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.impl.UniqueSwordRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
	private HashMap<AbstractBlockContainer, AbstractBlockRenderer> clientBlockRendererMap = new HashMap();
	private HashMap<AbstractItem, IItemRenderer> clientItemRenderMap = new HashMap();

	private void registerIR(AbstractItem i, IItemRenderer r)
	{
		if((i == null) || (r == null)) return;
		MinecraftForgeClient.registerItemRenderer(i,r);
	}

	private void registerCBR(AbstractBlockContainer b, AbstractBlockRenderer r)
	{
		if((b == null) || (r == null)) return;
		ClientRegistry.bindTileEntitySpecialRenderer(b.getTEClass(), r);
		if(b.useRendererForItem())
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(b),r);
	}

	private void registerClientRenderers()
	{
		{
			Iterator<Entry<AbstractBlockContainer,AbstractBlockRenderer>> iter = clientBlockRendererMap.entrySet().iterator();
			while(iter.hasNext())
			{
				Entry<AbstractBlockContainer,AbstractBlockRenderer> n = iter.next();
				registerCBR(n.getKey(),n.getValue());
				iter.remove();
			}
		}

		{
			Iterator<Entry<AbstractItem,IItemRenderer>> iter = clientItemRenderMap.entrySet().iterator();
			while(iter.hasNext())
			{
				Entry<AbstractItem,IItemRenderer> n = iter.next();
				registerIR(n.getKey(),n.getValue());
				iter.remove();
			}
		}
	}

	public void registerClientItem(AbstractItem i)
	{
		IItemRenderer iir = i.getRenderer();
		if(iir == null) return;
		if(DarkcoreMod.inited)
			registerIR(i,iir);
		else
			clientItemRenderMap.put(i, iir);
	}

	public void registerClientBlock(AbstractBlockContainer b)
	{
		AbstractBlockRenderer abr = b.getRenderer();
		if(abr == null) return;
		if(DarkcoreMod.inited)
			registerCBR(b,abr);
		else
			clientBlockRendererMap.put(b, abr);
	}

	@Override
	public void init()
	{
		MinecraftForgeClient.registerItemRenderer(DarkcoreMod.uniqueSword, new UniqueSwordRenderer());
		MinecraftForge.EVENT_BUS.register(MessageOverlayRenderer.i);
		MinecraftForge.EVENT_BUS.register(EffectOverlayRenderer.i);
		EffectOverlayRenderer.refreshConfigs();
	}

	@Override
	public void postInit()
	{
		registerClientRenderers();
	}

	@Override
	public World getWorld(int id)
	{
		World w = Minecraft.getMinecraft().theWorld;
		if ((w != null) && (WorldHelper.getWorldID(w) == id)) return w;
		if(DarkcoreMod.debugText)
			Thread.dumpStack();
		return null;
	}
}
