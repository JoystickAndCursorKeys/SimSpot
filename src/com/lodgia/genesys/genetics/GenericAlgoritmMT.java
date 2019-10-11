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
import com.lodgia.genesys.main.MainProgram.SimulationViewerThread;


public class GenericAlgoritmMT implements InterfaceSearchAlgoritm {

	public static int RV_PROCESSINGDONE=0;
	public static int RV_STILLPROCESSING=1;
	public static int RV_INTERUPTED=2;
	public static int RV_ILLEGALVALUE=3;
	
	public static int STATE_INIT=0;
	public static int STATE_WORKONITERATION=1;	
	public static int STATE_FINALIZEITERATION=2;	
	public static int STATE_DONE=3;
	public static int STATE_LAUNCHITERATION=4;
	public static int STATE_FINALIZEITERATIONS=5;
	public static int STATE_WAITFORTHREADS=6;
	
	int threadcount=8;
	
	InterfaceEvaluationFunction evaluations[];
	
	int initialPopulationSize, populationSize, thisPopulationSize, currentprocessingmember;
	int genomesize;
	int maxstaticcycles;
	int injectrandomagents_numberof;
	int chancemutation;
	InterfaceEvaluationResult[] ResultsArray;
	
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
	
	GenericAlgoritmMTThread GAThreads[];
	Thread GASystemThreads[];
	
	public GenericAlgoritmMT(InterfaceGenericGenomeFactory pGBF, 
			InterfaceEvaluationFunction pEvaluations[], //Array of simulator classes
			InterfaceGenericMemberPicker pGMP,
			int _threadcount,
			int pInitialPopulationSize, 
			int pPopulationSize, 
			int pDebuglevel)
	{
		
		
		debugLevel=pDebuglevel;
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
		
		evaluations=pEvaluations;
		gmp=pGMP;
		GBFact=pGBF;
		
		populationSize=pPopulationSize;
		initialPopulationSize = pInitialPopulationSize;
		
		matingpoolkeepercentage=DEFAULTMATINGPOOLKEEPPERCENTAGE;
		injectrandomagents_numberof=INJECTRANDOMAGENTS_NUMBEROF;
		
		initialPopulationFile = null;
		threadcount = _threadcount;
		
		l.debug("Threadcount = " + threadcount );
		
		GAThreads = new GenericAlgoritmMTThread[ threadcount ];
		GASystemThreads = new Thread[ threadcount ];
		for(int i=0; i<threadcount; i++) {
			GAThreads[ i ] = new GenericAlgoritmMTThread();
			//GASystemThreads[ i ] =new Thread( GAThreads[ i ] );
		}
		
		init();
					
	}
	
	public void init(  )
	{
		
		state=STATE_INIT;
		
	}
	
