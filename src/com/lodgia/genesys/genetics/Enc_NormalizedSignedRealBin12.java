package com.lodgia.genesys.genetics;


public class Enc_NormalizedSignedRealBin12 {
	
	static final int PRECISIONCONST=2000;
	static final int BINSTRLEN=12;
		
	static public String realToBinStr(double p_real)
    {
	 
		return Enc_NormalizedSignedRealBin.realToBinStr(p_real, PRECISIONCONST, BINSTRLEN);
    	
    }
	    
    public static double binStrToReal(String binstr)
    {

		return Enc_NormalizedSignedRealBin.binStrToReal(binstr, PRECISIONCONST);
		
	}    	
    
	static public String realToGraybinStr(double p_real)
    {
	 
		String binstr;
		
		binstr=Enc_NormalizedSignedRealBin.realToBinStr(p_real, PRECISIONCONST, BINSTRLEN);
		
		return Enc_GrayBin.binStrToGrayStr(binstr);
    	
    }
	    
    public static double graybinStrToReal(String graystr)
    {

    	String binstr;
    	
    	binstr=Enc_GrayBin.grayStrToBinStr(graystr);
    		
		return Enc_NormalizedSignedRealBin.binStrToReal(binstr, PRECISIONCONST);
		
	}        
	
}
