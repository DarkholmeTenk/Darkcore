package io.darkcraft.darkcraft.mod.common.spellsystem.interfaces;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;

public interface ISpellShape
{
	/**Gets the coefficient of the spell cost for the exponent given.
	 * Spells will normally have exponents 0 (linear), 1 (constant) and 2 (quadratic).
	 * The spells cost (so far) will be taken as a the x value in this quadratic equation which will return the new cost 
	 * @param exponent
	 * @return the value to multiply (oldspellcost)^exponent by to get the new spell cost.
	 */
	public double getCostCoefficient(int exponent);
	
	/**Returns the locations which this shape should affect given the originalLocation, e.g. a zone spell effect affecting the
	 * resultant location of a touch spell.
	 * @param originalLocation the original location which this shape should modify
	 * @return a set of the new locations which the spell should apply over
	 */
	public Set<SimpleDoubleCoordStore> getNewLocations(SimpleDoubleCoordStore originalLocation);
	
	/**Returns the locations that this spell should affect given the entity which is doing the casting.
	 * This should only be called for the first shape in the spell.
	 * @param ent
	 * @return
	 */
	public Set<SimpleDoubleCoordStore> getLocations(EntityLivingBase ent);
	
	/**Return a list of the entities affected by the result of applying this shape to this location
	 * 
	 */
	public Set<EntityLivingBase> getAffectedEnts(SimpleDoubleCoordStore pos);
	
	/**Return a list of the block coordinates affected as the result of applying this shape to this location
	 * 
	 */
	public Set<SimpleCoordStore> getAffectedBlocks(SimpleDoubleCoordStore pos);
	
	/**Applies the modifier in question to the effect.
	 * Should be called before the effect is applied.
	 * The effect in question should keep track of the modifiers which have been applied and modify its other code.
	 * Should only be called once.
	 */
	public void applyModifiers(List<ISpellModifier> modifiers);
	
	/**The number of ticks over which this shape should apply. <=1 means the effect will only run for the tick in which it hits.
	 * @return an int specifying the number of ticks which it should take place over
	 */
	public int getDuration();
	
	public String getID();
	public ISpellShape create();
}
