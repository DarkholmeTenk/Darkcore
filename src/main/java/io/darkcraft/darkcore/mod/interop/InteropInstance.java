package io.darkcraft.darkcore.mod.interop;

public abstract class InteropInstance
{
	public final String mod;
	public boolean installed = false;

	public InteropInstance(String modID)
	{
		mod = modID;
	}

	public void preInit() {}

	public void init() {}

	public void postInit() {}

	public void serverAboutToStart() {}
}
