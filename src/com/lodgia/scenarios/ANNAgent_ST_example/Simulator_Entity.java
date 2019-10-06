package com.lodgia.scenarios.ANNAgent_ST_example;

public class Simulator_Entity {
	
	double x,y;
	int type;
	
	public Simulator_Entity(double _x, double _y, int _type)
	{
		x=_x;
		y=_y;
		type = _type;
	}
	
	public boolean isType( int _type )
	{
		return this.type == _type;
	}

	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}	
}
