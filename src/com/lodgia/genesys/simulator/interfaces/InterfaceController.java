package com.lodgia.genesys.simulator.interfaces;


public interface InterfaceController {
	
	public int getInputSize();
	
	public int getOutputSize();
	
	public AgentControllerOutputRecord getOutputs();
	
	public void setInputs(double pinputarray[]);
	
	public void process();		
	
	public String debugDump();
	
	
}
