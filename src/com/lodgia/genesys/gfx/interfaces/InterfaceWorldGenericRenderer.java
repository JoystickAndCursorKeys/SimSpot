package com.lodgia.genesys.gfx.interfaces;

import java.awt.Rectangle;

public interface InterfaceWorldGenericRenderer {

	public static final int  NORMAL=1;
	public static final int  FIRST=1;
	public static final int  EACH=2;
	public static final int  LAST=3;
	public static final int  LASTOFBATCH=4;
	
	public void paintToFile(int state,int member, int cycle); 
	
	public void paintToBuffer(Object paintobject, Rectangle bounds, int xoffset, int yoffset); //called in world2d.render as renderer.paint2(paintobject, bounds);
	public void paint(Object paintobject, Rectangle bounds, int xoffset, int yoffset);  //calls in main as rendersolutionworld.paint(g, r);
	
	public void setWorld(Object pWorld);
	
}
