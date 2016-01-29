package io.darkcraft.darkcore.mod.interfaces;

/**
 * Implement this on your mod and call {@link io.darkcraft.darkcore.mod.config.ConfigHandlerFactory#getConfigHandler(IConfigHandlerMod)} to
 * get a ConfigHandler for your mod. Creates a folder in darkmods for your CH's config files.
 * @author dark
 *
 */
public interface IConfigHandlerMod
{
	public String getModID();
}
