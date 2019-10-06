package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_Threshhold  implements InterfaceActivationFunction {

	@Override
	public double Activation(double x) {
		double y;
		
		if( x < 1 )
		    y = 0;
		else 
		    y = 1;
	
		return y;
	}
	
	public void reset() {}

}
