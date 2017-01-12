package io.darkcraft.darkcore.test.math;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.common.cache.Cache;

import io.darkcraft.darkcore.mod.helpers.MathHelper;

public class BinomialCoefficientTest
{
	
	@Before
	public void clear() throws Exception
	{
		Field f = MathHelper.class.getDeclaredField("binomialCache");
		f.setAccessible(true);
		Cache c = ((Cache) (f.get(null)));
		c.invalidateAll();
	}

	@Test
	public void testOne()
	{
		assertTrue(Arrays.equals(MathHelper.getBinomialCoefficient(1), new int[]{1}));
	}
	
	private static final int[][] values = {
			{ 1 },
			{1, 1},
			{1, 2, 1},
			{1, 3, 3, 1},
			{1, 4, 6, 4, 1},
			{1,5, 10, 10, 5, 1},
			{1, 6, 15, 20, 15, 6, 1},
	};
	
	@Test
	public void testGeneration()
	{
		long st = System.nanoTime();
		for(int i = 0; i < values.length; i++)
		{
			int[] vals = MathHelper.getBinomialCoefficient(i+1);
			assertTrue(Arrays.toString(vals), Arrays.equals(values[i], vals));
		}
		long et = System.nanoTime();
		for(int i = 0; i < values.length; i++)
			MathHelper.getBinomialCoefficient(i+1);
		long ft = System.nanoTime();
		assertTrue(ft-et < et - st);
		System.out.format("Generating costs:%n%3.3f%n%3.3f%n",(et-st)/1000.0, (ft-et)/1000.0);
	}
	
	private long time(Runnable r)
	{
		long st = System.nanoTime();
		r.run();
		return System.nanoTime() - st;
	}
	
	@Test
	public void testWithoutPrefill()
	{
		int[] vals = MathHelper.getBinomialCoefficient(17);
		assertTrue(Arrays.toString(vals), Arrays.equals(
				new int[]{1,16,120,560,1820,4368,8008,11440,12870,11440,8008,4368,1820,560,120,16,1},
				vals));
	}
	
	private String getStringFromTimes(long... ts)
	{
		String x = "Times: \n";
		int i = 0;
		for(long l : ts)
			x += String.format("%4d% 6.3fms%n", i++, (l / 1000000.0));
		return x;
	}
	
	@Test
	public void testTimes()
	{
		long[] times = new long[60];
		for(int i = 0; i< times.length; i+=3)
		{
			final int x = i/3;
			times[i] = time(() -> MathHelper.getBinomialCoefficient(x));
			times[i+1] = time(() -> MathHelper.getBinomialCoefficient(x));
		}
		for(int i = 2; i< times.length; i+=3)
		{
			final int x = i/3;
			times[i] = time(() -> MathHelper.getBinomialCoefficient(x));
		}
		System.out.println(getStringFromTimes(times));
	}
}
