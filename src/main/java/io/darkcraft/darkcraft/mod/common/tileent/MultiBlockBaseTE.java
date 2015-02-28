package io.darkcraft.darkcraft.mod.common.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MultiBlockHelper;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockCore;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockPart;

public class MultiBlockBaseTE extends AbstractTileEntity implements IMultiBlockPart
{
	private IMultiBlockCore	linkedCore	= null;
	private boolean			isLinked	= false;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (tt % 20 == 0 && isLinked)
			recheckCore();
	}

	@Override
	public void setMultiBlockCore(IMultiBlockCore core)
	{
		linkedCore = core;
		isLinked = true;
	}

	@Override
	public void recheckCore()
	{
		if (isLinked && linkedCore != null)
		{
			if (!MultiBlockHelper.doesCoreExist(linkedCore))
			{
				IMultiBlockCore core = linkedCore;
				linkedCore = null;
				isLinked = false;
				core.recheckValidity();
			}
		}
	}

}
