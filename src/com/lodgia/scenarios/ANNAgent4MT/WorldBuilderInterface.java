package com.lodgia.scenarios.ANNAgent4MT;

import com.lodgia.genesys.utils.TypedProperties;
import com.lodgia.world2d.World2d;

public interface WorldBuilderInterface {
	
	public void buildWorld(World2d w2d, Simulator_Entities es, TypedProperties cfg, int iteration);
	public double getGravity();
	public int getAgentSize();

}
