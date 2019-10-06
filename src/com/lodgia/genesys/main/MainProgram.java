package com.lodgia.genesys.main;


import java.io.FileReader;

import com.lodgia.genesys.genetics.GenericAlgoritm;
import com.lodgia.genesys.genetics.Population;
import com.lodgia.genesys.genetics.StoredGeneticSolution;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationFunction;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationResult;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.genetics.interfaces.InterfaceSearchAlgoritm;
import com.lodgia.genesys.lib.Logger;
import com.lodgia.genesys.neuralnet.AF_Sigmoid;
import com.lodgia.genesys.problemdef.InterfaceProblemDefinition;
import com.lodgia.genesys.utils.TypedProperties;


public class MainProgram {
		

	String APPTITLE="Simspot V1.1";
	int SCREENDELAY = 1000/75; 
	String ProblemdefinitionName = null; //Take from properties files;
	
	public  boolean  stop, realtime;
	final static int debugLevel=Logger.LEVEL_ERRORS;
		
	public InterfaceProblemDefinition genericProblemDef;
	
	SimulationViewerThread simViewThread;
	Thread simViewThreadClassThread;
		
		
    public void run() throws Exception 
    {

    		@SuppressWarnings("unused")
			int status;
    		
    		init();
   		 
    		
    		System.out.println("stop="+stop);
    		while(!stop)
    		{
    			
    			status=genericProblemDef.processSolveCycle();
    			//STUB, to paint the complete iteration buffer on the screen
    		    			
    		}
    
    		fini();
    		System.out.println("Program ended");
    	
    }
    
    
    public void init() throws Exception {
    	
    	genericProblemDef =
    		  (InterfaceProblemDefinition) Class.forName(ProblemdefinitionName).newInstance();
    	
    	//GPD=(InterfaceProblemDefinition) new Robot2();    	
    	
    	genericProblemDef.init(debugLevel);
    	genericProblemDef.connectToController(this);
    	
    	AWTConsole.launch(this,debugLevel);
        
    	stop=false;
        realtime=false;
        
        simViewThread=new SimulationViewerThread();
        simViewThreadClassThread=new Thread(simViewThread);
        simViewThreadClassThread.start();
 
    }    
    
    
    public static void main(String[] args) {
    	
    	try  {


    		//new AF_Sigmoid().test();
    		//throw new Exception();
    		new MainProgram();
    		
    
    		}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	  	
    }

    
	public void threaded_callback_handleKeyEvents(int keycode,
			String text) {
		
    	if(keycode==AWTInputEvents.IE_QUIT)
		{
        	System.out.println("keycode==inputevents.IE_QUIT");
			stop=true;
		}
    	else if(keycode==AWTInputEvents.IE_PLAYLASTWINNER)
		{
        	System.out.println("keycode==inputevents.IE_PLAYLASTWINNER");
			        	
        	genericProblemDef.getEvaluationClassCopyForPlaying().interrupt();
        	simViewThread.show=true;
        	simViewThread.selector = text;
		}   
    	else if(keycode==AWTInputEvents.IE_REALTIME)
		{
    		realtime=!realtime;
		}       	
    	return;
 		
	}	
    
    public synchronized void threaded_callback_handleKeyEvents(int keycode)
    {
		
    	threaded_callback_handleKeyEvents( keycode, null );
    	
    }
    
    
	public MainProgram() throws Exception {
		
		TypedProperties cfg = new TypedProperties();
		
		cfg.load(new FileReader("./configuration.properties"));
		ProblemdefinitionName = cfg.gets("PROBLEMDEFINITION");
			
		run();
    	
    }

    public void fini() throws Exception {
    	
    
    	//blue, why create a new GDP, when we will quit?
    	
    	genericProblemDef =
  		  (InterfaceProblemDefinition) Class.forName(ProblemdefinitionName).newInstance();
  	
    	//GPD=(InterfaceProblemDefinition) new Robot2();    	
  	
    	
    	genericProblemDef.init(debugLevel);
    	genericProblemDef.connectToController(this);
    	
    	AWTConsole.close();
        
    	
    	//stop=false;
        realtime=false;
     
 
    }  
    

	public synchronized boolean stopped() {
		
        return stop;
        
    }
	
	public  String getTitle() {
		
        return APPTITLE;
        
    }
	
	public  void throttleSpeed() {
		
		if(realtime)
		{
			Sleep(SCREENDELAY/2);
		}
		else
		{
			Sleep(SCREENDELAY);
		}
		
    }
	
    static void  Sleep(int millis)
	{
    	try {
			Thread.sleep(millis);
			;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
    
    public class SimulationViewerThread implements Runnable {

    	
    	public String selector;
		public boolean show;
    	StoredGeneticSolution sgs;
    	double score;
    	int mode;
    	InterfaceGenericGenome gen;
    	
		public void run() {
			
			
			InterfaceSearchAlgoritm searchAlgoritm = 
					genericProblemDef.getSearchAlgoritmClass();
			
			Population pop;
			StoredGeneticSolution sgs;
						
			InterfaceEvaluationFunction eval=
					genericProblemDef.getEvaluationClassCopyForPlaying();
			
			eval.init();
			
			
			show=false;
	
			pop=null;
			sgs=null;
			
			while(true)
			{
				while(!show && !stop)
				{
					Sleep(50);
					
				}
				
				if(stop)
				{
					break;
				}
				
				System.out.println("Starting playing single for viewing");
				
				
				try
				{
					
					pop=searchAlgoritm .getPopulation();

					sgs = getStoredSolution( pop, selector );
					if( sgs != null ) {
					
					mode=InterfaceEvaluationFunction.MODE_REPLAY;
					
					gen=sgs.genome;			
					
					InterfaceEvaluationResult evr;
					evr=eval.evaluateSingle(gen,mode,true,-1,-1,-1,100000, -1, -1
						);
					
					
				     
					score=evr.getScore();
					
					
					System.out.println("replay eval stopped");		
					
					}
					else {
						System.out.println("invalid selection '" + selector +"'" );		
					}

				}
				catch (Exception iexc) 
				{
					iexc.printStackTrace();
					
					System.err.println("err eval="+eval);
					System.err.println("err pop="+pop);
					System.err.println("err sgs="+sgs);
					System.err.println("err sgs.genome="+gen);
					//System.exit(0);
					
				}
	
				show=false;
			}
		}

		private StoredGeneticSolution getStoredSolution(Population pop,
				String solutionSelectorString) {
			if( solutionSelectorString.equals( "winner") ) {
				return pop.getLastSavedWinner();
			}
			else {
				return pop.getLastSavedMember( Integer.parseInt( solutionSelectorString ));
			}
		}
    }



}

