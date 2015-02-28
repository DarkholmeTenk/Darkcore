package io.darkcraft.darkcraft.mod;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.darkcraft.mod.common.CommonProxy;
import io.darkcraft.darkcraft.mod.common.blocks.MultiBlockBaseBlock;
import io.darkcraft.darkcraft.mod.common.blocks.MultiBlockCoreBlock;
import io.darkcraft.darkcraft.mod.common.registries.SpellInstanceRegistry;
import io.darkcraft.darkcraft.mod.common.tileent.MultiBlockBaseTE;
import io.darkcraft.darkcraft.mod.common.tileent.MultiBlockCoreTE;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "darkcraft", version = "0.1", dependencies = "required-after:darkcore")
public class DarkcraftMod implements IConfigHandlerMod
{
	public static ConfigHandler	configHandler	= null;
	public static AbstractBlock multiBlockBaseBlock;
	public static AbstractBlock multiBlockCoreBlock;
	public static SpellInstanceRegistry spellInstanceRegistry = null;
	
	@SidedProxy(clientSide="io.darkcraft.darkcraft.mod.client.ClientProxy",
				serverSide="io.darkcraft.darkcraft.mod.common.CommonProxy")
	public static CommonProxy proxy;

	@Override
	public String getModID()
	{
		return "darkcraft";
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configHandler = ConfigHandlerFactory.getConfigHandler(this);
		registerBlocks();
	}
	
	private void registerBlocks()
	{
		multiBlockBaseBlock = new MultiBlockBaseBlock(true,"darkcraft");
		GameRegistry.registerBlock(multiBlockBaseBlock, multiBlockBaseBlock.getUnlocalizedName());
		GameRegistry.registerTileEntity(MultiBlockBaseTE.class, multiBlockBaseBlock.getUnlocalizedName());
		multiBlockCoreBlock = new MultiBlockCoreBlock(true,"darkcraft");
		GameRegistry.registerBlock(multiBlockCoreBlock, multiBlockCoreBlock.getUnlocalizedName());
		GameRegistry.registerTileEntity(MultiBlockCoreTE.class, multiBlockCoreBlock.getUnlocalizedName());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}
	
	@EventHandler
	public void serverStarting(FMLServerAboutToStartEvent event)
	{
		registerRegistries();
	}
	
	private void registerRegistries()
	{
		if(spellInstanceRegistry != null)
			FMLCommonHandler.instance().bus().unregister(spellInstanceRegistry);
		spellInstanceRegistry = new SpellInstanceRegistry();
		FMLCommonHandler.instance().bus().register(spellInstanceRegistry);
	}
}
