package io.darkcraft.darkcore.mod.config;

import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;

import java.io.File;
import java.util.HashMap;

public class ConfigHandlerFactory
{
	private static File configDirectory = null;
	private static HashMap<String,ConfigHandler> chMap = new HashMap<String,ConfigHandler>();
	
	public static void setConfigDir(File f)
	{
		if(configDirectory != null)
			return;
		f = new File(f,"darkmods");
		if((!f.exists()) || (!f.isDirectory()))
				f.mkdir();
		configDirectory = f;
	}
	
	public static ConfigHandler getConfigHandler(IConfigHandlerMod ichm)
	{
		String modID = ichm.getModID();
		if(chMap.containsKey(modID))
			return chMap.get(modID);
		File f = new File(configDirectory,modID);
		if((!f.exists()) || (!f.isDirectory()))
			f.mkdir();
		ConfigHandler ch = new ConfigHandler(f);
		chMap.put(modID, ch);
		return ch;
	}
}
