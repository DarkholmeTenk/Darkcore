package io.darkcraft.darkcore.mod.nbt.impl.collections;

import java.util.logging.Logger;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.datastore.PropertyMap;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;

public class PropertyMapMapper<T extends PropertyMap> extends MapMapper<T>
{
	private static final Logger LOGGER = Logger.getLogger(PropertyMapMapper.class.getName());

	public PropertyMapMapper(SerialisableType type)
	{
		super(type);
	}

	@Override
	public T createFromNBT(NBTTagCompound nbt, Object... arguments)
	{
		return createNewMap();
	}

	@Override
	public T createNewMap()
	{
		throw new RuntimeException("Unable to create new property map mapper");
	}

}
