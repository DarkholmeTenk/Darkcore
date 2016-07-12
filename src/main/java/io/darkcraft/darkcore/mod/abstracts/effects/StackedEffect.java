package io.darkcraft.darkcore.mod.abstracts.effects;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public final class StackedEffect extends AbstractEffect
{
	public static final String stackId = "darkcore.stack";
	public final String subID;
	private AbstractEffect[] effects;

	private static int getMaxDuration(AbstractEffect[] in)
	{
		int max = Integer.MIN_VALUE;
		for(AbstractEffect e : in)
		{
			if((e != null) && ((e.duration - e.tt) > max))
				max = e.duration - e.tt;
			if(e.duration == -1)
				return -1;
		}
		return max;
	}

	public StackedEffect(AbstractEffect... effects)
	{
		super(stackId, effects[0].getEntity(), getMaxDuration(effects), effects[0].visible, true, 1);
		this.effects = effects;
		subID = effects[0].id;
	}

	public static AbstractEffect addEffect(StackedEffect old, AbstractEffect newEffect)
	{
		int count = 0;
		for(AbstractEffect e : old.effects)
			if(e.tt < e.duration)
				count++;
		if(count == 0) return newEffect;
		AbstractEffect[] newArr = new AbstractEffect[count+1];
		int i = 0;
		for(AbstractEffect e : old.effects)
			if(e.tt < e.duration)
				newArr[i++] = e;
		newArr[i] = newEffect;
		return new StackedEffect(newArr);
	}

	public AbstractEffect[] getEffects()
	{
		return effects;
	}

	@Override
	public void apply()
	{
		for(AbstractEffect e : effects)
		{
			if(e.tt < e.duration)
				e.update();
			if(e.tt == e.duration)
				e.effectRemoved();
		}
	}

	@Override
	public void effectAdded()
	{
		for(AbstractEffect e : effects)
			e.effectAdded();
	}

	@Override
	public void effectRemoved()
	{
		for(AbstractEffect e : effects)
			e.effectRemoved();
	}

	@Override
	public ResourceLocation getIcon(){return effects[0].getIcon();}

	@Override
	public UVStore getIconLocation(){return effects[0].getIconLocation();}

	@Override
	protected void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("subID", subID);
		{
			int c = 0;
			for(int i = 0; i < effects.length; i++)
			{
				if(effects[i].tt == effects[i].duration) continue;
				NBTTagCompound snbt = new NBTTagCompound();
				effects[i].write(snbt);
				nbt.setTag("sub"+(c++), snbt);
			}
			nbt.setInteger("numSubs", c);
		}


	}

	@Override
	protected void readFromNBT(NBTTagCompound nbt){}

}
