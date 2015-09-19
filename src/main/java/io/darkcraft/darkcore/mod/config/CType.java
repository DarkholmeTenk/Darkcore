package io.darkcraft.darkcore.mod.config;

import io.darkcraft.darkcore.mod.exception.MalformedConfigException;

public enum CType
{
	INT("I"), DOUBLE("D"), STRING("S"), BOOLEAN("B");

	private final String	printable;

	private CType(String s)
	{
		printable = s;
	}

	public String getPrintable()
	{
		return printable;
	}

	public static CType fromPrintable(String pr)
	{
		for (CType c : values())
			if (c.getPrintable().equalsIgnoreCase(pr)) return c;
		throw new MalformedConfigException("Unrecognised data type: '" + pr + "'");
	}

	public Object toData(String data)
	{
		if (CType.BOOLEAN == this) return Boolean.parseBoolean(data);
		if (CType.DOUBLE == this) return Double.parseDouble(data);
		if (CType.INT == this) return Integer.parseInt(data);
		return data;
	}
}
