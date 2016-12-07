package io.darkcraft.darkcore.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;

public class SDCSMapperTest
{
	private SimpleDoubleCoordStore coords;
	private Mapper<SimpleDoubleCoordStore> mapper;

	@Before
	public void setup()
	{
		Random r = new Random();
		coords = new SimpleDoubleCoordStore(r.nextInt(255), r.nextDouble(), r.nextDouble(), r.nextDouble());
		mapper = NBTHelper.getMapper(SimpleDoubleCoordStore.class, SerialisableType.TRANSMIT);
	}

	@Test
	public void test()
	{
		NBTTagCompound tag = mapper.writeToNBT(coords);

		SimpleDoubleCoordStore newCoords = mapper.createFromNBT(tag);
		System.out.println(newCoords);

		assertEquals(coords, newCoords);
	}
}
