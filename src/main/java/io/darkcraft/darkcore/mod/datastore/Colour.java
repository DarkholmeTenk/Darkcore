package io.darkcraft.darkcore.mod.datastore;

public class Colour
{
	public final float r;
	public final float g;
	public final float b;
	public final float a;

	/**
	 * This int represents the colour in the same way the minecraft font renderer does, which doesn't include alpha
	 */
	public final int asInt;
	/**
	 * This long represents the colour in the same way the minecraft font renderer does, but has the most significant byte as the alpha
	 */
	public final long asLong;

	public Colour(int i)
	{
		this(iToF(i,16),iToF(i,8),iToF(i,0));
	}

	public Colour(long i)
	{
		this(iToF(i,16),iToF(i,8),iToF(i,0),iToF(i,24));
	}

	public Colour(float _r, float _g, float _b)
	{
		this(_r,_g,_b,1f);
	}

	public Colour(float _r, float _g, float _b, float _a)
	{
		r=_r;
		g=_g;
		b=_b;
		a=_a;
		asInt = ((fToI(r) << 16) + (fToI(g) << 8) + fToI(b));
		asLong = (((long)fToI(a)) << 24) + asInt;
	}

	private static int fToI(float f)
	{
		return (int) (f * 255);
	}

	private static float iToF(long i, int shift)
	{
		int t = (int) ((i >> shift) % 256l);
		float temp = t / 255f;
		return temp;
	}

	@Override
	public int hashCode()
	{
		return Long.hashCode(asLong);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Colour)
			return asLong == ((Colour)o).asLong;
		return false;
	}
}
