package io.darkcraft.darkcore.mod.impl;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class UniqueSwordItem extends AbstractItem
{
	private static HashMap<String, double[]>	cMap	= new HashMap();

	static
	{
		cMap.put(null, new double[] { 1, 1, 1 });
		cMap.put(DarkcoreMod.authName, new double[] { 0.3, 0.5, 1 });
		cMap.put("AlienXtream", new double[] { 0.2, 1, 0.3 });
		cMap.put("TorkSlanter", new double[] { 1, 0.1, 0.1 });
		cMap.put("Jwtc2K", new double[] { 0.1, 0.1, 0.1 });
		cMap.put("Kilynn", new double[] { 1, 0.1, 1 });
		cMap.put("Barbrule", new double[] { 1, 1, 0 });
		cMap.put("Stancie", new double[] { 0, 1, 1 });
		cMap.put("FoxPotato", new double[] { 1, 0.6, 0 });
	}

	public UniqueSwordItem()
	{
		super(DarkcoreMod.modName);
		setUnlocalizedName("UniqueSword");
		setMaxStackSize(1);
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	public static boolean isValid(Object o)
	{
		if(!PlayerHelper.swordsEnabled()) return false;
		String name;
		if (o instanceof String)
			name = (String) o;
		else if (o instanceof EntityPlayer)
			name = ServerHelper.getUsername((EntityPlayer) o);
		else
			return false;
		if ((name == null) || name.isEmpty()) return false;
		return cMap.containsKey(name);
	}

	@Override
	public boolean isItemTool(ItemStack is)
	{
		return true;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.block;
	}

	private void removeItemFromPlayer(EntityLivingBase pl, ItemStack is)
	{
		if ((pl == null) || (is == null) || (pl.getHeldItem() != is)) return;
		pl.setCurrentItemOrArmor(0, null);
		// pl.inventory.decrStackSize(pl.inventory.currentItem, is.stackSize);
	}

	@Override
	public boolean hitEntity(ItemStack is, EntityLivingBase hitee, EntityLivingBase hitter)
	{
		if (!(hitter instanceof EntityPlayer) || !isValid(hitter))
		{
			removeItemFromPlayer(hitter, is);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (!isValid(player))
		{
			removeItemFromPlayer(player, stack);
			return true;
		}
		/*
		 * if(entity instanceof EntityLivingBase) { EntityLivingBase ent = (EntityLivingBase) entity; ent.attackEntityFrom(DamageSource.generic, 20); }
		 */
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer pl)
	{
		if (!isValid(pl))
		{
			removeItemFromPlayer(pl, is);
			return is;
		}
		if (ServerHelper.isClient()) return is;
		/*
		double dist = 40;
		float yaw = pl.rotationYaw;
		float pit = pl.rotationPitch;
		double xo = Math.cos(Math.toRadians(pl.rotationYaw + 90));
		double yo = Math.cos(Math.toRadians(pl.rotationPitch + 90));
		double zo = Math.sin(Math.toRadians(pl.rotationYaw + 90));
		Vec3 start = Vec3.createVectorHelper(pl.posX, pl.posY + pl.eyeHeight, pl.posZ);
		Vec3 end = Vec3.createVectorHelper(pl.posX + (dist * xo), pl.posY + pl.eyeHeight + (dist * yo), pl.posZ + (dist * zo));
		MovingObjectPosition mop = w.rayTraceBlocks(start, end);
		pl.fallDistance = 0;
		if ((mop == null) || (mop.typeOfHit == MovingObjectType.MISS))
		{
			pl.setPositionAndUpdate(end.xCoord, end.yCoord, end.zCoord);
		}
		else if (mop.typeOfHit == MovingObjectType.BLOCK)
		{
			if (mop.sideHit == 1)
				pl.setPositionAndUpdate(mop.blockX + 0.5, mop.blockY + 1, mop.blockZ + 0.5);
			else if (mop.sideHit == 0)
				pl.setPositionAndUpdate(mop.blockX + 0.5, mop.blockY - 2, mop.blockZ + 0.5);
			else if (mop.sideHit == 2)
				pl.setPositionAndUpdate(mop.blockX + 0.5, mop.blockY, mop.blockZ - 0.5);
			else if (mop.sideHit == 3)
				pl.setPositionAndUpdate(mop.blockX + 0.5, mop.blockY, mop.blockZ + 1.5);
			else if (mop.sideHit == 4)
				pl.setPositionAndUpdate(mop.blockX - 0.5, mop.blockY, mop.blockZ + 0.5);
			else if (mop.sideHit == 5) pl.setPositionAndUpdate(mop.blockX + 1.5, mop.blockY, mop.blockZ + 0.5);
		}*/
		return is;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
	{
		return isValid(player);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		if (!isValid(entityLiving))
		{
			removeItemFromPlayer(entityLiving, stack);
			return true;
		}
		return false;
	}

	@Override
	public Multimap getItemAttributeModifiers()
	{
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 160, 0));
		return multimap;
	}

	private static long	last	= 0;

	private static double getC(String pl, int n)
	{
		if (cMap.containsKey(pl)) return cMap.get(pl)[n];
		return cMap.get(null)[n];
	}

	public static double getRed(String pl)
	{
		return getC(pl, 0);
	}

	public static double getGre(String pl)
	{
		return getC(pl, 1);
	}

	public static double getBlu(String pl)
	{
		return getC(pl, 2);
	}
}
