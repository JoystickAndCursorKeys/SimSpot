package com.lodgia.genesys.genetics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationFunction;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationResult;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenomeFactory;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericMemberPicker;
import com.lodgia.genesys.genetics.interfaces.InterfaceSearchAlgoritm;
import com.lodgia.genesys.lib.Logger;


public class GenericAlgoritm implements InterfaceSearchAlgoritm {

	public static int RV_PROCESSINGDONE=0;
	public static int RV_STILLPROCESSING=1;
	public static int RV_INTERUPTED=2;
	public static int RV_ILLEGALVALUE=3;
	
	public static int STATE_INIT=0;
	public static int STATE_WORKONITERATION=1;	
	public static int STATE_FINALIZEITERATION=2;	
	public static int STATE_DONE=3;
	
	//final int  DEFAULTMAXSTATICCYCLES=10;
	
	InterfaceEvaluationFunction evaluation;
	
	int initialPopulationSize, populationsize,currentprocessingmember;
	int genomesize;
	int maxstaticcycles;
	int injectrandomagents_numberof;
	int chancemutation;
	
	Logger l;
	int debugLevel;
	
	
	InterfaceGenericMemberPicker gmp;
	Population population;	//intern
	Stats stats;
	
	
	InterfaceGenericGenomeFactory GBFact;
	
	int iteration,lastwinnerscorestaticcycles ;
	double lastwinnerscore;
	
	double stats_winnerscore, stats_avgscore;

	InterfaceGenericGenome winner,looser,randomgenome;
	ArrayList<InterfaceGenericGenome> keeplist;
	double winnerscore;	
	
	int state;

	double matingpoolkeepercentage;
	final  double DEFAULTMATINGPOOLKEEPPERCENTAGE=80.0; //Don't change here
	final  int INJECTRANDOMAGENTS_NUMBEROF=5; //Don't change here 
	private int StartSimLength, SimLengthIncreaseStep, SimLength;
	private int SimPopIncreaseStep;
	private int SimPopIncreaseMax;
	
	String initialPopulationFile;
	
	public GenericAlgoritm(InterfaceGenericGenomeFactory pGBF, 
			InterfaceEvaluationFunction pEvaluation, 
			InterfaceGenericMemberPicker pGMP, 
			int pPopulation, 
			int pDebuglevel)
	{
		debugLevel=pDebuglevel;
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
		
		evaluation=pEvaluation;
		gmp=pGMP;
		GBFact=pGBF;
		
		populationsize=pPopulation;
		initialPopulationSize = pPopulation;
		
		matingpoolkeepercentage=DEFAULTMATINGPOOLKEEPPERCENTAGE;
		injectrandomagents_numberof=INJECTRANDOMAGENTS_NUMBEROF;
		
		initialPopulationFile = null;
		
		init();
					
	}
	
