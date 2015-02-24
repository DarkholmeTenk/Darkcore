package io.darkcraft.darkcore.mod.config;

import java.io.File;
import java.util.HashMap;

public class ConfigHandler
{
	private final File configDir;
	private volatile HashMap<String,ConfigFile> cfMap = new HashMap<String,ConfigFile>();
	
	protected ConfigHandler(File dir)
	{
		configDir = dir;
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
}
