package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_SigmoidThreshHold  implements InterfaceActivationFunction {

	@Override
	public double Activation(double x) {
		double y, y2, y3;
		
		if( x < -10 )
		    y = 0;
		else if( x > 10 )
		    y = 1;
		else
		    y = 1 / (1 + Math.exp(-x));
		
		y2 = 0;
		if( x > 5 ) {
			y2 = 1;
		}
		
		y3 = ( y + y2 ) / 2;
		return y;
	} 

	public void reset() {}
}
