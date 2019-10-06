package com.lodgia.genesys.genetics.interfaces;

public interface InterfaceGenericGenome {
	
	public void randomize(java.util.Random r);
	public void mutate1genome(java.util.Random r);
	//public void setString(String s);
	public String getAsString();
	public boolean equals( InterfaceGenericGenome other );
	//public int distance( InterfaceGenericGenome other );
	public InterfaceGenericGenome getCopy();
	public InterfaceGenericGenome produceChildSimple(java.util.Random r,InterfaceGenericGenome mate);
	//public String getRawString();
		
}
