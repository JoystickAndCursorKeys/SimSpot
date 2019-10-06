package com.lodgia.genesys.genetics.interfaces;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.lodgia.genesys.genetics.Population;
import com.lodgia.genesys.genetics.Stats;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationFunction;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationResult;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenomeFactory;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericMemberPicker;
import com.lodgia.genesys.lib.Logger;


public interface InterfaceSearchAlgoritm {


	
	public void init();
	public int  processJobFraction();
	public void setMaxNoGrowCycles(int _maxstaticcycles);
	public void setNumberOfRandomAgentsToInsert(int y);
	public void setMatingpoolKeepingPercentage(double y);
	public void setMutationChance(int c);
	public void setSimulationStartLength(int startsimlength);
	public void setSimulationIncreaseStep(int step);
	public void setSimulationPopulationIncreaseStep(int step);	
	public void setSimulationPopulationIncreaseMax(int max);
	
	/*synchronized*/ public Stats getStats();
	/*synchronized*/ public double avgscore();
	/*synchronized*/ public double winnerScore();
	/*synchronized*/ public int  populationSize();
	/*synchronized*/ public Population  getPopulation();
	/*synchronized*/ public int  currentProcessingMember();
	/*synchronized*/ public int  currentProcessingIteration();
	/*synchronized*/ public int  currentProcessingIterationLength();
	/*synchronized*/ public void setInitialPopulation(String _InitialPopulationFile);	
}
