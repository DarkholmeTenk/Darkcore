package io.darkcraft.darkcore.test;

import static io.darkcraft.darkcore.test.EnumMapperTest.TestEnum.A;
import static io.darkcraft.darkcore.test.EnumMapperTest.TestEnum.B;
import static io.darkcraft.darkcore.test.EnumMapperTest.TestEnum.C;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.nbt.Mapper;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;

public class EnumMapperTest
{
	@Test
	public void test()
	{
		TestEnum[] enums = new TestEnum[]{A,B,C,A,C,B};
		Mapper<TestEnum[]> mapper = NBTHelper.getMapper(enums, SerialisableType.TRANSMIT);
		NBTTagCompound nbt = mapper.writeToNBT(enums);
		TestEnum[] enumsTwo = mapper.createFromNBT(nbt);
		assertEquals(enums, enumsTwo);
	}

	public static enum TestEnum
	{
		A,
		B,
		C;
	}
}
