package com.lodgia.genesys.genetics.interfaces;

import com.lodgia.genesys.main.MainProgram;


public interface InterfaceEvaluationFunction {

	
	public static final int MODE_EVALUATE=1;
	public static final int  MODE_EVALUATE_SAVERENDERDETAILS=2;
	public static final int  MODE_REPLAY=3;
	
	public void init();
	public void connectToController(MainProgram pApp);
	
	public InterfaceEvaluationResult  
	     evaluateSingle(InterfaceGenericGenome gg, 
	    		 int mode, 
	    		 boolean firstMemberFlag, 
	    		 int member, 
	    		 int members, 
	    		 int itteration, 
	    		 int simlength, 
	    		 int batch, 
	    		 int lastInBatch
	    		 );
	//public double  evaluateMulti(ArrayList<InterfaceGenericGenome> listgg);	
	
	public void interrupt();
	public boolean  wasInterupted();
	
	public int  getCurrentIterationStep();
	public double  getAgentCurrentScore(int agentindex);
	public double  getNumericProperty(int property);
	public int getAgentCurrentHunger(int i);
	public boolean getAgentCurrentIsDead();
	public boolean getAgentCurrentIsHungry();
	public double getAgentCurrentHealth(int i);
	public int getAgentCurrentGround();
	public int getCustomLabelCount();
	public String getCustomLabel(int i);
	public String getCustomLabelValue(int i);



	//public int setEvaluationLength(int l);
}
