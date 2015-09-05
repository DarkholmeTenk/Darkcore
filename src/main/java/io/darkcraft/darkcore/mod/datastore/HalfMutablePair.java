package io.darkcraft.darkcore.mod.datastore;

public class HalfMutablePair<MUTABLE,B>
{
	public MUTABLE a;
	public final B b;
	public HalfMutablePair(MUTABLE _a, B _b)
	{
		a = _a;
		b = _b;
	}



	@Override
	public int hashCode()
	{
		return b.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(!(obj instanceof HalfMutablePair)) return false;
		HalfMutablePair o = (HalfMutablePair)obj;
		if(b == null) return o.b == null;
		return b.equals(o.b);
	}



	@Override
	public String toString()
	{
		return "[" + a.toString() + ","+b.toString()+"]";
	}
}
