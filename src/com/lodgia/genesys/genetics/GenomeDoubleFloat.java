package com.lodgia.genesys.genetics;

import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.lib.Logger;

public class GenomeDoubleFloat implements InterfaceGenericGenome {

	public double floats[];
	int  length=12;
	//java.util.Random r;
	int debugLevel;
	Logger l;
	String name;
	
	public GenomeDoubleFloat(int pLength,java.util.Random randomgenerator, int pDebugLevel)
	{
		 
		name = System.currentTimeMillis() + "";
		debugLevel=pDebugLevel;
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
		
		length=pLength;
		floats = new double[ length ];
		
		if( randomgenerator != null ) {
			randomize( randomgenerator );
		}

	}
	
	public GenomeDoubleFloat getCopy( )
	{
		 
		GenomeDoubleFloat copy;
		
		copy=new GenomeDoubleFloat(length,null,debugLevel);
		copy.l=l;
		copy.floats= new double[ length ];
		for(int t=0; t<length; t++) {
			copy.floats[ t ] = floats[ t ]; 
		}
		
		copy.name="C:"+this.name;
		copy.name="Copy";
		return copy;
		
	}
	
	
	public void randomize( java.util.Random randomgenerator)
	{
			
		for(int t=0; t<length; t++)
		{
			floats[ t ] = getrandomgene( randomgenerator );
		}
	
	}
	
	private double getrandomgene( java.util.Random randomgenerator ) {
		int x;
		boolean negative;
		double gene;
		negative = randomgenerator.nextBoolean();
		x = randomgenerator.nextInt(10000);
		if( negative ) {
			gene = (double) -x / 10000.0;
		}
		else {
			gene = (double)  x / 10000.0;
			
		}
		return gene;
	}
	
	
	public void mutate1genome( java.util.Random randomgenerator )
	{

		double d;
		int genome=randomgenerator.nextInt(length);
		
		d=getrandomgene( randomgenerator );
		floats[ genome ] = ( 2 * ( d /9 ))+ (7 * (floats[ genome ]/9 ) );

		this.name="M1:"+this.name;
	}	
	
	
	public InterfaceGenericGenome produceChildSimple(java.util.Random randomgenerator, InterfaceGenericGenome imate)
	{
		int splitpoint;
		boolean order12;
		String part1,part2;
		GenomeDoubleFloat mate;
		GenomeDoubleFloat child;
		
		mate=(GenomeDoubleFloat) imate;
		child=new GenomeDoubleFloat(length,randomgenerator,debugLevel);
		
		order12=randomgenerator.nextBoolean();
		splitpoint=randomgenerator.nextInt(length-1)+1;
		
		if(order12)
		{
			for(int t=0; t<length; t++) {
				if(t<splitpoint) {
					child.floats[ t ] = mate.floats[ t ];
				}
				else {
					child.floats[ t ] = this.floats[ t ];
				}
			}
		}
		else
		{
			for(int t=0; t<length; t++) {
				if(t<splitpoint) {
					child.floats[ t ] = this.floats[ t ];
				}
				else {
					child.floats[ t ] = mate.floats[ t ];
				}
			}		
		}
		
		child.name="Child";
		return (InterfaceGenericGenome) child;
		
	}
	
	
	public String getAsString()
	{
		String mystring;
		mystring = name;
		for(int t=0; t<length; t++) {
			if( mystring.length() != 0) {
				mystring = mystring + ", ";
			}
			mystring = mystring + Double.toString( floats[t] );
		}
		
		return "GenomeDoubleFloat("+mystring+") ";
	}


	public boolean equals(InterfaceGenericGenome iother) {
		GenomeDoubleFloat other;
		
		other=(GenomeDoubleFloat) iother;
		
		for(int t=0; t<length; t++) {
			if(other.floats[ t ] != this.floats[ t ] ) {
				return false;
			}
		}
		return true;
	}

	//public String getRawString() {
//
	//	return bits;
	//}

	/*public int distance(InterfaceGenericGenome other) {
		int diff;
		
		diff = 0;
		
		for(int t=0; t<length; t++) {
			if(s1.substring(t, t) != s2.substring(t, t)) {
				diff++;
			}
		}
		
		return diff;
	}*/
	

}
