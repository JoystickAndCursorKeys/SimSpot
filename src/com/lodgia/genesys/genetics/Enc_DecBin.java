package com.lodgia.genesys.genetics;

public class Enc_DecBin {
	
	 static public String decToBinStr(int dec, int positions)
	    {
		 	String binstr ;
		 	
		 	binstr="00000000000000000000000000000000000000000000000000000000000000000" + 
		 			Integer.toBinaryString(dec);
	    	
		 	
		 	int index;
		 	index=binstr.length()-positions;
		 	
	    	return binstr.substring(index);
	    	
	    }
	    
	    public static int binStrToDec(String binstr)
	    {
	
	    	return Integer.parseInt(binstr,2);
	    	
	    }
   	
	    
}
