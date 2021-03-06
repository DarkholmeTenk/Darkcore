package io.darkcraft.darkcore.mod.interop;

import java.util.Set;

import cpw.mods.fml.common.Loader;
import gnu.trove.set.hash.THashSet;
import io.darkcraft.darkcore.mod.DarkcoreMod;

public class InteropHandler
{
	private static Set<InteropInstance> instances = new THashSet<InteropInstance>();

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
