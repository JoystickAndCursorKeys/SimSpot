package com.lodgia.world2d;

public class GenericSensor {

	
	protected double range;
	protected double directionangle;
	@SuppressWarnings("unused")
	private double rangeangle;
	protected double value;
	protected double distance;
	protected double foundangle;
	
	GenericSensor()
	{
		value=0;
	}
	
	
	double getSensorValue()
	{
		return value;
	}
	
	double getDebugValue_SensorAngleToObj()
	{
		return foundangle;
	}
	
	
	double getSensorRange()
	{
		return range;
	}
	
	double getSensorDirectionAngle()
	{
		return directionangle;
	}
	
	
	void sense(World2d w2d, PhysicalObject host)
	{
		
	}


	public double getObjDistance() {

		return distance;
	}


	public void reset() {
		/* For sensors that have an internal state */
	}
	
	
	
	
}
