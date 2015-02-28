package io.darkcraft.darkcraft.mod.common.spellsystem.interfaces;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;

public interface ISpellEffect
{
	/**Returns the amount of mana that this spell will take to cast
	 */
	public double getBaseCost();
	
	/**Applies the effect to the entity in question.
	 */
	public void applyEffect(EntityLivingBase ent);
	
	/**Applies the effect to the block at the coords in question
	 */
	public void applyEffect(SimpleCoordStore scs);
	
	/**Returns how long the spell instance should wait between multiple applications of this effect
	 * 
	 */
	public int getInterval();
	
	/**Applies the modifier in question to the effect.
	 * Should be called before the effect is applied.
	 * The effect in question should keep track of the modifiers which have been applied and modify its other code.
	 * Should only be called once.
	 */
	public void applyModifiers(List<ISpellModifier> modifiers);
	
	public String getID();
	public ISpellEffect create();
}
