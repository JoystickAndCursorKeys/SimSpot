package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_FallThrough  implements InterfaceActivationFunction {

	@Override
	public double Activation(double x) {
		return x;
	}
	
	public void reset() {}

}
