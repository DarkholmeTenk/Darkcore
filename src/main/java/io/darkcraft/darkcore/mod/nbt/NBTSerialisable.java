package io.darkcraft.darkcore.mod.nbt;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface NBTSerialisable
{
	/**
	 * If this is true then the class currently being saved will be added in to the NBT data.<p>
	 *
	 * This should only really be used when you're going to be instantiating some subclass of an object without
	 * actually knowing what it is.
	 * @return
	 */
	boolean includeType() default false;

	/**
	 * If this is true then a new instance will be created whenever the type is deserialised
	 * @return
	 */
	boolean createNew() default false;
}

