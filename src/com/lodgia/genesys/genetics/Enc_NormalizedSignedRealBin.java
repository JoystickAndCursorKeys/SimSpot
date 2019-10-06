package com.lodgia.genesys.genetics;



public class Enc_NormalizedSignedRealBin {
	
		
	static public String realToBinStr(double p_real,double precisionfact,int strlen)
	    {
		 

		 	double real;
		 	
		 	real=p_real;
		 	
		 	if(real<-1.0) {real=-1.0;}
		 	if(real>1.0)  {real=1.0;}
		 	
		 	real=real+1.0;
		 	
		 	int dec;
		 	
		 	dec=(int) (real * precisionfact);
			
	    	return Enc_DecBin.decToBinStr(dec, strlen);
	    	
	    }
	    
	    public static double binStrToReal(String binstr,double precisionfact)
	    {
	
	    	int dec;
	    	double real;
	    	
	    	dec=Enc_DecBin.binStrToDec(binstr);
	    	
	    	real=dec;
	    	real=real / precisionfact;
	   	    	
	    	real=real-1.0;
	    	
	    	return real;
	    }    	    	
	
}

/*
 * 		Sandbox comment to store test code for adding new encoders
 * 
 * 		String binstr;
    	String graystr;
    	int dec;
    	double real;
    	
    	graystr="110011";
    	
    	binstr=BaseEnc_GrayBin.grayStrToBinStr(graystr);
    	
    	dec=BaseEnc_DecBin.binStrToDec(binstr);
    	
    	System.out.println("gray is= "+graystr);
    	System.out.println("bin is=  "+binstr);
    	System.out.println("dec is "+dec);
    	
    	binstr=BaseEnc_DecBin.decToBinStr(dec, 7);
    	System.out.println("bin2 is=  "+binstr);
    	
    	dec=BaseEnc_DecBin.binStrToDec(binstr);
    	System.out.println("dec2 is "+dec);
    	
    	graystr=BaseEnc_GrayBin.binStrToGrayStr(binstr);
    	
    	System.out.println("gray2 is= "+graystr);
    	
    	double real0, real1;
    	double maxdev,dev;
    	double maxdevvalue0=-3.14;
    	double maxdevvalue1=-3.14;    	
    	maxdev=0.0;
    	
    	for(real0=-1.0; real0<=1.0; real0=real0+0.001 )
    	{
    		
    		binstr=Enc_NormalizedSignedRealBin12.realToGraybinStr(real0);
    		real1=Enc_NormalizedSignedRealBin12.graybinStrToReal(binstr);
    	
    		dev=Math.abs(real0-real1);
    		if(dev>maxdev)
    		{
    			maxdev=dev;
    			maxdevvalue0=real0;
    			maxdevvalue1=real1;
    		}
    		
    	}
    	
    	System.out.println("maxdev="+maxdev+" for "+maxdevvalue0+"<>"+maxdevvalue1);
    	
    	*/

