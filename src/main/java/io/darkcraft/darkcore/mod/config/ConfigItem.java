package io.darkcraft.darkcore.mod.config;

public class ConfigItem
{
	private final String	id;
	private final CType		type;
	private Object			val;
	private final String	com;

	public ConfigItem(String identifier, CType t, Object defaultVal, String... comments)
	{
		id = identifier;
		type = t;
		if (t.equals(CType.DOUBLE) && defaultVal instanceof Integer) defaultVal = Double.valueOf((Integer) defaultVal);
		val = defaultVal;
		String comStr = null;
		if (comments != null && comments.length > 0)
		{
			comStr = parseComments(defaultVal, comments);
		}
		com = comStr;
	}

	private String parseComments(Object defaultVal, String... comments)
	{
		String finalStr = "";
		boolean started = false;
		boolean defaultIncluded = false;
		for (String s : comments)
		{
			String[] d = s.split("\n");
			for (String m : d)
			{
				if (m.toLowerCase().contains("default:")) defaultIncluded = true;
				if (m.startsWith("#"))
					finalStr += (started ? "\n" : "") + m;
				else
					finalStr += (started ? "\n" : "") + "#" + m;
				started = true;
			}
		}
		if (!defaultIncluded) finalStr += (started ? "\n" : "") + "#Default: " + defaultVal.toString();
		return finalStr;
	}

	public String getID()
	{
		return id;
	}

	public String getComment()
	{
		return com;
	}

	public Boolean getBoolean()
	{
		if (CType.BOOLEAN.equals(type)) return (Boolean) val;
		return null;
	}

	public Double getDouble()
	{
		if (CType.DOUBLE.equals(type)) return (Double) val;
		return null;
	}

	public Integer getInt()
	{
		if (CType.INT.equals(type)) return (Integer) val;
		return null;
	}

	public String getString()
	{
		if (CType.STRING.equals(type)) return (String) val;
		return null;
	}

	public int hashcode()
	{
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ConfigItem) return id.equals(((ConfigItem) o).getID());
		return false;
	}

	protected String printable()
	{
		StringBuilder sb;
		if (com != null)
			sb = new StringBuilder(getComment()).append("\n");
		else
			sb = new StringBuilder();
		sb.append(id).append(":").append(type.getPrintable()).append(":");
		sb.append(val.toString());
		sb.append("\n\n");
		return sb.toString();
	}
}
