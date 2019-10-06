package com.lodgia.genesys.neuralnet;

import java.util.ArrayList;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class Layer {

	public String name;
	public ArrayList<Neuron> neurons ;
	
	Layer(String pName)
	{
		name=pName;
		neurons = new ArrayList<Neuron>();
	}
	
	void add(Neuron n)
	{
		neurons.add(n);
	}

	public Neuron addNeuron(InterfaceActivationFunction af) {
		Neuron n;
		n = new Neuron( af );
		add( n );
		return n;
	}
	
}
