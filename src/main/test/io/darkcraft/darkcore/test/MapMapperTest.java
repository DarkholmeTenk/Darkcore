package io.darkcraft.darkcore.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import net.minecraft.nbt.NBTTagCompound;

public class MapMapperTest
{
	private Mapper<TestClass> mapper;

	@Before
	public void setup()
	{
		mapper = NBTHelper.getMapper(TestClass.class, SerialisableType.WORLD);
	}

	@Test
	public void test()
	{
		TestClass tc = new TestClass();
		for(long i = 0; i < 10; i++)
			tc.stringSet.put(""+i, i);
		NBTTagCompound nbt = mapper.writeToNBT(tc);
		System.out.println(nbt.toString());

		TestClass second = mapper.createFromNBT(nbt);
		for(long i = 0; i < 10; i++)
			assertThat(second.stringSet.get(""+i), is(equalTo(i)));
	}

	@NBTSerialisable
	public static class TestClass
	{
		@NBTProperty
		public Map<String, Long> stringSet = new HashMap<String, Long>();
	}
}
