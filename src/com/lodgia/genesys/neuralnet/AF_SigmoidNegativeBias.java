package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_SigmoidNegativeBias  implements InterfaceActivationFunction {

	@Override
	public double Activation(double x) {
		double y;
		
		if( x < -10 )
		    y = 0;
		else if( x > 10 )
		    y = 1;
		else
		    y = 1 / (1 + Math.exp(-x));
		return y -.5;
	} 

	public void reset() {}
}
