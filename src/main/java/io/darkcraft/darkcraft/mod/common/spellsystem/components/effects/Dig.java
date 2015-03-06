package io.darkcraft.darkcraft.mod.common.spellsystem.components.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellComponent;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellEffect;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellModifier;
import io.darkcraft.darkcraft.mod.common.spellsystem.interfaces.ISpellShape;

public class Dig implements ISpellEffect
{

	@Override
	public String getID()
	{
		return "dig";
	}

	@Override
	public ISpellComponent create()
	{
		return new Dig();
	}

	@Override
	public double getBaseCost()
	{
		return 2;
	}

	@Override
	public void applyEffect(EntityLivingBase ent)
	{
	}

	@Override
	public void applyEffect(SimpleCoordStore scs)
	{
		if(scs == null)
			return;
		World w = scs.getWorldObj();
		if(w == null)
			return;
		SimpleDoubleCoordStore dropPos = scs.getCenter();
		Block b = w.getBlock(scs.x,scs.y,scs.z);
		int m = w.getBlockMetadata(scs.x,scs.y,scs.z);
		if(b.getBlockHardness(w, scs.x, scs.y, scs.z) >= 0)
		{
			ArrayList<ItemStack> drops = b.getDrops(w, scs.x, scs.y, scs.z, m, 0);
			w.setBlockToAir(scs.x, scs.y, scs.z);
			for(ItemStack is : drops)
			{
				if(is != null)
					WorldHelper.dropItemStack(is, dropPos);
			}
		}
	}

	@Override
	public int getInterval()
	{
		return 1;
	}

	@Override
	public void applyModifiers(List<ISpellModifier> modifiers)
	{
		// TODO Auto-generated method stub
		
	}
}
