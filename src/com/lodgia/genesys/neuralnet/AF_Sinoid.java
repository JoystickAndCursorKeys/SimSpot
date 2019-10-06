package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_Sinoid  implements InterfaceActivationFunction {

	@Override
	public double Activation(double x) {
		double y;
		
		y = Math.sin( x );
	
		return y;
	}
	
	public void reset() {}

}
