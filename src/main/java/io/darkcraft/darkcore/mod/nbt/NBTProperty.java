package io.darkcraft.darkcore.mod.nbt;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface NBTProperty
{
	SerialisableType[] value() default {SerialisableType.WORLD, SerialisableType.TRANSMIT};

	String name() default "";

	public static enum SerialisableType
	{
		WORLD,
		TRANSMIT;

		public boolean valid(NBTProperty property)
		{
			for(SerialisableType t : property.value())
				if(t == this)
					return true;
			return false;
		}
	}
}
