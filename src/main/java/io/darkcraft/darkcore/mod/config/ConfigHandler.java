package io.darkcraft.darkcore.mod.config;

import java.io.File;
import java.util.HashMap;

public class ConfigHandler
{
	private final File configDir;
	private final String modID;
	private volatile HashMap<String,ConfigFile> cfMap = new HashMap<String,ConfigFile>();
	
	protected ConfigHandler(File dir,String _modID)
	{
		configDir = dir;
		modID = _modID;
	}
	
	public File getConfigDir()
	{
		return configDir;
	}
	
	public synchronized ConfigFile registerConfigNeeder(String icnKey)
	{
		if(cfMap.containsKey(icnKey))
			return cfMap.get(icnKey);
		File f = new File(configDir,icnKey+".cfg");
		ConfigFile cf = new ConfigFile(f);
		cfMap.put(icnKey, cf);
		return cf;
	}
	
	public synchronized ConfigFile getModConfig()
	{
		return registerConfigNeeder(modID);
	}
}
