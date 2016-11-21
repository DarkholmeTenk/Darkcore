package io.darkcraft.darkcore.mod.compat;

import net.minecraft.util.ChatMessageComponent;

public class ChatComponentText extends ChatMessageComponent
{
	public ChatComponentText(String message)
	{
		addText(message);
	}
}
