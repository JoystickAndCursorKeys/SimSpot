package com.lodgia.genesys.neuralnet;

public class Synapse {

	public Neuron inputCell;
	public double weight;
	//int id;
	
	Synapse(Neuron pInputCell, double pStartweight)
	{
		weight=pStartweight;
		inputCell=pInputCell;
		
	}


}
