package com.lodgia.genesys.genetics;

import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;

public class StoredGeneticSolution implements Comparable {

	public InterfaceGenericGenome genome;
	public double historicalscore;
	public double historicaliterationaverage;
	public int historicaliteration;
	public int historicaliterationressultindex;
	
	public StoredGeneticSolution getCopy()
	{
		StoredGeneticSolution copy;
		copy=new StoredGeneticSolution();
		
		copy.genome = genome.getCopy();
		copy.historicalscore = historicalscore;
		copy.historicaliterationaverage = historicaliterationaverage;
		copy.historicaliteration = historicaliteration;
		copy.historicaliterationressultindex = historicaliterationressultindex;
				
		return copy;
	}
	
	@Override
	public int compareTo(Object arg0) {
		
		if(this == arg0){
	        return 0;
	    }
		
		StoredGeneticSolution sgs;
		sgs = (StoredGeneticSolution) arg0;
		
		if( sgs.historicalscore > historicalscore ) {
			return 1;
		}
		else if( sgs.historicalscore < historicalscore ) {
			return -1;
		}
		else  {
			return 0;
		}		
	}
}



