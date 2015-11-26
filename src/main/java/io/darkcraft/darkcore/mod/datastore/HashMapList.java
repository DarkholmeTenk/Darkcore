package io.darkcraft.darkcore.mod.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HashMapList<K,V>
{
	private final HashMap<K,List<V>> map = new HashMap();
	private final Iterator<V> deadIter = new Iterator<V>(){@Override
	public boolean hasNext(){return false;} @Override
	public V next(){return null;}};

	public List<V> put(K key, List<V> values)
	{
		if(values == null) return null;
		return map.put(key, values);
	}

	public List<V> get(K key)
	{
		return map.get(key);
	}

	public boolean add(K key, V value)
	{
		if(!map.containsKey(key))
			put(key, new ArrayList<V>());
		return get(key).add(value);
	}

	public List<V> removeKey(K key)
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

	public int size(K key)
	{
		if(!map.containsKey(key)) return 0;
		return map.get(key).size();
	}

	public Iterator<V> iterator(K key)
	{
		if(!map.containsKey(key)) return deadIter;
		return map.get(key).iterator();
	}

	public Iterator<V> iterator()
	{
		Iterator<V> i = new Iterator<V>()
		{
			private Iterator<K> keyIter = map.keySet().iterator();
			private Iterator<V> prevIter;
			private Iterator<V> valIter;

			private void setNextIter()
			{
				if((valIter == null) || !valIter.hasNext())
				{
					if(!keyIter.hasNext()) return;
					while(keyIter.hasNext())
					{
						K key = keyIter.next();
						List<V> s = map.get(key);
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
				prevIter = valIter;
				if((valIter != null) && valIter.hasNext())
					return valIter.next();
				return null;
			}

			@Override
			public void remove()
			{
				prevIter.remove();
			}

		};
		return i;
	}

	public Set<K> keySet()
	{
		return map.keySet();
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
		if(!(o instanceof HashMapList)) return false;
		HashMapList h = (HashMapList) o;
		return h.map.equals(map);
	}
}