	public int  processJobFraction() //iterates through a fraction of a cycle
	{
		
		if(state==STATE_INIT)
		{
			System.out.println("=== === === === === ===");
			System.out.println("Initializing");
			System.out.println("--- --- --- --- --- ---");
			
			l.debug("Initialize");

			population=new Population(gmp,debugLevel);
			stats=new Stats();
			
			System.out.println("  Initial PopulationSize:	" + initialPopulationSize);
			System.out.println("  Creating population...");
			for(int i=0;i<initialPopulationSize; i++)
			{
					InterfaceGenericGenome g;
					g=GBFact.createGenome();
					population.add(g);
			}
			thisPopulationSize = initialPopulationSize;
				
			
			iteration=1;
			lastwinnerscore=0.0;
			lastwinnerscorestaticcycles=0;
			SimLength = StartSimLength;

			stats_winnerscore=0.0;
			stats_avgscore=0.0;
			
			state=STATE_LAUNCHITERATION;
			currentprocessingmember=0;
			
			System.out.println("  Initializing scores...");
			population.startSetScores();

			return RV_STILLPROCESSING;
			
		}
		else if (state==STATE_LAUNCHITERATION)
		{
			
			System.out.println("=== === === === === ===");
			System.out.println("Next Iteration");
			System.out.println("--- --- --- --- --- ---");
			
			if( iteration == 2) {
				thisPopulationSize = populationSize;
			}
			
			System.out.println("  Simulation Length: 	" + SimLength);
			System.out.println("  PopulationSize:		" + thisPopulationSize);
			System.out.println("  ResultsArray.size:		" + ResultsArray.length);
			System.out.println("  ThreadCount:			" + threadcount);
			
			for( int i=0; i<this.SimPopIncreaseMax;i++ ) {
				ResultsArray[ i ] = null;
			}
		
			
			for( int i=0; i<threadcount;i++ ) {
				
				l.debug("launching thread " + i );
				evaluations[ i ].init();
				GAThreads[ i ].set(
						this,
						iteration,  		//iteration 
						i, 					//start (same as thread num)
						thisPopulationSize, //populationsize
						threadcount,		//step == number of threads
						evaluations[ i ], //class to run evaluation function 
						ResultsArray,
						SimLength,
						population,
						debugLevel
						);
				GASystemThreads[ i ] =new Thread( GAThreads[ i ] );
				GASystemThreads[ i ].start();	
			}				
				
			state=STATE_WAITFORTHREADS;
			return RV_STILLPROCESSING;
		}
		else if (state==STATE_WAITFORTHREADS)
		{
			
			
			int threadsDoneCount;
			threadsDoneCount = 0;
			for( int i=0; i<threadcount;i++ ) {
				if( GASystemThreads[ i ].isAlive() ) {
					try {
						GASystemThreads[ i ].join( 100 );
					} catch (InterruptedException e) {
						System.err.println("GASystemThreads[ i ].join( 100 ) failed");
						e.printStackTrace();
						return RV_ILLEGALVALUE;
					}
				}
				if( !GASystemThreads[ i ].isAlive() ) {
					l.debug("done with thread " + i );
					
					threadsDoneCount++;
				}
			}
			
			if( threadsDoneCount == threadcount ) {
				l.debug("done with threads" );
				System.out.println("..finished");
				state=STATE_FINALIZEITERATION;
			}
			else 
			{
				System.out.print(".");
				state=STATE_WAITFORTHREADS;
			}
			return RV_STILLPROCESSING;
			
		}
		else if (state==STATE_FINALIZEITERATION)
		{
			
			System.out.println("=== === === === === ===");
			System.out.println("Finalize iteration");
			System.out.println("--- --- --- --- --- ---");

			
			l.debug("finalize iteration" );
			
			System.out.println("Solution population count: " + population.members.size() );
			int i=-1;
			try {
				
				
			for( i=0; i<population.members.size();i++ ) {
				
				System.out.println("Score["+iteration+":"+i+"]: " +
						ResultsArray[i].getScore() );
			}
				
			for( i=0; i<population.members.size();i++ ) {
				population.registerScore(
						i,
						ResultsArray[i].getScore() );
			}
			}
			catch (Exception e) {
				System.err.println("resultarray size " + ResultsArray.length);
				System.err.println("i " + i );
				e.printStackTrace();
				throw e;
			}
			
	
			population.saveIterationSolutions(iteration);
			stats = population.updateStatsAsynchroniously(stats);
			
			
			keeplist=new ArrayList<InterfaceGenericGenome>();	
			
			InterfaceGenericGenome tmp;
			
			
			looser=population.looser();
			keeplist.add(population.looser().getCopy());
			
			tmp=looser.getCopy();
			tmp.mutate1genome( population.r);
			keeplist.add(tmp);
		
			winner=population.winner();

			if( winner != null ) {
				
				int lastwinnerIndex = population.findMemberGenome( winner );
				
				if( lastwinnerIndex >= 0 ) {
					System.out.println("  Maxscore: " + population.scores.get( lastwinnerIndex ) );
				}
				else
				{
					System.out.println("  Maxscore: unknown" );
				}
			}
			
			
			winner=population.winner().getCopy();
			
			tmp = population.winner().getCopy();
			keeplist.add(tmp);
			
			tmp = population.winner().getCopy();
			tmp.mutate1genome( population.r );
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
						" population was "+thisPopulationSize); 
				
				//state=STATE_DONE;
				///return RV_PROCESSINGDONE;
				
			}
			

			if(winnerscore<=lastwinnerscore)
			{
				lastwinnerscorestaticcycles=lastwinnerscorestaticcycles+1;
				if( lastwinnerscorestaticcycles >=  ( maxstaticcycles - 1 ) ) {
					lastwinnerscorestaticcycles = 0;
					SimLength = SimLength + this.SimLengthIncreaseStep;
					thisPopulationSize = thisPopulationSize + this.SimPopIncreaseStep;
					if( thisPopulationSize > this.SimPopIncreaseMax) {
						thisPopulationSize = this.SimPopIncreaseMax;
					}
				}
			}
			else
			{
				lastwinnerscorestaticcycles=0;
				lastwinnerscore=winnerscore;
				
				thisPopulationSize = populationSize;
			}
			
			population.createNewPopulation(keeplist, thisPopulationSize, chancemutation);			

			stats_avgscore=population.avgscore();
			stats_winnerscore=population.winnerScore();
			
			
			currentprocessingmember=0;
			population.startSetScores();
			
			iteration=iteration+1;
			currentprocessingmember = 0;
					
			System.out.println("=== === === === === === === ===");
			
			state=STATE_LAUNCHITERATION;
			return RV_STILLPROCESSING;
		}
		else if(state==STATE_DONE)
		{
			return RV_PROCESSINGDONE;
		}	
		
		/*
			 * 
			 * step = threadcount
			 * For t=1 to threadcount - 1
			 * 		launch_thread( processIterations, t, step );
			 * 
			 * launch_thread( processIterations, lastblock, step );
			 * 
			 * while 1
			 * {
			 * 		if all_threads_done( )
			 * 		{
			 * 			break;
			 * 		}
			 * }
			 * 
			 * state=STATE_FINALIZEITERATIONS;
			 * 
			 * return RV_STILLPROCESSING;
			 * 
		 */
			


		return RV_STILLPROCESSING;
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
	
	public void setSimulationPopulationIncreaseMax(int _max) {
		int max = _max;
		
		if( max < initialPopulationSize ) {
			max = initialPopulationSize;
		}
		this.SimPopIncreaseMax = max; 
		ResultsArray = new  InterfaceEvaluationResult[ max ];
		
		System.out.println("Initializing ResultsArray["+max+"]");
		
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
		return thisPopulationSize;
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
	
	synchronized public void memberDone()
	{
		currentprocessingmember++;
	}
	
	synchronized public int  currentProcessingIterationLength()
	{
		return SimLength;
	}

	public void setInitialPopulation(String _InitialPopulationFile) {
		
		 initialPopulationFile = _InitialPopulationFile;
		
	}


}
