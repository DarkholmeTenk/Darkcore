package io.darkcraft.darkcore.mod.datastore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HashMapSet<K,V>
{
	private final HashMap<K,Set<V>> map = new HashMap();

	public Set<V> put(K key, Set<V> values)
	{
		if(values == null) return null;
		return map.put(key, values);
	}

	public Set<V> get(K key)
	{
		return map.get(key);
	}

	public boolean add(K key, V value)
	{
		if(!map.containsKey(key))
			put(key, new HashSet<V>());
		return get(key).add(value);
	}

	public Set<V> removeKey(K key)
	{
		return map.remove(key);
	}

	public boolean remove(K key, V value)
	{
		if(!map.containsKey(key)) return false;
		return map.get(key).remove(value);
	}

	public boolean containsKey(K key)
	{
		return map.containsKey(key);
	}

	public boolean contains(K key, V value)
	{
		if(!map.containsKey(key)) return false;
		return map.get(key).contains(value);
	}

	public void clear()
	{
		map.clear();
	}

	public Iterator<V> iterator(K key)
	{
		if(!map.containsKey(key)) return null;
		return map.get(key).iterator();
	}

	public Iterator<V> iterator()
	{
		Iterator<V> i = new Iterator<V>()
		{
			private Iterator<K> keyIter = map.keySet().iterator();
			private Iterator<V> valIter;

			private void setNextIter()
			{
				if((valIter == null) || !valIter.hasNext())
				{
					if(!keyIter.hasNext()) return;
					while(keyIter.hasNext())
					{
						K key = keyIter.next();
						Set<V> s = map.get(key);
						if(s != null)
						{
							valIter = s.iterator();
							if(valIter.hasNext())
								break;
						}
					}
				}
			}

			@Override
			public boolean hasNext()
			{
				setNextIter();
				if((valIter == null) || !valIter.hasNext())
					return false;
				return true;
			}

			@Override
			public V next()
			{
				setNextIter();
				if((valIter != null) && valIter.hasNext())
					return valIter.next();
				return null;
			}

		};
		return i;
	}

	@Override
	public int hashCode()
	{
		return map.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if(o == this) return true;
		if(!(o instanceof HashMapSet)) return false;
		HashMapSet h = (HashMapSet) o;
		return h.map.equals(map);
	}
}
