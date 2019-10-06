package com.lodgia.world2d;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.lodgia.genesys.gfx.interfaces.InterfaceWorldGenericRenderer;

public class World2dFileRenderer extends World2dAWTRenderer {

	//Panel p;
	Rectangle bounds;
	BufferedImage bi;
    Graphics bufferGraphics;
    
    final static int w=1024;
    final static int h=768;
    
    String BasePath;

    
	public World2dFileRenderer(World2d pWorld2d, String _BasePath) {
		
		super(pWorld2d);
		
		BasePath = _BasePath;

		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		bufferGraphics=bi.getGraphics();  
		System.out.println("bufferGraphics create = " + bufferGraphics);
		bounds=new Rectangle(0,0,w,h);

	}

	  //called in world2d.render as renderer.paint2(paintobject, bounds);
    public void paintToFile(int state, int member, int iteration) {
    	
       	super.paintToBuffer(bufferGraphics,state,true,bounds,0,0);
    	
    	if(state == InterfaceWorldGenericRenderer.LASTOFBATCH)
    	{
 
        	try {
       	     
        	    File outputfile = new File(BasePath + "/" + String.format("%04d", iteration ) + ".png");
        	    ImageIO.write(bi, "png", outputfile);
        	} catch (IOException e) {
        	    e.printStackTrace();
        	} 	
    	}
    	    	
    }
    
    
}	
	

