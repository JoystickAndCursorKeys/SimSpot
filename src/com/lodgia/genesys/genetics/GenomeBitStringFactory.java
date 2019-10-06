package com.lodgia.genesys.genetics;

import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenomeFactory;
import com.lodgia.genesys.lib.Logger;

public class GenomeBitStringFactory implements InterfaceGenericGenomeFactory {


	int length;
	java.util.Random r;
	int debugLevel;
	Logger l;
	
	public GenomeBitStringFactory(int pLength, int pDebugLevel)
	{
		
		debugLevel=pDebugLevel;
		
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
					
		length=pLength;
		
		r=new java.util.Random();
		
	}
	
	public InterfaceGenericGenome createGenome()
	{
		GenomeBitString gbs;
		
		gbs=new  GenomeBitString(length,r,debugLevel);
		
		return (InterfaceGenericGenome) gbs;
	} 
	
	public InterfaceGenericGenome createGenome(String line)
	{
		GenomeBitString gbs;
		
		gbs=new  GenomeBitString(length,r,debugLevel);
		
		
		System.out.println("GenomeBitStringFactory.createGenome(String line) not yet implemented");
		return (InterfaceGenericGenome) gbs;
	} 
		

}
