package io.darkcraft.darkcore.mod.helpers;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ServerHelper
{
	public static boolean isServer()
	{
		return !FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT);
	}
	
	public static NBTTagCompound readNBT(InputStream in)
	{
		try
		{
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(in);
			return nbt;
		}
		catch(ZipException e)
		{
			try
			{
				if(in instanceof DataInputStream)
				{
					NBTTagCompound nbt = CompressedStreamTools.read((DataInputStream)in);
					return nbt;
				}
				return null;
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		catch(IOException e)
		{
			System.err.println("[SH]Error writing NBT: "+ e.getMessage());
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
		catch(IOException e)
		{
			System.err.println("[SH]Error writing NBT: "+ e.getMessage());
			e.printStackTrace();
		}
	}
}
