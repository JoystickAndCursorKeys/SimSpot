package com.lodgia.world2d;

/*
 *  
 *  Returns 1, always
 *    
 */

public class ButtonSensor extends GenericSensor {

	
	double dimishfactor;
	
	public ButtonSensor() {
		// pass
	}

	public void reset() {
		value = 0.0;
	}
	
	public void setup(double _dimishfactor) {
		
		dimishfactor = _dimishfactor;
		value = 0.0;

	}
	
	public void SetSensorValue(double _value) {
		value = _value;
	}
	
	public void Activate() {
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
		
	}

}



