package io.darkcraft.darkcore.test.math;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import io.darkcraft.darkcore.mod.helpers.MathHelper;

public class CoefficientsTest
{
	private final double t = 0.1;
	private final double mt = 0.9;
	
	@Test
	public void testTwo()
	{
		double[] vals = MathHelper.getCoefficients(t, mt, 2);
		assertTrue(Arrays.toString(vals), Arrays.equals(vals, new double[]{0.1,0.9}));
	}
	
	private boolean equals(double[] a, double[] b)
	{
		if(a.length != b.length)
			return false;
		for(int i = 0; i < a.length; i++)
			if(Math.abs(a[i]-b[i]) > 0.0000001)
				return false;
		return true;
	}
	
	@Test
	public void testThree()
	{
		double[] vals = MathHelper.getCoefficients(t, mt, 3);
		assertTrue(Arrays.toString(vals), equals(vals, new double[]{0.01,0.09,0.81}));
	}
	
	@Test
	public void testFour()
	{
		double[] vals = MathHelper.getCoefficients(t, mt, 4);
		assertTrue(Arrays.toString(vals), equals(vals, new double[]{0.001,0.009,0.081,0.729}));
	}
	
	@Test
	public void testFive()
	{
		double[] vals = MathHelper.getCoefficients(t, mt, 5);
		assertTrue(Arrays.toString(vals), equals(vals, new double[]{0.0001,0.0009,0.0081,0.0729, 0.6561}));
	}
}
