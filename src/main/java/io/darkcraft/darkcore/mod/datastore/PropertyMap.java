package io.darkcraft.darkcore.mod.datastore;

import java.util.HashMap;
import java.util.function.Function;

public class PropertyMap<K,V> extends HashMap<K,V>
{
	private final Function<V,K> function;

	public PropertyMap(Function<V,K> function)
	{
		this.function = function;
	}

	public V add(V value)
	{
		return put(function.apply(value), value);
	}
}
