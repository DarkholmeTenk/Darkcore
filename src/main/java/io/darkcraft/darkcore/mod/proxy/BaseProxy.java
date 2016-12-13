package io.darkcraft.darkcore.mod.proxy;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

@SuppressWarnings("unchecked")
public class BaseProxy
{
	private static ClassToInstanceMap<BaseProxy> map = MutableClassToInstanceMap.create();
	
	{
		Class<?> c = this.getClass();
		while(c != null && c != BaseProxy.class && BaseProxy.class.isAssignableFrom(c))
		{
			map.put((Class<? extends BaseProxy>) c, this);
			c = c.getSuperclass();
		}
	}
	
	public static BaseProxy getProxy(String name)
	{
		try
		{
			Class<?> c = Class.forName(name);
			return map.get(c);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		
	}
}
