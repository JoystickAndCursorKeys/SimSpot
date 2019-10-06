package com.lodgia.genesys.genetics;

import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.lib.Logger;

public class GenomeBitString implements InterfaceGenericGenome {

	public String bits;
	int  length=12;
	//java.util.Random r;
	int debugLevel;
	Logger l;
	
	public GenomeBitString(int pLength,java.util.Random randomgenerator, int pDebugLevel)
	{
		 
		debugLevel=pDebugLevel;
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
		
		length=pLength;
		bits="";
		
		if( randomgenerator != null ) {
			randomize( randomgenerator );
		}
	}
	
	public GenomeBitString getCopy( )
	{
		 
		GenomeBitString copy;
		
		copy=new GenomeBitString(length,null,debugLevel);
		copy.l=l;
		copy.bits=new String(bits);
		
		return copy;
		
	}
	
	
	public void randomize( java.util.Random randomgenerator)
	{
	
		bits="";
		for(int t=0; t<length; t++)
		{

			if(randomgenerator.nextBoolean())
			{
				bits+="1";
			}
			else
			{
				bits+="0";
			}
		}
	
	} 
	
	public void mutate1genome( java.util.Random randomgenerator )
	{
		String newbits="";
				
		//System.out.println("bits="+bits);
		
		int bitpos=randomgenerator.nextInt(length);
		
		for(int t=0; t<length; t++)
		{
			//System.out.println("here("+t+"/"+length+")"+bits.substring(t, t+1));
			if(t==bitpos)
			{
				
				int bitvalue=Integer.parseInt(bits.substring(t, t+1));
				
				bitvalue=1-bitvalue;
				
				newbits+=""+bitvalue;				
			}
			else
			{
				newbits+=bits.substring(t, t+1);
			}
		}
	
		bits=newbits;
	}	
	
	private void setString(String s)
	{
		bits=s;
		length=s.length();
		
	}
	
	public InterfaceGenericGenome produceChildSimple(java.util.Random randomgenerator, InterfaceGenericGenome imate)
	{
		int splitpoint=randomgenerator.nextInt(length-1)+1;
		boolean order12;
		String part1,part2;
		GenomeBitString mate;
		
		mate=(GenomeBitString) imate;
		
		order12=randomgenerator.nextBoolean();

		
		if(order12)
		{
			part1=mate.bits.substring(0,splitpoint);		
			part2=this.bits.substring(splitpoint);
		}
		else
		{
			part1=this.bits.substring(0,splitpoint);		
			part2=mate.bits.substring(splitpoint);			
		}

		GenomeBitString child;
		
		child=new GenomeBitString(-1,randomgenerator,debugLevel);
		
		child.setString(part1+part2);

		return (InterfaceGenericGenome) child;
		
	}	
	
	public String getAsString()
	{
		return "GenomeBitString("+bits+") ";
	}


	public boolean equals(InterfaceGenericGenome iother) {
		GenomeBitString other;
		
		other=(GenomeBitString) iother;
		
		return other.bits.equals( this.bits );
	}

	public String getRawString() {

		return bits;
	}

	/*public int distance(InterfaceGenericGenome other) {
		String s1;
		String s2;
		int diff;
		
		s1 = getRawString();
		s2 = other.getRawString();
		
		diff = 0;
		
		for(int t=0; t<length; t++) {
			if(s1.substring(t, t) != s2.substring(t, t)) {
				diff++;
			}
		}
		
		return diff;
	}*/
	
	
}
