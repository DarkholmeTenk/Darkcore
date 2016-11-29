package io.darkcraft.darkcore.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import net.minecraft.nbt.NBTTagCompound;

public class SetMapperTest
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
		tc.stringSet.addAll(Arrays.asList("A","b","c", null));
		NBTTagCompound nbt = mapper.writeToNBT(tc);
		System.out.println(nbt.toString());

		TestClass second = mapper.createFromNBT(nbt);
		assertThat(second.stringSet, hasItems("A","b", "c", null));
	}

	@NBTSerialisable
	public static class TestClass
	{
		@NBTProperty
		public Set<String> stringSet = new HashSet<String>();
	}
}
