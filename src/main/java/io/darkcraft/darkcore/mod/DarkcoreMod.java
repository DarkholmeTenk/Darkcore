package io.darkcraft.darkcore.mod;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.handlers.ChunkLoadingHandler;
import io.darkcraft.darkcore.mod.handlers.CommandHandler;
import io.darkcraft.darkcore.mod.handlers.SoundPacketHandler;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.darkcore.mod.network.PacketHandler;
import io.darkcraft.darkcore.mod.proxy.CommonProxy;

import java.util.HashMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "darkcore", version = "0.22")
public class DarkcoreMod implements IConfigHandlerMod
{
	@SidedProxy(clientSide = "io.darkcraft.darkcore.mod.proxy.ClientProxy", serverSide = "io.darkcraft.darkcore.mod.proxy.CommonProxy")
	public static CommonProxy						proxy;
	public static DarkcoreMod						i;

	public static ChunkLoadingHandler				chunkLoadingHandler	= null;
	public static ConfigHandler						configHandler		= null;
	public static FMLEventChannel					networkChannel;
	public static PacketHandler						packetHandler		= new PacketHandler();
	public static SoundPacketHandler				soundPacketHandler	= new SoundPacketHandler();
	public static CommandHandler					comHandler			= new CommandHandler();
	private static HashMap<String, CreativeTabs>	creativeTabMap		= new HashMap();
	public static ConfigFile						config				= null;
	public static boolean							debugText			= true;
	public static boolean							repostMessage		= true;
	private static String[]							repostMessages		= { "DarkCore: If you have downloaded this mod from anywhere using an ad-wall or other method of gaining money from this mod then that site is breaking this mod's license.", "Please download from official sources", "You can disable this message in the DarkCore config file" ,""};

	public static void refreshConfigs()
	{
		if (config == null) config = configHandler.registerConfigNeeder("DarkCore");
		AbstractTileEntity.refreshConfigs();
		AbstractBlock.refreshConfigs();
		debugText = config.getBoolean("debug text", false, "Print debug text");
		repostMessage = config.getBoolean("Display repost message", true, "Set to false if you have seen the repost message", "and are aware the correct download location", "is http://minecraft.curseforge.com/mc-mods/230170-tardis-mod",
				"and the forum post is http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2420176-tardis-mod");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent ev)
	{
		i = this;
		ConfigHandlerFactory.setConfigDir(ev.getModConfigurationDirectory());
		configHandler = ConfigHandlerFactory.getConfigHandler(this);
		networkChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel("darkcore");
		packetHandler.registerHandler(0, soundPacketHandler);

	}

	@EventHandler
	public void init(FMLInitializationEvent ev)
	{
		refreshConfigs();
		networkChannel.register(packetHandler);
		FMLCommonHandler.instance().bus().register(this);
	}

	@EventHandler
	public void preServerStarting(FMLServerAboutToStartEvent ev)
	{
		resetChunkLoadingHandler();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		comHandler.registerCommands(event);
		new DarkcoreTeleporter(event.getServer().worldServerForDimension(0));
	}

	private void resetChunkLoadingHandler()
	{
		if (chunkLoadingHandler != null)
		{
			MinecraftForge.EVENT_BUS.unregister(chunkLoadingHandler);
			FMLCommonHandler.instance().bus().unregister(chunkLoadingHandler);
		}
		chunkLoadingHandler = new ChunkLoadingHandler();
		MinecraftForge.EVENT_BUS.register(chunkLoadingHandler);
		FMLCommonHandler.instance().bus().register(chunkLoadingHandler);
		ForgeChunkManager.setForcedChunkLoadingCallback(this, chunkLoadingHandler);
	}

	@Override
	public String getModID()
	{
		return "darkcore";
	}

	public static void registerCreativeTab(String modID, CreativeTabs tab)
	{
		creativeTabMap.put(modID, tab);
	}

	public static CreativeTabs getCreativeTab(String modID)
	{
		if (creativeTabMap.containsKey(modID)) return creativeTabMap.get(modID);
		return null;
	}

	@SubscribeEvent
	public void repostWarning(PlayerLoggedInEvent event)
	{
		System.out.println("RW");
		EntityPlayer pl = event.player;
		if (repostMessage) for (String s : repostMessages)
			ServerHelper.sendString(pl, s);
	}
}
