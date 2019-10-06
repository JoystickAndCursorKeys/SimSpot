package com.lodgia.genesys.utils;

import java.util.Properties;

public class TypedProperties extends Properties {

	public void set( String name, int value ) {
		this.setProperty(name, "" + value );
	}
	
	public void set( String name, double value ) {
		this.setProperty(name, "" + value );
	}
	
	public void set( String name, String value ) {
		this.setProperty(name, value );
	}
	
	public int geti( String name) {
		return Integer.parseInt( this.getProperty(name) );
	}
	
	public double getd( String name ) {
		return Double.parseDouble( this.getProperty(name) );
	}
	
	public String gets( String name ) {
		return this.getProperty(name);
	}
	
}
