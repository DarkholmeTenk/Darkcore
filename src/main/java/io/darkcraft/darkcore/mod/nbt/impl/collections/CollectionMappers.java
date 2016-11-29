package io.darkcraft.darkcore.mod.nbt.impl.collections;

import java.util.Collection;

import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.impl.SubTypeMapper;

public class CollectionMappers
{
	public static void register()
	{
		for(SerialisableType t : SerialisableType.values())
			NBTHelper.register(Collection.class, t, new SubTypeMapper<Collection>(t));
		ListMapper.register();
		SetMapper.register();
		MapMapper.register();
	}
}
