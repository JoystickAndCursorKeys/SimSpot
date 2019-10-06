package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_ThreshholdKV  implements InterfaceActivationFunction {

	double keepfactor = .5;
	double keepfactor2 = 1-keepfactor;
	
	public void setKeepFactor( double pkeepfactor )
	{
		keepfactor = pkeepfactor;
		keepfactor2 = 1-keepfactor;
		
		return;
	}
	
	@Override
	public double Activation(double x) {
		double y;
		
		if( x < 1 )
		    y = 0;
		else 
		    y = 1;
	
		y = ( y * keepfactor2 ) + ( x * keepfactor );
		return y;
	}
	
	public void reset() {}

}
