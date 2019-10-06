package com.lodgia.genesys.neuralnet;
import java.util.ArrayList;
import java.util.Random;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;
// 
// Todo, how about dynamic activation functions
//       or perhaps better with dynamic neurons?
//       speed, speed, speed!!!

public class LayeredNetwork {

	String name;
	ArrayList<Layer> ilayers ;
	ArrayList<Layer> hlayers ;
	ArrayList<Layer> olayers ;
	
	Random generator;
	
	public LayeredNetwork(String pName)
	{
		name=pName;
		
		ilayers = new ArrayList<Layer>();
		hlayers = new ArrayList<Layer>();
		olayers = new ArrayList<Layer>();
		
		generator = new Random();
		
	}
	
	public void connectAllForward_x(Layer layerSource,Layer layerDestination,double minRand,double maxRand)
	{
	
		for(int six=0; six< layerSource.neurons.size(); six++)
		{
			Neuron s;
			s=layerSource.neurons.get(six);
			
		
			for(int dix=0; dix< layerDestination.neurons.size(); dix++)
			{
				Neuron destLayerNeuron;
				destLayerNeuron=layerDestination.neurons.get(dix);
				
				double r;
				
				r=generator.nextDouble() * (maxRand-minRand) + minRand;
				
				destLayerNeuron.addSynapseLinkFrom(s,r);
			}
		}
	}
	
	
	public void connectLayerToNeuronForward(
			Layer previousLayer,Neuron thisNeuron,
			double minRand, double maxRand, SynapseList sl) {
		
			
		for(int dix=0; dix< previousLayer.neurons.size(); dix++)
		{
			Neuron prevLayerNeuron;
			prevLayerNeuron=previousLayer.neurons.get(dix);
			
			double r;
			
			r=generator.nextDouble() * (maxRand-minRand) + minRand;
			
			Synapse syn=thisNeuron.addSynapseLinkFrom(prevLayerNeuron,r); //link should be opposits
			///!!!sl.id++;
			sl.synapses.add(syn);
		}
		
	}
	
	
	public void connectNeuronToLayerForward(
			 Neuron thisNeuron, Layer nextLayer, 
			 double minRand, double maxRand, SynapseList sl) {
		
			
		for(int dix=0; dix< nextLayer.neurons.size(); dix++)
		{
			Neuron nextLayerNeuron;
			nextLayerNeuron=nextLayer.neurons.get(dix);
			
			double r;
			
			r=generator.nextDouble() * (maxRand-minRand) + minRand;
			
			Synapse syn=nextLayerNeuron.addSynapseLinkFrom(thisNeuron,r); //link should be opposits
			///!!!sl.id++;
			sl.synapses.add(syn);
		}
		
	}
	
	
	public void connectAllForward(Layer layerSource,Layer layerDestination,double minRand,double maxRand, SynapseList sl)
	{
	
		//int synapseId;
		//synapseId = sl.id;
		for(int six=0; six< layerSource.neurons.size(); six++)
		{
			Neuron sourceLayerNeuron;
			sourceLayerNeuron=layerSource.neurons.get(six);
			
			for(int dix=0; dix< layerDestination.neurons.size(); dix++)
			{
				Neuron destLayerNeuron;
				destLayerNeuron=layerDestination.neurons.get(dix);
				
				double r;
				
				r=generator.nextDouble() * (maxRand-minRand) + minRand;
				
				Synapse syn = destLayerNeuron.addSynapseLinkFrom( sourceLayerNeuron ,r );
				
				if( sl != null ) { sl.synapses.add(syn); }
			}
		}
		//return synapseId;
	}
	
	
	void UNUSED_connectLayersBackward(Layer layersrc,Layer layerdst,double minrand,double maxrand)
	{
	
		for(int t=0; t< layersrc.neurons.size(); t++)
		{
			Neuron s;
			s=layersrc.neurons.get(t);
			
			for(int u=0; t< layerdst.neurons.size(); t++)
			{
				Neuron d;
				d=layerdst.neurons.get(u);
				
				double r;
				
				r=generator.nextDouble() * (maxrand-minrand) + minrand;
				
				s.addSynapseLinkFrom(d,r);
			}
		}
	}	
	

	public Layer newOutputLayer(String layername,long populationcount,
			InterfaceActivationFunction pAf)
	{

		return newLayer(olayers,layername,populationcount,false, pAf);
	}

	public Layer newHiddenLayer(String layername,long populationcount ,
			InterfaceActivationFunction pAf )
	{
		return newLayer(hlayers,layername,populationcount,false, pAf);
	}
	
	public Layer newInputLayer(String layername,long populationcount,
			InterfaceActivationFunction pAf)
	{
		return newLayer(ilayers,layername,populationcount,true, pAf);
	}
	

	Layer newLayer(ArrayList<Layer> layercollection,
			String layername,
			long populationcount, 
			boolean inputlayertype,
			InterfaceActivationFunction pAf )
	{
		Layer layer=new Layer(layername);

		for(int  t=0;t<populationcount ; t++)
		{
			Neuron neuron=new Neuron( pAf );
			layer.add(neuron);	
			
			if(inputlayertype)
			{
				neuron.useAsInput();
			}
		}
		
		layercollection.add(layer);

		return layer;
	}
	
	public void doSimpleFeedForward2()
	{
		for(int li=0; li< olayers.size(); li++)
		{
			Layer l;
			l=olayers.get(li);
			
			for(int ni=0; ni< l.neurons.size(); ni++)
			{
				Neuron n;
				n=l.neurons.get(ni);
				
				@SuppressWarnings("unused")
				double dummy=n.CalculateBackward();
				
			}
		}

	}
	
	public void doSimpleFeedForward()
	{
		for(int li=0; li< olayers.size(); li++)
		{
			Layer l;
			l=olayers.get(li);
			
			for(int ni=0; ni< l.neurons.size(); ni++)
			{
				Neuron n;
				n=l.neurons.get(ni);
				
				@SuppressWarnings("unused")
				double dummy=n.CalculateBackward();
				
			}
		}

	}
	
	public void  doRecursive()
	{

		
		for(int li=0; li< hlayers.size(); li++)
		{
			Layer l;
			l=hlayers.get(li);
			
			for(int ni=0; ni< l.neurons.size(); ni++)
			{
				Neuron n;
				n=l.neurons.get(ni);
				
				n.done=false;
				
			}
		}
		
		for(int li=0; li< olayers.size(); li++)
		{
			Layer l;
			l=olayers.get(li);
			
			for(int ni=0; ni< l.neurons.size(); ni++)
			{
				Neuron n;
				n=l.neurons.get(ni);
				
				n.done=false;
				
			}
		}
		
		for(int li=0; li< olayers.size(); li++)
		{
			Layer l;
			l=olayers.get(li);
			
			for(int ni=0; ni< l.neurons.size(); ni++)
			{
				Neuron n;
				n=l.neurons.get(ni);
				
				@SuppressWarnings("unused")
				double dummy=n.CalculateRecursive();
				
			}
		}
		
	}

	
	
}
