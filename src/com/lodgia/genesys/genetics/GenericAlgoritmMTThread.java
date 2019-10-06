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


public class GenericAlgoritmMTThread implements Runnable {

	GenericAlgoritmMT parentThread;
	int iteration, start, populationSize, step;
	InterfaceEvaluationFunction evaluation;
	InterfaceEvaluationResult[] resultsArray;
	int simLength;
	Population population;
	Logger l;
	int debugLevel;	
	
	public void set( GenericAlgoritmMT _parentThread, int _iteration, 
			int _start, 
			int _populationSize, 
			int _step,
			InterfaceEvaluationFunction _evaluation, InterfaceEvaluationResult[] _resultsArray, 
			int _simLength, Population _population,
			int pDebugLevel) {
		parentThread = _parentThread;
	    start = _start;
	    step = _step;
	    populationSize = _populationSize;
	    iteration = _iteration;
	    evaluation = _evaluation;
	    resultsArray = _resultsArray;
	    simLength = _simLength;
	    population = _population;
	    
		debugLevel=pDebugLevel;
		l=new Logger(this.getClass().getSimpleName(),debugLevel);
		
	}
	
	@Override
	public void run() {
		l.debug( "Starting thread " + start );
		
		int lastInBatch = 0;
		for( int i = start; i < populationSize; i += step ) {
			lastInBatch = i;
		}
		
		for( int i = start; i < populationSize; i += step ) {
			l.debug( "Starting eval " + start + ":" + i );
			evaluation.init();
			resultsArray[ i ] = evaluation.evaluateSingle(
					/*
					 *  int member, 
	    		 int members, 
	    		 int itteration, 
	    		 int simlength, 
	    		 int batch, 
					 */
					population.getMemberGenome( i ),
					InterfaceEvaluationFunction.MODE_EVALUATE_SAVERENDERDETAILS,
					i==start,
					i,
					populationSize,
					iteration,
					simLength,
					start,
					lastInBatch
					);	
			parentThread.memberDone();
		}
	}
}
/*
SimulationViewerThread simViewThread;
Thread simViewThreadClassThread;

simViewThread=new SimulationViewerThread();
simViewThreadClassThread=new Thread(simViewThread);
simViewThreadClassThread.start();
*/

