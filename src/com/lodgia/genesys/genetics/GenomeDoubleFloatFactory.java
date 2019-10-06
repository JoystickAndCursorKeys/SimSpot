package com.lodgia.genesys.genetics;

import java.security.SecureRandom;

import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenomeFactory;
import com.lodgia.genesys.lib.Logger;
import com.lodgia.genesys.lib.MersenneTwister;

public class GenomeDoubleFloatFactory implements InterfaceGenericGenomeFactory {


	int length;
	java.util.Random r;
	int debugLevel;
	Logger l;
	
	public GenomeDoubleFloatFactory(int pLength, int pDebugLevel)
	{
		System.out.println("new GenomeDoubleFloatFactory");
		
		debugLevel=pDebugLevel;
		
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
					
		length=pLength;
		
		
		System.out.println(" GenomeDoubleFloatFactory.new MersenneTwister()!!!!");
		//r=new MersenneTwister();
		r = new SecureRandom();
		r.setSeed(System.currentTimeMillis());
	}
	
	public InterfaceGenericGenome createGenome()
	{

		GenomeDoubleFloat gbs;
		
		gbs=new  GenomeDoubleFloat(length,r,debugLevel);
		
		return (InterfaceGenericGenome) gbs;
	} 
		
	
	public InterfaceGenericGenome createGenome(String line)
	{
		GenomeDoubleFloat gdf;
		String s2,s3[];
		
		gdf=new  GenomeDoubleFloat(length,r,debugLevel);
		
		s2=line.substring(18);
		s2 = s2.substring(1, s2.length()-2);
		
		s3=s2.split(",");
		for ( int i=0; i<s3.length; i++) {
			gdf.floats[i] = Double.parseDouble( s3[i].trim() );
			
		}

		return (InterfaceGenericGenome) gdf;
	}

}
