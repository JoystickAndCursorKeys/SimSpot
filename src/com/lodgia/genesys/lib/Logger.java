package com.lodgia.genesys.lib;

public class Logger {
	
	final static public int LEVEL_ALL=100;	
	final static public int LEVEL_DEBUG=10;
	final static public int LEVEL_INFO=5;	
	final static public int LEVEL_WARNING=2;
	final static public int LEVEL_ERRORS=1;
	final static public int LEVEL_FATALS=0;
	final static public int LEVEL_NONE=-1;
			
	String identity;
	int debuglevel;
	
	
	public Logger(String pIdentity, int pDebuglevel)
	{
		init(pIdentity,  pDebuglevel);
	}
	
	public void init(String pIdentity, int pDebuglevel)
	{
		identity=pIdentity;
		debuglevel=pDebuglevel;
	}
	
	public void debug(String text)
	{
				
		if(debuglevel<LEVEL_DEBUG)
		{
			return;
		}
		
		
		String type="DEBUG";
		lograwout(text, type);
	}
	
	public void logerror(String text)
	{
		String type="ERROR";
		if(debuglevel<LEVEL_ERRORS)
		{
			return;
		}
		
		lograwerr(text, type);
	}
	
	public void logfatal(String text)
	{
		String type="FATAL";
		if(debuglevel<LEVEL_FATALS)
		{
			return;
		}
		
		lograwerr(text, type);
	}
	
	public void info(String text)
	{
		String type="INFO";
		if(debuglevel<LEVEL_INFO)
		{
			return;
		}
		
		lograwout(text, type);
	}
	
	public void warning(String text)
	{
	
		String type="WARNING";
		if(debuglevel<LEVEL_WARNING)
		{
			return;
		}
		lograwerr(text, type);
		
	}	
	
	void lograwerr(String text, String type)
	{
	
		System.out.println(padSpaces(type,10)+":("+padSpaces(identity,30)+"): "+text);
		
	}
	
	void lograwout(String text, String type)
	{
	
		System.out.println(padSpaces(type,10)+":("+padSpaces(identity,30)+"): "+text);
		
	}	
	
	public String padSpaces(String text, int len)
	{
		int t;
		String txt2="";
		
		for(t=text.length();t<len; t++)
		{
			txt2=txt2+" ";
		}
		
		return text+txt2;
	}	
	
}
