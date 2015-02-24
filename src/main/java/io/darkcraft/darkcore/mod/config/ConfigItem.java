package io.darkcraft.darkcore.mod.config;

public class ConfigItem
{
	private final String id;
	private final CType type;
	private Object val;
	private final String com;
	
	public ConfigItem(String identifier,CType t, Object defaultVal, String... comments)
	{
		id = identifier;
		type = t;
		val = defaultVal;
		String comStr = null;
		if(comments != null && comments.length >0)
		{
			comStr = parseComments(comments);
		}
		com = comStr;
	}
	
	private String parseComments(String... comments)
	{
		String finalStr = "";
		boolean started = false;
		for(String s : comments)
		{
			String[] d = s.split("\n");
			for(String m : d)
			{
				if(m.startsWith("#"))
					finalStr += (started ? "\n" : "") + m;
				else
					finalStr += (started ? "\n" : "") + "#" + m;
				started = true;
			}
		}
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
		if(CType.BOOLEAN.equals(type))
			return (Boolean) val;
		return null;
	}
	
	public Double getDouble()
	{
		if(CType.DOUBLE.equals(type))
			return (Double) val;
		return null;
	}
	
	public Integer getInt()
	{
		if(CType.INT.equals(type))
			return (Integer) val;
		return null;
	}
	
	public String getString()
	{
		if(CType.STRING.equals(type))
			return (String) val;
		return null;
	}
	
	public int hashcode()
	{
		return id.hashCode();
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof ConfigItem)
			return id.equals(((ConfigItem)o).getID());
		return false;
	}
	
	protected String printable()
	{
		StringBuilder sb;
		if(com != null)
			sb = new StringBuilder(getComment()).append("\n");
		else
			sb = new StringBuilder();
		sb.append(id).append(":").append(type.getPrintable()).append(":");
		sb.append(val.toString());
		sb.append("\n\n");
		return sb.toString();
	}
}
