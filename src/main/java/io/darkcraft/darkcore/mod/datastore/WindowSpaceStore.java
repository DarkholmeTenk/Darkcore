package io.darkcraft.darkcore.mod.datastore;

import net.minecraft.client.Minecraft;

public class WindowSpaceStore
{
	public final double x;
	public final double y;
	public final double scale;

	public WindowSpaceStore(double _x, double _y, double _scale)
	{
		x = _x;
		y = _y;
		scale = _scale;
	}

	public WindowSpaceStore transform(double _x, double _y)
	{
		return new WindowSpaceStore(x + (_x * scale), y + (_y * scale), scale);
	}

	public WindowSpaceStore scale(double _scale)
	{
		return new WindowSpaceStore(x,y,scale * _scale);
	}

	public double getFromBottom()
	{
		return Minecraft.getMinecraft().displayHeight - y;
	}
}
