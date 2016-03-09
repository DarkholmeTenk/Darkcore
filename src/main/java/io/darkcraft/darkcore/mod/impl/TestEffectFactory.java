package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.abstracts.effects.IEffectFactory;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TestEffectFactory implements IEffectFactory
{

	@Override
	public AbstractEffect createEffect(EntityLivingBase ent, String id, NBTTagCompound nbt)
	{
		int dur = nbt.hasKey("dur") ? nbt.getInteger("dur") : 200;
		if(id.startsWith("testID"))
			return new TestEffect(id, ent, dur);
		return null;
	}

	private static class TestEffect extends AbstractEffect
	{
		private ResourceLocation rl;
		private static UVStore uvstore = new UVStore(0,1,0,1);
		public TestEffect(String _id, EntityLivingBase ent, int duration)
		{
			super(_id, ent, duration, true, false, 0);
			rl = new ResourceLocation(DarkcoreMod.modName, "textures/gui/effects/"+id+".png");
		}

		@Override
		public ResourceLocation getIcon()
		{
			return rl;
		}

		@Override
		public UVStore getIconLocation()
		{
			return uvstore;
		}

		@Override
		public void apply(){}

		@Override
		protected void writeToNBT(NBTTagCompound nbt){}

		@Override
		protected void readFromNBT(NBTTagCompound nbt){}

	}
}
