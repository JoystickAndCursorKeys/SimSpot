package com.lodgia.scenarios.ANNAgent4MT;

import com.lodgia.genesys.utils.TypedProperties;
import com.lodgia.world2d.World2d;

public class WorldBuilder1 implements WorldBuilderInterface {
	
	public void buildWorld(World2d w2d, Simulator_Entities es, TypedProperties cfg, int iteration) {
		
		String Maze[];
		
		Maze = new String[17];
		
		Maze[ 0] = "#######################";
		Maze[ 1] = "#                     #";
		Maze[ 2] = "#  #################  #";
		Maze[ 3] = "#  #               #  #";
		Maze[ 4] = "#  #               #  #";
		Maze[ 5] = "#  #               #  #";
		Maze[ 6] = "#  #               #  #";
		Maze[ 7] = "#  #################  #";
		Maze[ 8] = "#  #*                 #";
		Maze[ 9] = "#  ####################";
		Maze[10] = "#  #      #     #     #";
		Maze[11] = "#  #   #  #  #  #  #  #";
		Maze[12] = "#  #   #  #  #  #  #  #";
		Maze[13] = "#  #   #     #     #  #";
		Maze[14] = "#  #################  #";
		Maze[15] = "#                     #";
		Maze[16] = "#######################";
		
		WorldBuilderMazeConverterLib.buildWorld(w2d, es, cfg, iteration, Maze, 30);
		
	}

	public double getGravity() {
		//return .05;
		return 0.0;
	}

	public int getAgentSize() {
		
		return 3;
	}

}
