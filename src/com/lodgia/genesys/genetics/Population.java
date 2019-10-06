package com.lodgia.genesys.genetics;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationResult;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericMemberPicker;
import com.lodgia.genesys.lib.Logger;
import com.lodgia.genesys.lib.MersenneTwister;

public class Population {

	public double scoresum;
	public double topscore;
	public double bottomscore;
	InterfaceGenericGenome topscoreperformer;
	InterfaceGenericGenome bottomscoreperformer;
	
	public int debugLevel;
	Logger l;
	
	Random r;
		
	ArrayList<StoredGeneticSolution> historicalsolutions  ;
	
	ArrayList<InterfaceGenericGenome> members  ;
	ArrayList<InterfaceGenericGenome> matingpool  ;	
	ArrayList<Double> scores  ;
	ArrayList<InterfaceEvaluationResult> resultsdetails  ;

	InterfaceGenericMemberPicker randomchooser;

	
	Population(InterfaceGenericMemberPicker thischooser, int pDebuglevel)
	{	

		debugLevel=pDebuglevel;
				
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
		
		l.debug("Constructor chooser= "+thischooser.getClass().getName());
		
		scoresum=0.0;
		topscore=0.0;
		bottomscore=0.0;
		
		topscoreperformer=null;
		bottomscoreperformer=null;
		
		randomchooser=thischooser;
		
		members=new ArrayList<InterfaceGenericGenome>();
		matingpool=null;
		scores=new ArrayList<Double>();
		resultsdetails = new ArrayList<InterfaceEvaluationResult>();
		
		historicalsolutions=new ArrayList<StoredGeneticSolution>();
		
		
		r=new MersenneTwister();
		//r = new SecureRandom();
		r.setSeed( System.currentTimeMillis());
		
		System.out.println("New Random Generator" + r.getClass().getName() );
		
	}
	
	
	void add(InterfaceGenericGenome member)
	{
		l.debug("Add member:"+member.getAsString());
		this.members.add(member);
		this.scores.add(0.0);
		this.resultsdetails.add(null);
		
	}
	
	InterfaceGenericGenome getMemberGenome(int t)
	{
		return this.members.get(t);
	}
	
	int findMemberGenome( InterfaceGenericGenome g)
	{
		for( int i=0; i < this.members.size(); i++) {
			if( this.members.get( i ).equals( g ) ) {
				return i;
			}
		}
		return -1;
	}
	

	
	/*
	def getmembers(self):
		return len(self.members);

    	def getmemberobject(self, index):
		return self.members[index];
*/
	
	
	void registerResult(int memberindex,InterfaceEvaluationResult result)
	{
		registerScore(memberindex, result.getScore());
		this.resultsdetails.set(memberindex, result);
	}
	
	void registerScore(int memberindex,double score)
	{
		
		l.debug("registerScorer for["+memberindex+"]:"+score);
		
		this.scores.set(memberindex, score);
		
		 
		this.scoresum=this.scoresum+score;
		this.randomchooser.registerScore(memberindex,score);
		
		
		if(score>this.topscore || this.topscoreperformer==null){
			this.topscore=score;
			this.topscoreperformer=this.members.get(memberindex);
		}
			 
		if(score<this.bottomscore || this.bottomscoreperformer==null){
			this.bottomscore=score;
			this.bottomscoreperformer=this.members.get(memberindex);
		}				 
	}

	
	void startSetScores()
	{
		l.debug("startSetScores");
		
		this.scoresum=0.0;
		this.topscore=0.0;
		this.topscoreperformer=null;
		this.bottomscore=Double.MAX_VALUE;
		this.bottomscoreperformer=null;
		 
		this.randomchooser.reInit();
	}
	
	
	void createMatingPool(double matingpoolsizeperc)
	{
		
		l.debug("createMatingPool "+matingpoolsizeperc+"%");
		
		this.matingpool=new ArrayList<InterfaceGenericGenome>();	

		double matingpoolsize=((matingpoolsizeperc/100)*this.members.size());
		 

		for(int  i=0; i<(int)matingpoolsize; i++)
		{

			int memberindex;
			
			memberindex=this.randomchooser.chooseMember();
			
			this.matingpool.add(	
					this.members.get(memberindex)	
			);
						
		}
	}

	
	void createNewPopulation(ArrayList<InterfaceGenericGenome> keeplist, int newsize, int chancemutations)
	{
		
		l.debug("createNewPopulation keeplist="+keeplist.size());
		
		int oldsize;
		int matingpoolsize;
		int j;
		int duplicates;
		//java.util.Random r = Random();
		
		InterfaceGenericGenome mate1, mate2;
		InterfaceGenericGenome child;
		InterfaceGenericGenome currentMember_i;
		InterfaceGenericGenome currentMember_j;
		
		oldsize=this.members.size();
		if( newsize > oldsize ) {
			while( scores.size() < newsize ) {
				scores.add(0.0);
				resultsdetails.add(null);
			}
		}
		
		
		
		matingpoolsize=this.matingpool.size();
		this.members=new ArrayList<InterfaceGenericGenome>();
		
		for(int i=0; i<keeplist.size();i++)
		{
			InterfaceGenericGenome g;
			g = keeplist.get(i).getCopy();
			
			
			this.members.add(
					g //keeplist.get(i) 
					);
			
			if( ! keeplist.get(i).equals( g ))
			{
				System.out.println("keeplist oops 217");
			}
			
		}

		while (this.members.size()< newsize)
		{
			int choice1=r.nextInt(matingpoolsize);
			int choice2=r.nextInt(matingpoolsize);
			int p;
			 
			mate1=this.matingpool.get(choice1);
			mate2=this.matingpool.get(choice2);

			child=mate1.produceChildSimple(r, mate2);
					//(r,mate2);
			
			p = r.nextInt(100);
			if( p <= chancemutations ) {
				child.mutate1genome( r );
			}
			
			this.members.add(child);
			
		}
		
		//System.out.println("New population contains " + this.members.size() + " members" );
		//Add random bits to avoid duplicates
		l.debug("Avoid duplicates");
		while( true ) {
			duplicates = 0;
			for(int i=0; i<this.members.size();i++)
			{
				currentMember_i=this.members.get( i );
				j=i+1;
				while ( j<this.members.size() ) {
					currentMember_j=this.members.get( j );
					if( currentMember_i.equals( currentMember_j )) {
						duplicates++;
						currentMember_j.mutate1genome( r );
						l.debug("Mutated duplicate " + i + " & m" + j);
						if( currentMember_i.equals( currentMember_j ))
						{
							l.logerror("Oops 260");
						}
					}
					j++;
				}
			}
			if( duplicates == 0 ) {
				 
				break;
			}
		}
		//r.setSeed( System.currentTimeMillis());
		//System.out.println("ReInit random");
	}	


