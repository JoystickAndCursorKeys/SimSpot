package com.lodgia.genesys.genetics;

public class Enc_GrayBin {
	
	    	    	
	    public static String binStrToGrayStr(String binstr)
	    {
	    	String graystr;
	    	
	    	boolean bin[], gray[];
	    	
	    	bin=new boolean[binstr.length()];
	    	    	
	    	for(int t=0; t<bin.length; t++)
	    	{
	    		bin[t]= binstr.substring(t, t+1).equals("1");
	    	}    	
	    	
	    	gray=new boolean[binstr.length()];
	    	
	    	gray[0]=bin[0];
			
	    	for(int t=1; t<gray.length; t++)
	    	{
	    		gray[t]= bin[t-1] ^ bin[t];
	    	}
	    	
	    	graystr="";
	    	
	    	for(int t=0; t<gray.length; t++)
	    	{
	    		if(gray[t])
	    		{
	    			graystr+="1";
	    		}
	    		else
	    		{
	    			graystr+="0";
	    		}    		
	    		
	    	}    
	    	
	    	return graystr;
	    }
	    
	    
	    public static String grayStrToBinStr(String graystr)
	    {
	    	String binstr;
	    	
	    	boolean bin[], gray[];
	    	
	    	gray=new boolean[graystr.length()];
	    	
	    	for(int t=0; t<gray.length; t++)
	    	{
	    		gray[t]= graystr.substring(t, t+1).equals("1");
	    	}
	    	
	    	bin= new boolean[graystr.length()];
	    	
	    	bin[0]=gray[0];
	    		
	    	for(int t=1; t<bin.length; t++)
	    	{
	    		bin[t]= bin[t-1] ^ gray[t];
	    	}
	    	
	    	binstr="";
	    	
	    	for(int t=0; t<bin.length; t++)
	    	{
	    		if(bin[t])
	    		{
	    			binstr+="1";
	    		}
	    		else
	    		{
	    			binstr+="0";
	    		}    		
	    		
	    	}    	
	    	
	    	return binstr;
	    }
}
