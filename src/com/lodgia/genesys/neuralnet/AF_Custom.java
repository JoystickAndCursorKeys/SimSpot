package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_Custom  implements InterfaceActivationFunction {

	AF_Sigmoid sig = new AF_Sigmoid();
	@Override
	public double Activation(double x) {
		double y;
		
		y=sig.Activation( x );
		
		double y2;
		
		if( x < 1 )
		    y2 = y/5;
		else 
		    y2 = 1;
	
		return y2;
	} 

	public void reset() {}
}
