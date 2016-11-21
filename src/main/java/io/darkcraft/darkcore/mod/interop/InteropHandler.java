package io.darkcraft.darkcore.mod.interop;

import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.Loader;
import io.darkcraft.darkcore.mod.DarkcoreMod;

public class InteropHandler
{
	private static Set<InteropInstance> instances = new HashSet<InteropInstance>();

	public static void register(InteropInstance instance)
	{
		instance.installed = Loader.isModLoaded(instance.mod);

		if(!instance.installed) return;
		instances.add(instance);
		if(DarkcoreMod.preInited)
			instance.preInit();
		if(DarkcoreMod.inited)
			instance.init();
		if(DarkcoreMod.postInited)
			instance.postInit();
	}
}
