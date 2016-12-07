package io.darkcraft.darkcore.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;

public class SCSTest
{
	private SimpleCoordStore coords;
	private Mapper<SimpleCoordStore> mapper;

	@Before
	public void setup()
	{
		Random r = new Random();
		coords = new SimpleCoordStore(r.nextInt(255), r.nextInt(), r.nextInt(), r.nextInt());
		mapper = NBTHelper.getMapper(SimpleCoordStore.class, SerialisableType.TRANSMIT);
	}

	@Test
	public void test()
	{
		NBTTagCompound tag = mapper.writeToNBT(coords);

		SimpleCoordStore newCoords = mapper.createFromNBT(tag);
		System.out.println(newCoords);

		assertEquals(coords, newCoords);
	}

	@Test
	public void testOriginal()
	{
		NBTTagCompound nbt = coords.writeToNBT();

		SimpleCoordStore newCoords = SimpleCoordStore.readFromNBT(nbt);

		assertEquals(coords, newCoords);
	}
}
