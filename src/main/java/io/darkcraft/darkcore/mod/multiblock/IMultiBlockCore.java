package io.darkcraft.darkcore.mod.multiblock;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

public interface IMultiBlockCore
{
	public boolean isValid();

	public void recheckValidity();

	public SimpleCoordStore getCoords();

	public boolean keepRechecking();
}
