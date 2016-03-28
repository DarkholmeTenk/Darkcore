package io.darkcraft.darkcore.mod.helpers;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.interfaces.IBlockIteratorCondition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockIterator implements Iterator<SimpleCoordStore>
{
	public static final IBlockIteratorCondition	sameIncMeta	= new SameIncMetaCondition();
	public static final IBlockIteratorCondition	sameExcMeta	= new SameExMetaCondition();
	public static final IBlockIteratorCondition	sameExcMetaNS = new SameExMetaConditionNS();

	private final boolean						diagonals;
	private final IBlockIteratorCondition		cond;
	private final SimpleCoordStore				start;
	private final int							maxDist;

	private HashSet<SimpleCoordStore>			done		= new HashSet<SimpleCoordStore>();
	private Queue<SimpleCoordStore>				queue		= new LinkedBlockingQueue<SimpleCoordStore>();

	public BlockIterator(SimpleCoordStore _start, IBlockIteratorCondition _cond, boolean doDiagonals, int maxDistance)
	{
		cond = _cond;
		diagonals = doDiagonals;
		start = _start;
		maxDist = maxDistance;
		queue.add(_start);
	}

	@Override
	public synchronized boolean hasNext()
	{
		return !queue.isEmpty();
	}

	private void add(SimpleCoordStore from, SimpleCoordStore n)
	{
		if (done.contains(n)) return;
		if (start.diagonalParadoxDistance(n) > maxDist) return;
		if (cond.isValid(start, from, n))
		{
			done.add(n);
			queue.add(n);
		}
	}

	private void addNearby(SimpleCoordStore point)
	{
		if (!diagonals)
			for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
				add(point, point.getNearby(d));
		else
			for (int x = -1; x <= 1; x++)
				for (int y = -1; y <= 1; y++)
					for (int z = -1; z <= 1; z++)
					{
						if ((x == 0) && (y == 0) && (z == 0)) continue;
						add(point, new SimpleCoordStore(point.world, point.x + x, point.y + y, point.z + z));
					}
	}

	@Override
	public synchronized SimpleCoordStore next()
	{
		if (queue.isEmpty()) return null;
		SimpleCoordStore top = queue.remove();
		addNearby(top);
		return top;
	}

	private static class SameIncMetaCondition implements IBlockIteratorCondition
	{
		@Override
		public boolean isValid(SimpleCoordStore start, SimpleCoordStore prevPosition, SimpleCoordStore newPosition)
		{
			return prevPosition.getBlock().equals(newPosition.getBlock()) && (prevPosition.getMetadata() == newPosition.getMetadata());
		}

	}

	private static class SameExMetaCondition implements IBlockIteratorCondition
	{
		@Override
		public boolean isValid(SimpleCoordStore start, SimpleCoordStore prevPosition, SimpleCoordStore newPosition)
		{
			return prevPosition.getBlock().equals(newPosition.getBlock());
		}

	}

	private static class SameExMetaConditionNS implements IBlockIteratorCondition
	{
		@Override
		public boolean isValid(SimpleCoordStore start, SimpleCoordStore prevPosition, SimpleCoordStore newPosition)
		{
			return prevPosition.getBlock().equals(newPosition.getBlock()) && (newPosition.getMetadata()!=start.getMetadata());
		}

	}

}
