package io.darkcraft.darkcore.mod.datastore;

public class Pair<A,B>
{
	public final A a;
	public final B b;
	public Pair(A _a, B _b)
	{
		a = _a;
		b = _b;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((a == null) ? 0 : a.hashCode());
		result = (prime * result) + ((b == null) ? 0 : b.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Pair)) return false;
		Pair other = (Pair) obj;
		if (a == null)
		{
			if (other.a != null) return false;
		}
		else if (!a.equals(other.a)) return false;
		if (b == null)
		{
			if (other.b != null) return false;
		}
		else if (!b.equals(other.b)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "[" + a.toString() + ","+b.toString()+"]";
	}
}