	@SuppressWarnings("unchecked")
	synchronized public void saveIterationSolutions(int iteration)
	{
		StoredGeneticSolution sgs;
		ArrayList<StoredGeneticSolution>  tmphistoricalsolutions;
		tmphistoricalsolutions=new ArrayList<StoredGeneticSolution>();
		
		for(int i=0; i<members.size();i++)
		{
			sgs=new StoredGeneticSolution();
			sgs.genome=members.get(i);
			sgs.historicalscore=scores.get(i);
			sgs.historicaliteration=iteration;
			//sgs.historicaliterationressultindex=
			//	(winnerScore()==sgs.historicalscore);
			sgs.historicaliterationaverage=avgscore();
			tmphistoricalsolutions.add(sgs);		
		}
		
		Collections.sort( tmphistoricalsolutions );
		
		for(int i=0; i<tmphistoricalsolutions.size();i++)
		{
			sgs=tmphistoricalsolutions.get(i).getCopy();
			sgs.historicaliterationressultindex=i;
			this.historicalsolutions.add(sgs);	
		}
		
		for(int i=0; i<tmphistoricalsolutions.size();i++)
		{
			tmphistoricalsolutions.remove(i);
		}
	

	}
	
	
	
	synchronized public StoredGeneticSolution getLastSavedWinner()
	{
		
		
		StoredGeneticSolution sgs;
		StoredGeneticSolution lastwinner=null;
		
		for(int i=0; i<historicalsolutions.size();i++)
		{
			sgs=historicalsolutions.get(i);
			
			if(sgs.historicaliterationressultindex == 0)
			{
				lastwinner=sgs;
			}
		}
		
		return lastwinner;
	}
	
	

	synchronized public StoredGeneticSolution getLastSavedMember(int index) {
		
		StoredGeneticSolution sgs;
		
		if( historicalsolutions.size() == 0 ) {
			return null;
		}
		
		sgs = historicalsolutions.get( historicalsolutions.size() - 1 );
		if( sgs == null ) {
			return null;
		}
		
		int iteration = sgs.historicaliteration;
		
		for( int i = historicalsolutions.size()  - 1 ; i>=0 ; i--) {
			sgs = historicalsolutions.get( i );
			
			if( sgs.historicaliteration != iteration ) {
				return null;
			}
			
			if( sgs.historicaliterationressultindex == index ) {
				return sgs;
			}
		}
		
		return null;
	}
		
	
	
	
	synchronized public Stats updateStatsAsynchroniously(Stats stats)
	{
		StoredGeneticSolution sgs;
		
		int size,sup;
		size = historicalsolutions.size();
		sup = stats.lastUpdatePointer();
		
		for(int i=sup; i<size;i++)
		{
			sgs=historicalsolutions.get(i);
			
			if(sgs.historicaliterationressultindex == 0)
			{
				double[] scores;
				scores=new double[2];

				scores[0]=sgs.historicalscore;
				scores[1]=sgs.historicaliterationaverage;
								
				stats.AddScores(
						scores
						);
				
				break; /* there could be multiple winners */
			}
		}
		
		stats.updatePointer(size);
		
		return stats;
	}
	
	
	double avgscore(){
		
		return this.scoresum / this.scores.size();
	}

	double winnerScore()
	{
		return this.topscore;
	}

	InterfaceGenericGenome winner()
	{
		return this.topscoreperformer;
	}

	double looserScore()
	{
		return this.bottomscore;
	}

	InterfaceGenericGenome looser()
	{
		return this.bottomscoreperformer;
	}


}
