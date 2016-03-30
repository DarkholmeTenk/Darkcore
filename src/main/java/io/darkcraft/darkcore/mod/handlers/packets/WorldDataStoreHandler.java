package io.darkcraft.darkcore.mod.handlers.packets;

import io.darkcraft.darkcore.mod.abstracts.AbstractWorldDataStore;
import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;

public class WorldDataStoreHandler implements IDataPacketHandler
{
	public static final String disc = "core.awds";

	@SuppressWarnings("unchecked")
	private static HashMap<Pair<Integer,String>,AbstractWorldDataStore> map = new HashMap();

	public static void clear()
	{
		map.clear();
	}

	public static boolean register(AbstractWorldDataStore store)
	{
		if(store == null) return false;
		Pair<Integer,String> key = new Pair<Integer,String>(store.getDimension(),store.getName());
		if(map.containsKey(key)) return false;
		map.put(key, store);
		return true;
	}

	public static AbstractWorldDataStore get(Integer dim, String name)
	{
		Pair<Integer,String> key = new Pair<Integer,String>(dim,name);
		return map.get(key);
	}

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(data.hasKey("AWDSdim") && data.hasKey("AWDSname"))
		{
			int dim = data.getInteger("AWDSdim");
			String name = data.getString("AWDSname");
			//System.out.print("RP:"+dim+":"+name);
			AbstractWorldDataStore awds = get(dim,name);
			if(awds != null)
			{
				//System.out.println("#NN");
				if(ServerHelper.isClient())
				{

					if(!ServerHelper.isIntegratedClient())
						awds.readFromNBT(data);
				}
				else
					awds.sendUpdate();
			}
			else
				System.out.println("#N");
		}
	}
}
