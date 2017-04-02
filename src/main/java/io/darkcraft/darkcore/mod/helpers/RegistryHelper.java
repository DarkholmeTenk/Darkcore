package io.darkcraft.darkcore.mod.helpers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;

public class RegistryHelper
{
	private static Map<Class<?>, Object> instanceMap = new HashMap<>();
	private static Table<Class<?>, String, Object> constructedMap = HashBasedTable.create();

	public static void fillIn(Object o)
	{
		fillIn(o, o.getClass());
	}

	public static void fillIn(Class<?> c)
	{
		fillIn(null, c);
	}

	private static void fillIn(Object o, Class<?> c)
	{
		try
		{
			for(Field f : c.getDeclaredFields())
			{
				if(f.isAnnotationPresent(RegistryIgnore.class))
					continue;
				CustomRegistryItem annotation = f.getAnnotation(CustomRegistryItem.class);
				if((annotation != null) && !f.isAccessible())
					throw new RuntimeException(c.getName() + "." + f.getName() + " is not accessible for RegistryHelper");
				Class<?> fieldClass = f.getType();
				if(!(AbstractBlock.class.isAssignableFrom(fieldClass) || AbstractItem.class.isAssignableFrom(fieldClass)))
				{
					System.out.println("In class " + c.getName() + " field " + f.getName() + " is not assignable");
					continue;
				}
				Object instance = generate(annotation, fieldClass);
				if(instance instanceof AbstractBlock)
					((AbstractBlock)instance).register();
				else if(instance instanceof AbstractItem)
					((AbstractItem)instance).register();
				f.set(o, generate(annotation, fieldClass));
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException("Exception filling in registry", e);
		}
	}

	private synchronized static Object generate(CustomRegistryItem annot, Class<?> clazz) throws Exception
	{
		Object o;
		if(annot == null)
		{
			if((o = instanceMap.get(clazz)) == null)
			{
				o = clazz.newInstance();
				instanceMap.put(clazz, o);
			}
		}
		else
		{
			if((o = constructedMap.get(clazz, annot.value())) == null)
			{
				o = clazz.getConstructor(String.class).newInstance(annot.value());
				constructedMap.put(clazz, annot.value(), o);
			}
		}
		return o;
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface CustomRegistryItem
	{
		public String value() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface RegistryIgnore {}
}
