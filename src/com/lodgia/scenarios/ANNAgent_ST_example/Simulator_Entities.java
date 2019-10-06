package com.lodgia.scenarios.ANNAgent_ST_example;

import java.util.ArrayList;

import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationResult;

public class Simulator_Entities {
	public ArrayList<Simulator_Entity> entities  ;
	
	public Simulator_Entities()
	{
		entities = new ArrayList<Simulator_Entity>();
	}
	
	public void Add( Simulator_Entity e )
	{
		entities.add( e );
	}
}
