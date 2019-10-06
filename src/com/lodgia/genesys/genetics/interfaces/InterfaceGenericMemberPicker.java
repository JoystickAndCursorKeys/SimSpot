package com.lodgia.genesys.genetics.interfaces;



public interface InterfaceGenericMemberPicker {
		
	public void registerScore( int memberid,double score);
		
	public int chooseMember();
	
	public void reInit();
	
}
