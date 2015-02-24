package io.darkcraft.darkcraft.mod;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.darkcraft.mod.common.blocks.MultiBlockBaseBlock;
import io.darkcraft.darkcraft.mod.common.blocks.MultiBlockCoreBlock;
import io.darkcraft.darkcraft.mod.common.tileent.MultiBlockBaseTE;
import io.darkcraft.darkcraft.mod.common.tileent.MultiBlockCoreTE;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "darkcraft", version = "0.1", dependencies = "required-after:darkcore")
public class DarkcraftMod implements IConfigHandlerMod
{
	public static ConfigHandler	configHandler	= null;
	public static AbstractBlock multiBlockBaseBlock;
	public static AbstractBlock multiBlockCoreBlock;

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
}
