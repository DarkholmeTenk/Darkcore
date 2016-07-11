package io.darkcraft.darkcore.mod.helpers;

import java.lang.reflect.Field;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionHelper
{
	private static Field potionField;
	static
	{
		Potion[] nullArr = new Potion[0];
		for(Field f : Potion.class.getDeclaredFields())
		{
			if(f.getType().isInstance(nullArr))
			{
				potionField = f;
				break;
			}
			if(f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
			{
				potionField = f;
				break;
			}
		}
	}

	public static Potion[] getPotionTypes()
	{
		if(potionField == null) return null;
		try
		{
			return (Potion[]) potionField.get(null);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static int getPotionIndex(String potionName)
	{
		Potion[] potions = getPotionTypes();
		if(potions == null) return -1;
		for(int i = 0; i < potions.length; i++)
			if((potions[i] != null) && potions[i].getName().equals(potionName))
				return potions[i].getId();
		return -1;
	}

	public static boolean applyPotion(EntityLivingBase ent, String potionName, int length)
	{
		int index = getPotionIndex(potionName);
		if(index == -1) return false;
		PotionEffect pe = new PotionEffect(index, length);
		ent.addPotionEffect(pe);
		return true;
	}
}
