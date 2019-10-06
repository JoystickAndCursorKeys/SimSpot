package com.lodgia.genesys.neuralnet;

import java.util.ArrayList;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class Neuron {

	public ArrayList<Synapse> synapseInputs ;
	boolean clamped;
	double clampedValue;
	public double value;
	boolean done;
	int count;
	InterfaceActivationFunction activationFunction;
	
	//myFunction=None
	//myLearnFunction=None

	Neuron( InterfaceActivationFunction activationFunction_ )
	{
		activationFunction = activationFunction_;
		synapseInputs = new ArrayList<Synapse>();
		clamped=false;
		clampedValue=0.0;
		value=0.0;
		done=false;
		count=0;
		
	}
	
	void useAsInput()
	{
		clamped=true;
	}
	
	public void clampTo(double pValue)
	{
		clampedValue=pValue;
	}
	
	
	Synapse addSynapseLinkFrom(Neuron pOther,double pStartWeight )
	{
		Synapse synapse;
		synapse=new Synapse(pOther,pStartWeight);
		
		this.synapseInputs.ensureCapacity(this.synapseInputs.size()+1);
		if(!this.synapseInputs.add(synapse))
		{
			System.err.println("Fatal internal error occured, inputs.add failed");
			
		}
		return synapse;
	}
	

	double CalculateBackward()
	{

		double tempsum=0.0;
		
		if(clamped)
		{
			return clampedValue;
		}
		
		int size=synapseInputs.size(); 
		
		for(int t=0; t<size; t++)
		{
		
			Synapse s;
			s=synapseInputs.get(t);
			
			double cbw;
			cbw=s.inputCell.CalculateBackward();
			tempsum=tempsum + cbw * s.weight;
		}	

	
		this.value = activationFunction.Activation( tempsum );
		return value;
	}
	
	
	void  initRecursive()
	{

		if(false==this.done)
		{
			return 	;				
		}
		
		this.done=false;

		int size=synapseInputs.size(); 
		for(int t=0; t<size; t++)
		{
		
			Synapse s;
			s=synapseInputs.get(t);
		
			s.inputCell.initRecursive();
			
		}	
		
	}
	
	
	double CalculateRecursive()
	{
		

		if(this.done)
		{
			return this.value;
		
		}
		
		if(clamped)
		{
			return clampedValue;
		}

		this.done=true;


		double tempsum=0.0;
		
		
		int size=synapseInputs.size(); 
		for(int t=0; t<size; t++)
		{
		
			Synapse s;
			s=synapseInputs.get(t);
		
			double cbw=s.inputCell.CalculateRecursive();
			
			tempsum=tempsum + cbw *s.weight;
			
		}
		
		this.value=activationFunction.Activation( tempsum );

		return this.value;
	}
	
}
