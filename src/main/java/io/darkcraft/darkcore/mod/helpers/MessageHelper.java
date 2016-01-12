package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.handlers.packets.MessagePacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

public class MessageHelper
{
	private static void sendMess(EntityPlayerMP pl, DataPacket dp)
	{
		DarkcoreMod.networkChannel.sendTo(dp, pl);
	}

	public static void sendMessage(ICommandSender player, ResourceLocation icon, String message, int seconds)
	{
		if(player instanceof EntityPlayerMP)
			sendMess((EntityPlayerMP)player, MessagePacketHandler.getDataPacket(icon, message, seconds));
		else
			player.addChatMessage(new ChatComponentText(message));
	}

	public static void sendMessage(ICommandSender player, String message, int seconds)
	{
		sendMessage(player,null,message,seconds);
	}

	public static void sendMessage(ICommandSender player, String message)
	{
		sendMessage(player,null,message,MessagePacketHandler.secondsDefault);
	}

	public static void sendToWorld(int world, ResourceLocation rl, String message, int seconds)
	{
		DarkcoreMod.networkChannel.sendToDimension(MessagePacketHandler.getDataPacket(rl, message, seconds), world);
	}

	public static void sendToWorld(int world, String message, int seconds)
	{
		sendToWorld(world, null, message, seconds);
	}

	public static void sendToWorld(int world, String message)
	{
		sendToWorld(world, null, message, MessagePacketHandler.secondsDefault);
	}

	public static void sendToAll(ResourceLocation rl, String message, int seconds)
	{
		DarkcoreMod.networkChannel.sendToAll(MessagePacketHandler.getDataPacket(rl, message, seconds));
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
