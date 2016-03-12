package io.darkcraft.darkcore.mod.helpers;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ServerHelper
{


	public static boolean isServer()
	{
		return !FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT);
	}

	public static boolean isClient()
	{
		return FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT);
	}

	public static boolean isIntegratedClient()
	{
		if(isServer())
			return false;
		Minecraft m = Minecraft.getMinecraft();
		return m.isIntegratedServerRunning();
	}

	public static NBTTagCompound readNBT(InputStream in)
	{
		try
		{
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(in);
			return nbt;
		}
		catch (ZipException e)
		{
			try
			{
				if (in instanceof DataInputStream)
				{
					NBTTagCompound nbt = CompressedStreamTools.read((DataInputStream) in);
					return nbt;
				}
				return null;
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		catch (IOException e)
		{
			System.err.println("[SH]Error writing NBT: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static void writeNBT(NBTTagCompound nbt, OutputStream out)
	{
		try
		{
			CompressedStreamTools.writeCompressed(nbt, out);
		}
		catch (IOException e)
		{
			System.err.println("[SH]Error writing NBT: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static MinecraftServer getServer()
	{
		return MinecraftServer.getServer();
	}

	public static ServerConfigurationManager getConfigManager()
	{
		return MinecraftServer.getServer().getConfigurationManager();
	}

	public static EntityPlayerMP getPlayer(String username)
	{
		if((username == null) || username.isEmpty()) return null;
		List playerEnts = getConfigManager().playerEntityList;
		for (Object o : playerEnts)
		{
			if (o instanceof EntityPlayerMP)
			{
				if (((EntityPlayerMP) o).getCommandSenderName().equalsIgnoreCase(username)) return (EntityPlayerMP) o;
			}
		}
		return null;
	}

	public static String getUsername(EntityPlayer player)
	{
		return player.getCommandSenderName();
	}

	/**
	 * Sends a message in the chat
	 *
	 * @param pl
	 * @param source
	 * @param String s
	 */
	public static void sendString(EntityPlayer pl, String source, String s)
	{
		sendString(pl, new ChatComponentText("[" + source + "] " + s));
	}

	/**
	 * Sends a message in the chat
	 *
	 * @param pl
	 * @param message
	 */
	public static void sendString(EntityPlayer pl, ChatComponentText message)
	{
		if(pl == null) return;
		pl.addChatMessage(message);
	}

	/**
	 * Sends a message in the chat
	 *
	 * @param pl
	 * @param string
	 */
	public static void sendString(EntityPlayer pl, String string)
	{
		sendString(pl, new ChatComponentText(string));
	}

	/**
	 * Sends a coloured message in the chat
	 *
	 * @param pl
	 * @param source
	 * @param string
	 * @param color
	 */
	public static void sendString(EntityPlayer pl, String source, String string, EnumChatFormatting color)
	{
		sendString(pl, "["+source+"]"+string, color);
	}

	/**
	 * Sends a coloured message in the chat
	 *
	 * @param pl
	 * @param string
	 * @param color
	 */
	public static void sendString(EntityPlayer pl, String string, EnumChatFormatting color)
	{
		sendString(pl, string, color, false, false, false, false);
	}

	/**
	 * Sends an italic message in the chat
	 *
	 * @param pl
	 * @param string
	 * @param color
	 */
	public static void sendItalicString(EntityPlayer pl, String string, EnumChatFormatting color)
	{
		sendString(pl, string, color, true, false, false, false);

	}

	/**
	 * Sends a bold message in the chat
	 *
	 * @param pl
	 * @param string
	 * @param color
	 */
	public static void sendBoldString(EntityPlayer pl, String string, EnumChatFormatting color)
	{
		sendString(pl, string, color, false, true, false, false);

	}

	/**
	 * Sends a struck through message in the chat
	 *
	 * @param pl
	 * @param string
	 * @param color
	 */
	public static void sendStrikedThroughString(EntityPlayer pl, String string, EnumChatFormatting color)
	{
		sendString(pl, string, color, false, false, true, false);

	}

	/**
	 * Sends an underlined through message in the chat
	 *
	 * @param pl
	 * @param string
	 * @param color
	 */
	public static void sendUnderlinedString(EntityPlayer pl, String string, EnumChatFormatting color)
	{
		sendString(pl, string, color, false, false, false, true);

	}

	/**
	 * Sends a styled message in the chat
	 *
	 * @param pl
	 * @param string
	 * @param color
	 */
	public static void sendString(EntityPlayer pl, String string, EnumChatFormatting color, boolean italics, boolean bold, boolean strikeThrough, boolean underLined)
	{
		ChatComponentText c = new ChatComponentText("");
		c.getChatStyle().setColor(color);
		c.getChatStyle().setItalic(italics);
		c.getChatStyle().setBold(bold);
		c.getChatStyle().setStrikethrough(strikeThrough);
		c.getChatStyle().setUnderlined(underLined);
		c.appendText(string);
		pl.addChatMessage(c);
	}

}
