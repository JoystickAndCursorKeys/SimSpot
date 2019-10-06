package com.lodgia.world2d;

/*
 *  
 *  Returns 1, always
 *    
 */

public class UpSensor extends GenericSensor {

	public UpSensor() {
		// pass
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
		
		value=1.0;
		
	}

}



