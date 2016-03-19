package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.packets.MessagePacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;

/**
 * This class is used to help send messages to the DarkCore message box system
 * @author dark
 *
 */
public class MessageHelper
{
	public static final int defaultSeconds = MessagePacketHandler.secondsDefault;

	private static void sendMess(EntityPlayerMP pl, DataPacket dp)
	{
		if((pl == null) || (pl instanceof FakePlayer)) return;
		if((pl.playerNetServerHandler == null) || (pl.playerNetServerHandler.netManager == null)) return;
		DarkcoreMod.networkChannel.sendTo(dp, pl);
	}

	public static void sendMessage(ICommandSender player, ResourceLocation icon, UVStore uv, String message, int seconds)
	{
		if(player instanceof EntityPlayerMP)
			sendMess((EntityPlayerMP)player, MessagePacketHandler.getDataPacket(icon, message, seconds, uv));
		else
			player.addChatMessage(new ChatComponentText(message));
	}

	public static void sendMessage(ICommandSender player, ResourceLocation icon, String message, int seconds)
	{
		sendMessage(player, icon, null, message, seconds);
	}

	public static void sendMessage(ICommandSender player, String message, int seconds)
	{
		sendMessage(player,null,null,message,seconds);
	}

	public static void sendMessage(ICommandSender player, String message)
	{
		sendMessage(player,null,null,message,MessagePacketHandler.secondsDefault);
	}

	public static void sendToWorld(int world, ResourceLocation rl, UVStore uv, String message, int seconds)
	{
		DarkcoreMod.networkChannel.sendToDimension(MessagePacketHandler.getDataPacket(rl, message, seconds, uv), world);
	}

	public static void sendToWorld(int world, ResourceLocation rl, String message, int seconds)
	{
		sendToWorld(world, rl, null, message, seconds);
	}

	public static void sendToWorld(int world, String message, int seconds)
	{
		sendToWorld(world, null, null, message, seconds);
	}

	public static void sendToWorld(int world, String message)
	{
		sendToWorld(world, null, null, message, MessagePacketHandler.secondsDefault);
	}

	public static void sendToAll(ResourceLocation rl, UVStore uv, String message, int seconds)
	{
		DarkcoreMod.networkChannel.sendToAll(MessagePacketHandler.getDataPacket(rl, message, seconds, uv));
	}

	public static void sendToAll(ResourceLocation rl, String message, int seconds)
	{
		sendToAll(rl, null, message, seconds);
	}

	public static void sendToAll(ResourceLocation rl, String message)
	{
		sendToAll(rl, message, MessagePacketHandler.secondsDefault);
	}

	public static void sendToAll(String message, int seconds)
	{
		sendToAll(null, message, seconds);
	}

	public static void sendToAll(String message)
	{
		sendToAll(null, message, MessagePacketHandler.secondsDefault);
	}
}
