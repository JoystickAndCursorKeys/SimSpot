package com.lodgia.genesys.simulator.interfaces;

public class AgentControllerOutputRecord {

	public double[] outputsarray;	
	public int[]    connectarray;	
	
	public AgentControllerOutputRecord( int outputs)
	{
		outputsarray=new double[outputs];
		connectarray=new int[outputs];		
	}
}