	public void init()
	{
		
		
		state=STATE_INIT;
		
	}
	
	
	public int  processJobFraction() //iterates through a fraction of a cycle
	{

		
		if(state==STATE_INIT) /* Init all */
		{
			System.out.println("Initialize");
			evaluation.init();
			population=new Population(gmp,debugLevel);
			stats=new Stats();
			
			if( initialPopulationFile == null ) {
				//create initial populationcurrentProcessingMember
				for(int i=0;i<populationsize; i++)
				{
					InterfaceGenericGenome g;
					g=GBFact.createGenome();
					population.add(g);
				}
			}
			else
			{
				File f = new File( initialPopulationFile );
				
				try {
					
					BufferedReader buffer=new BufferedReader(new InputStreamReader(new FileInputStream( f )));
					
					while( true ) {

						String line=buffer.readLine();
						
						if( line == null || line.equals("" )) {
							break;
						}
						
						InterfaceGenericGenome g;
						g=GBFact.createGenome( line );
						population.add(g);
						
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
			iteration=1;
			lastwinnerscore=0.0;
			lastwinnerscorestaticcycles=0;
			SimLength = StartSimLength;

			stats_winnerscore=0.0;
			stats_avgscore=0.0;
			//maxstaticcycles=DEFAULTMAXSTATICCYCLES;
			
			//evaluation function
			
			
			state=STATE_WORKONITERATION;
			currentprocessingmember=0;
			population.startSetScores();

			return RV_STILLPROCESSING;
			
		}
		else if(state==STATE_WORKONITERATION)
		{
		
			
			if(!(currentprocessingmember<populationsize))
			{
				state=STATE_FINALIZEITERATION;
				return RV_STILLPROCESSING;
			}
			
			InterfaceGenericGenome g;
			InterfaceEvaluationResult result;
			
			g=population.getMemberGenome(currentprocessingmember);
			
			//evaluation.connectToGUI(pApp)
			result = evaluation.evaluateSingle(
					g,
					InterfaceEvaluationFunction.MODE_EVALUATE_SAVERENDERDETAILS,
					currentprocessingmember == 0,
					currentprocessingmember,
					populationsize,
					iteration,
					SimLength,
					0, 1
					);
			
			//BLUE change mode here
			
			//System.out.println("Done with evaluation");
			
			population.registerScore(currentprocessingmember,result.getScore() );
	
			if(evaluation.wasInterupted())
			{
				return RV_INTERUPTED;
			}
			
			currentprocessingmember++;
			
			return RV_STILLPROCESSING;
		}
		else if(state==STATE_FINALIZEITERATION)
		{
			//logger.logprintdebug(True, "main", "I="+logger.pad(" ",4,str(iteration))+" WINNER="+logger.pad(" ",4,str(p.winnerscore()))+" AVG="+logger.pad( " ",5,str(p.avgscore()))+"  winnerdetail="+str(p.winner())+ " "+evaluationcomment())


			population.saveIterationSolutions(iteration);
			stats = population.updateStatsAsynchroniously(stats);
			
			
			keeplist=new ArrayList<InterfaceGenericGenome>();	
			
			looser=population.looser();
			keeplist.add(looser);
			
			looser=looser.getCopy();
			looser.mutate1genome( population.r);
			keeplist.add(looser);
		
			winner=population.winner();
			keeplist.add(winner);
			
			winner=winner.getCopy();
			winner.mutate1genome( population.r );
			keeplist.add(winner);

			
			for(int t=0;t<injectrandomagents_numberof; t++)
			{
			
				randomgenome= GBFact.createGenome(); 
				randomgenome.randomize( population.r );
				keeplist.add( randomgenome );
			}	
			
			
			//mating pool
			
			population.createMatingPool(matingpoolkeepercentage);


			winnerscore=population.winnerScore();
			
			
			if(lastwinnerscore <= winnerscore && 
					lastwinnerscorestaticcycles > maxstaticcycles)
			{
				l.debug( "Criteria fullfilled, itteration="+iteration+
						" population was "+populationsize); 
				
				//state=STATE_DONE;
				///return RV_PROCESSINGDONE;
				
			}
			

			if(winnerscore<=lastwinnerscore)
			{
				lastwinnerscorestaticcycles=lastwinnerscorestaticcycles+1;
				if( lastwinnerscorestaticcycles >=  ( maxstaticcycles - 1 ) ) {
					lastwinnerscorestaticcycles = 0;
					SimLength = SimLength + this.SimLengthIncreaseStep;
					populationsize = populationsize + this.SimPopIncreaseStep;
					if( populationsize > this.SimPopIncreaseMax) {
						populationsize = this.SimPopIncreaseMax;
					}
				}
			}
			else
			{
				lastwinnerscorestaticcycles=0;
				lastwinnerscore=winnerscore;
				
				populationsize = initialPopulationSize;
			}
			
			population.createNewPopulation(keeplist, populationsize, chancemutation);			

			stats_avgscore=population.avgscore();
			stats_winnerscore=population.winnerScore();
			
			
			iteration=iteration+1;
			
			state=STATE_WORKONITERATION;
			currentprocessingmember=0;
			population.startSetScores();
			
			return RV_STILLPROCESSING;
		}
		else if(state==STATE_DONE)
		{
			return RV_PROCESSINGDONE;
		}
		
		return RV_ILLEGALVALUE;
	}
	
	
	public void setMaxNoGrowCycles(int _maxstaticcycles)
	{
		
		
		maxstaticcycles=_maxstaticcycles;
		
	}
	
	
	public void setNumberOfRandomAgentsToInsert(int y)
	{
		
		
		injectrandomagents_numberof=y;
		
	}
	
	public void setMatingpoolKeepingPercentage(double y)
	{
		
		
		matingpoolkeepercentage=y;
		
	}
	
	
	public void setMutationChance(int c)
	{
		
		
		chancemutation=c;
		
	}
	
	
	public void setSimulationStartLength(int startsimlength)
	{
				
		this.StartSimLength = startsimlength; 
		
	}
	
	public void setSimulationIncreaseStep(int step)
	{
				
		this.SimLengthIncreaseStep = step; 
		
	}
	
	public void setSimulationPopulationIncreaseStep(int step) {
		
		this.SimPopIncreaseStep = step; 
	}	
	
	public void setSimulationPopulationIncreaseMax(int max) {
		this.SimPopIncreaseMax = max; 
		
	}
	
	synchronized public Stats getStats()
	{
		return stats;
	}
	
	
	synchronized public double avgscore(){
		
		return stats_avgscore;
	}

	synchronized public double winnerScore()
	{
		return stats_winnerscore;
	}
	
	synchronized public int  populationSize()
	{
		return populationsize;
	}
	
	synchronized public Population  getPopulation()
	{
		return population;
	}
	
	synchronized public int  currentProcessingMember()
	{
		return currentprocessingmember;
	}
	
	synchronized public int  currentProcessingIteration()
	{
		return iteration;
	}
	
	synchronized public int  currentProcessingIterationLength()
	{
		return SimLength;
	}

	public void setInitialPopulation(String _InitialPopulationFile) {
		
		 initialPopulationFile = _InitialPopulationFile;
		
	}




	
	
}
