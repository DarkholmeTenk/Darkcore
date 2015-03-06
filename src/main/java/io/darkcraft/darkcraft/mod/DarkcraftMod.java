package io.darkcraft.darkcraft.mod;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.darkcraft.mod.common.CommonProxy;
import io.darkcraft.darkcraft.mod.common.blocks.MultiBlockBaseBlock;
import io.darkcraft.darkcraft.mod.common.blocks.MultiBlockCoreBlock;
import io.darkcraft.darkcraft.mod.common.command.CreateSpellCommand;
import io.darkcraft.darkcraft.mod.common.items.BaseStaff;
import io.darkcraft.darkcraft.mod.common.registries.SpellComponentRegistry;
import io.darkcraft.darkcraft.mod.common.registries.SpellInstanceRegistry;
import io.darkcraft.darkcraft.mod.common.spellsystem.MagicDamageSource;
import io.darkcraft.darkcraft.mod.common.spellsystem.components.effects.Damage;
import io.darkcraft.darkcraft.mod.common.spellsystem.components.effects.Dig;
import io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes.Area;
import io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes.Target;
import io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes.Zone;
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
	public static SpellInstanceRegistry spellInstanceRegistry = null;
	public static MagicDamageSource damageSource = new MagicDamageSource();
	
	public static AbstractBlock multiBlockBaseBlock;
	public static AbstractBlock multiBlockCoreBlock;
	
	public static AbstractItem baseStaff;
	
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
		registerItems();
		registerCommands();
		registerSpells();
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
	
	private void registerItems()
	{
		baseStaff = new BaseStaff().register();
	}
	
	private void registerCommands()
	{
		DarkcoreMod.comHandler.addCommand(new CreateSpellCommand());
	}
	
	private void registerSpells()
	{
		//Effects
		SpellComponentRegistry.add(new Damage());
		SpellComponentRegistry.add(new Dig());
		//Shapes
		SpellComponentRegistry.add(new Target());
		SpellComponentRegistry.add(new Area());
		SpellComponentRegistry.add(new Zone());
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
