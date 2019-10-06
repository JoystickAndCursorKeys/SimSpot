package com.lodgia.world2d;

/*
 *  
 *  Returns 1, always
 *    
 */

public class PingSensor extends GenericSensor {

	
	double dimishfactor;
	double olddirectionangle;
	
	public PingSensor() {
		// pass
	}

	public void reset() {
		value = 0.0;
		directionangle = olddirectionangle;
		System.out.println(this.getClass().getName() + " reset");
	}
	
	public void setup(double _dimishfactor) {
		
		dimishfactor = _dimishfactor;
		olddirectionangle = directionangle;
		value = 0.0;
		range = 100;

	}
	
	public void SetSensorValue(double _value) {
		value = _value;
	}
	
	private void Activate() {
		SetSensorValue(1.0);
	}	
	
	public double getSensorValue() {
		return value;
	}

	public double getDebugValue_SensorAngleToObj() {
		return foundangle;
	}

	public double getSensorRange() {
		return range;
	}

	public double getSensorDirectionAngle() {
		return directionangle;
	}

	public void sense(World2d w2d, PhysicalObject host) {
		
		value=value * dimishfactor;
		directionangle = directionangle + .1;
		if(value < .1 ) {
			Activate();
			directionangle = 0;
		}
	}

}



