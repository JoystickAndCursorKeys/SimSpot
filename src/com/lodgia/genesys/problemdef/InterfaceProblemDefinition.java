package com.lodgia.genesys.problemdef;

import com.lodgia.genesys.genetics.GenericAlgoritm;
import com.lodgia.genesys.genetics.Stats;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationFunction;
import com.lodgia.genesys.genetics.interfaces.InterfaceSearchAlgoritm;
import com.lodgia.genesys.gfx.interfaces.InterfaceWorldGenericRenderer;
import com.lodgia.genesys.main.MainProgram;

public interface InterfaceProblemDefinition {

	public void connectToController(MainProgram pApp);
	
	public void init(int pDebugLevel) throws Exception;
	
	public  int processSolveCycle();
		
	public InterfaceWorldGenericRenderer getSolutionSpaceRenderer();
	
	public InterfaceEvaluationFunction[]  getEvaluationClass();
	
	public InterfaceEvaluationFunction  getEvaluationClassCopyForPlaying();
	
	
	public InterfaceSearchAlgoritm  getSearchAlgoritmClass();
	
	public Stats getStats();

	public String getDataRootDir();
	
}
