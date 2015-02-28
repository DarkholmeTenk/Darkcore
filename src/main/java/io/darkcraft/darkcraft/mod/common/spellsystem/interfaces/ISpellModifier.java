package io.darkcraft.darkcraft.mod.common.spellsystem.interfaces;

public interface ISpellModifier
{
	/**Gets the coefficient of the spell cost for the exponent given.
	 * Spells will normally have exponents 0 (linear), 1 (constant) and 2 (quadratic).
	 * The spells cost (so far) will be taken as a the x value in this quadratic equation which will return the new cost 
	 * @param exponent
	 * @return the value to multiply (oldspellcost)^exponent by to get the new spell cost.
	 */
	public double getCostCoefficient(int exponent);
	
	/**The following 2 functions should get and set the number of times this modifier has been applied and as such its strength.
	 */
	public void setStrength(int strength);
	public int getStrength();
	
	public String getID();
	public ISpellModifier create();
}
