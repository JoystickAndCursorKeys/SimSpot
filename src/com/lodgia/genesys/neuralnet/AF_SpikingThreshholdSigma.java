package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_SpikingThreshholdSigma  implements InterfaceActivationFunction {

	final double decay = 0.78;
	double spiking = 0.0;
	int spike=0;
	AF_Sigmoid sig = new AF_Sigmoid();
	
	@Override
	public double Activation(double x) {
		double y, y2;
		y=0;
		y2=0;
		
		if( x < 1 )
		    y = 0;
		else 
		    y = 1;
	
		if( y == 1 ) {
			spiking = 1;
			spike = 1;
		}
		
		if( spiking > 0.1) 
		{
			y2= sig.Activation( x );
			spike = 1 - spike;
		}
		
		
		return y2;
	}
	
	public void reset() { spike=0; spiking=0.0; }

}


