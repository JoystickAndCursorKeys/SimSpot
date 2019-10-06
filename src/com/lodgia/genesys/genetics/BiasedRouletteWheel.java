package com.lodgia.genesys.genetics;

import java.util.ArrayList;

import com.lodgia.genesys.genetics.interfaces.InterfaceGenericMemberPicker;
import com.lodgia.genesys.lib.Logger;

public class BiasedRouletteWheel implements InterfaceGenericMemberPicker {

	double scoresum;
	boolean debugflag;
	ArrayList<Integer> members ;
	ArrayList<Double> scores  ;
	java.util.Random r;
	int debugLevel;
	Logger l;
	
	public BiasedRouletteWheel(int pDebugLevel)
	{
		debugLevel=pDebugLevel;
		
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
		r=new java.util.Random();
		reInit();
	}
	
	public void reInit()
	{
		scoresum=0.0;
		debugflag=false;
		members=new ArrayList<Integer>();
		scores=new ArrayList<Double>();
		
	}
		
	public int spinWheel()
	{

		double factor,minperc_currmbr,perc_currmbr,maxperc_currmbr;
		int dice;
		
		factor=100 / this.scoresum;
		
		minperc_currmbr=0.0;

		dice=r.nextInt(100);

				
		for (int i=0 ; i<scores.size(); i++)
		{

			perc_currmbr=this.scores.get(i) * factor;
			
			maxperc_currmbr=minperc_currmbr+perc_currmbr;

			if(dice >= minperc_currmbr && dice <= maxperc_currmbr)
			{		
				return (i);
			}
				
			minperc_currmbr=minperc_currmbr+perc_currmbr;
		}
			
		return 0;
	}		
			
	public void registerScore( int memberid,double score)
	{

		members.add(new Integer(memberid));
		scores.add(new Double(score));		
		scoresum+=score;
		
	}
	
	
	public int chooseMember()
	{
		return this.spinWheel();
	}
	
}
