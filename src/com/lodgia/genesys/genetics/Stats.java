package com.lodgia.genesys.genetics;

import java.util.ArrayList;

public class Stats {

	private int lastupdatepointer, items;
	private double max=5.0;
	
	public ArrayList<double []> scores;
	
	
	public Stats()
	{
		lastupdatepointer=0;
		items=0;
		scores=new ArrayList<double []>();
	}

	void AddScores(double[] scores)
	{
		for(int t=0; t<scores.length; t++)
		{
			if(scores[t]>max)
			{
				max=scores[t];
			}
		}
		this.scores.add(scores);
		items++;
	}
	
	int lastUpdatePointer()
	{
		return lastupdatepointer;
	}
	
	void updatePointer(int p)
	{
		lastupdatepointer=p;
	}
	
	public int items()
	{
		return items;
	}	
	
	public double maxValue()
	{
		return max;
	}

}



