package io.darkcraft.darkcraft.mod.common.spellsystem.components.shapes;

import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;

public class Zone extends Area
{
	@Override
	public String getID()
	{
		return "zone";
	}
	
	@Override
	public ISpellComponent create()
	{
		System.out.println("NZ!");
		return new Zone();
	}
	
	@Override
	public double getCostCoefficient(int exponent)
	{
		switch(exponent)
		{
			case 2 : return 0.75;
			case 1 : return 0.5;
			default : return 0;
		}
	}
	
	@Override
	public int getDuration()
	{
		return 8;
	}
}
