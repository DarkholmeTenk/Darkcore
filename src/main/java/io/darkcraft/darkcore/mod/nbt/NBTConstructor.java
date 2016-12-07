package io.darkcraft.darkcore.mod.nbt;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(CONSTRUCTOR)
/**
 * Annotation to mark a constructor to be used for generating an object of this class from a generated mapper.<br/>
 * Values should be a list of the property names to be used.
 */
public @interface NBTConstructor
{
	String[] value();
}
