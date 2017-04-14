package io.darkcraft.darkcore.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;

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

	@Test
	public void testInstances()
	{
		int testNums = 10;
		String[] originals = new String[testNums];
		TestClass tc = new TestClass();
		for(int i = 0; i < testNums; i++)
			tc.stringSet.put(""+i, (long) i);
		NBTTagCompound nbt = mapper.writeToNBT(tc);
		tc.stringSet.clear();
		for(int i = 0; i < testNums; i++)
			tc.stringSet.put(originals[i] = new String(""+i), (long) i*2);
		tc = mapper.fillFromNBT(nbt, tc);
		for(Entry<String, Long> entry : tc.stringSet.entrySet())
		{
			for(String s : originals)
				if(s.equals(entry.getKey()))
					assertSame(s, entry.getKey());
			assertEquals(entry.getKey(), ""+entry.getValue());
		}
	}

	@NBTSerialisable
	public static class TestClass
	{
		@NBTProperty
		public Map<String, Long> stringSet = new HashMap<>();
	}
}
