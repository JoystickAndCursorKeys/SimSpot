package com.lodgia.world2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.lodgia.genesys.gfx.interfaces.InterfaceWorldGenericRenderer;

public class World2dAWTRenderer implements InterfaceWorld2dGenericRenderer {

	World2d world2d;
	
	CoordinateTranslator ct;
	
	Color black;
	Color blue;
	Color red;
	Color green; 
	Color gray;
	Color yellow;
	Color textBgColor;
	Color textBgColorRed;
	Color tileColors[];
	Color yellowTransparent;
	Color whiteTransparent;
	
	
	public class CoordinateTranslator {

		double xOffset;
		double xFactor;
		double yOffset; 
		double yFactor;
		double yInvertFactor;
		
		CoordinateTranslator(  )
		{
			yInvertFactor = -1;
		}
		
		void set( double _xOffset, double _xFactor, double _yOffset, double _yFactor )
		{
			xOffset = _xOffset;
			yOffset = _yOffset;
			xFactor = _xFactor;
			yFactor = _yFactor;
		}		
		
		int xToDisplay( double value )
		{
			return  (int) ( (  value * xFactor ) + xOffset);
		}
		
		int yToDisplay( double value )
		{
			return   (int) ( (yInvertFactor * value * yFactor) + yOffset );
		}
		
		int wToDisplay( double value )
		{
			return  (int) (  value * xFactor);
		}
		
		int hToDisplay( double value )
		{
			return   (int) ( value * yFactor);
		}
		
	}
	
	public World2dAWTRenderer(World2d pWorld2d)
	{
		System.out.println( "Creating new World2dAWTRenderer" );
		setWorld(pWorld2d);
		
		tileColors = new Color[ 256 ];
		for(int i=0; i < 256; i++) {
			tileColors[ i ] = new Color(  ( i ), 128 + ( i/2 ), 128, 64);
		}
		
        black=new Color(0,0,0);
        blue=new Color(0,0,255);
        red=new Color(255,0,0);
        green=new Color(0,255,0);
        gray=new Color(128,128,128);
        yellow=new Color(255,128,0);
        textBgColor = new Color (128,128,128,64);
        textBgColorRed  = new Color (255,64,64,64);
        yellowTransparent  = new Color(255,128,0,64);
        whiteTransparent   = new Color(255,255,255,192);
        
   
        ct = new CoordinateTranslator();
	}
	
	public void setWorld(Object pWorld) {
		
		setWorld((World2d) pWorld);
		
	}
	
	public void setWorld(World2d pWorld2d)
	{
		world2d=pWorld2d;
	}	
	
	
	private void drawAngledLine(Graphics2D g2d, Color c,int x, int y, double angle, double len)
	{

		double lx2,ly2;
    	
    	lx2=x + ( Math.cos( angle ) * len  );
    	ly2=y - ( Math.sin( angle ) * len  );
    	
    	drawLine(g2d, c, x,  y, (int) lx2, (int) ly2 );
    	
	}
	
	
	private void drawLine(Graphics2D g2d, Color c,int x, int y, int x2, int y2)
	{
		Color old;
		old=g2d.getColor();
		
		g2d.setColor(c);
  	
    	g2d.drawLine(x, y, x2, y2);
    	
    	if( c!=old )  {
    		g2d.setColor(old);
    	}
    	
	}
	
	
	
	//called in main as rendersolutionworld.paint(g, r);
    public void paint(Object paintobject, Rectangle bounds, int xoffset, int yoffset ) {
        
    	if(null==world2d) {return;}
    	   	
    	paintToBuffer(paintobject, bounds, xoffset, yoffset);
    	
    }
    
    
    public void paintToFile(int state,int member, int cycle) {
        
    	System.err.println(this.getClass().getSimpleName()+".paintToFile is not implemented");
    	Exception e;
    	e=new Exception();
    	e.printStackTrace();
    }
    
    //called in world2d.render as renderer.paint2(paintobject, bounds);
    public void paintToBuffer(Object paintobject, Rectangle bounds, int xoffset, int yoffset ) {
        // Retrieve the graphics context; this object is used to paint shapes
 
    	paintToBuffer((Graphics) paintobject,InterfaceWorldGenericRenderer.NORMAL,false,bounds,  xoffset,  yoffset);
    }
    
	// This method is called whenever the contents needs to be painted
    protected void paintToBuffer(Graphics g, int state, boolean manyFlag, Rectangle screenBounds, int xoffset, int yoffset) {
              
    	Graphics2D g2d;
    	g2d=(Graphics2D) g;
    	
    	PhysicalAgent a;
        
        int t,u,v,lx,rx,ty,by;
        ArrayList<PhysicalObject> Objects ;
        
        if(null==world2d) {return;}
        
        Objects=world2d.ObjectList();

        //Rectangle r=bounds;
        
        /* offset x and y coordinates, to transform from objectspace coords to screencoords */
        int x0;
        int y0;
        double wx = screenBounds.width;
        double wy = screenBounds.height;
        double xfact, yfact;
        xfact= wx / world2d.tiles.rangex;
        yfact= wy / world2d.tiles.rangey;
        
        int xoffsubstract = (screenBounds.width/2 ) - (world2d.tiles.rangex / 2);
        int yoffsubstract = (screenBounds.height/2 ) - (world2d.tiles.rangey / 2);
        x0= (int) ( xfact * ( world2d.tiles.rangex / 2)) + xoffset; 
        y0= (int) ( yfact *(world2d.tiles.rangey / 2)) + yoffset;           
        ct.set( x0, xfact, y0, yfact);
        
		if(state == InterfaceWorldGenericRenderer.FIRST && manyFlag == true)
		{
	    	g2d.setColor( new Color(  255, 255, 255 ) );
	    	g2d.fillRect(
	    			0,0,
	    			screenBounds.width,
	    			screenBounds.height );
		}
		
        
		if(state == InterfaceWorldGenericRenderer.NORMAL && manyFlag == false)
		{        
	        u=0; while( u < world2d.tiles.xsize )
	        {
	        	lx=world2d.tiles.getLeft(u);// + x0;
	        	rx=world2d.tiles.getRight(u);// + x0;
	        	
	            v=0; while(v<world2d.tiles.ysize)
	            {
	            	double maxvalue = world2d.tiles.getMaxValue();
	            	double value;
	            		            	
	            	Color brightness;
	            	int cvalue;
	            	
	            	ty=world2d.tiles.getTop(v);
	            	by=world2d.tiles.getBottom(v);
	            	value = world2d.tiles.getworth(lx, by);
	            		            	
	            	value = ( value / maxvalue ) * 255;
	            	cvalue = (int) value;
	            	
	            	brightness = tileColors[ cvalue ];
	            	
	            	g2d.setColor(brightness);
	            	g2d.fillRect(
	            			ct.xToDisplay( lx ), 
	            			ct.yToDisplay( ty ), 
	            			ct.wToDisplay( (rx-lx) ) -1, 
	            			ct.hToDisplay( (ty-by) ) -1);
	            	v++;
	            }
	            
	        	u++;
	        }
		}
            
		

    	
    	
        
        /*midscreen cross*/
		//System.out.println("debug: black = " + black );
		//System.out.println("debug: g2d = " + g2d );
		//System.out.println("debug: g = " + g );
		g2d.setColor( black );
        g2d.drawLine( ct.xToDisplay( -10 ), ct.yToDisplay( 0 ), ct.xToDisplay( +10 ), ct.yToDisplay( 0 ));
        g2d.drawLine(ct.xToDisplay( 0 ), ct.yToDisplay( -10 ), ct.xToDisplay( 0 ), ct.yToDisplay( +10 ));
        
       
        t=0;
        while(t<Objects.size())
        {

        	
        	g2d.setColor(black);
        	
        	PhysicalObject o;
        	
        	o=Objects.get(t);

        	a=null;
        	if ( o.getIsAgent() ) {
        		
        		a=(PhysicalAgent)  o;
        	}
        	
        	double xObjectSpace = o.getX();
        	double yObjectSpace = o.getY();
        	double angle = o.getHeading();

        	int xScreen=ct.xToDisplay( xObjectSpace ); /* used for sensor lines starting point */
        	int yScreen=ct.yToDisplay( yObjectSpace );
        	
        	@SuppressWarnings("unused")
			boolean collided;
        	
        	collided=false;
        	
        	if(o.shapetype==PhysicalObject.POLYGON && state == InterfaceWorldGenericRenderer.NORMAL)
        	{
            	//double size = o.getSize();
           	
            	Color mycolor;
            	mycolor=black;
            	
            	if(o.getCollisionStatus()) 
            	{
            		mycolor=yellow;
            		
            		double collisionpoint[];
            		
            		collisionpoint=o.getCollisionPoint();
            		
            		collided=true;
            		
                	int tx0= (int) ct.xToDisplay( collisionpoint[0] );
                	int ty0= (int) ct.yToDisplay( collisionpoint[1] );
          	  	
                	g2d.setColor(green);
                	
                	g2d.drawOval(tx0 - ct.wToDisplay( 10 ) , ty0 - ct.hToDisplay( 10 ), ct.wToDisplay( 20 ), ct.hToDisplay( 20 ));
            		
            	}
            	else if ( o.getIsAgent() ) {
            		
            		if(a.dead) {
            			mycolor=red;
            		}
            	}
            	
            	boolean reusePolygon;
            	
            	Polygon p;
            	if( o.customRenderObject == null || (! o.isimmovable ))
            	{
            		reusePolygon = false;
            		p = new Polygon();
            		o.customRenderObject = p;
            	}
            	else 
            	{
               		reusePolygon = true;
               		p = (Polygon) o.customRenderObject;          			
            	}
            	
            	for(int i=0; i<o.l0a.length; i++)
            	{
            		int tx1;
                	int ty1;
                	int tx2;
                	int ty2;
                	
                	
                	tx1= ct.xToDisplay( o.rendercache.linepoint1_x[i] )  ; 
                	ty1= ct.yToDisplay(  o.rendercache.linepoint1_y[i] )  ; 
                	tx2= ct.xToDisplay( o.rendercache.linepoint2_x[i] )  ; 
                	ty2= ct.yToDisplay(  o.rendercache.linepoint2_y[i] )  ; 
                	  	
                	if( ! reusePolygon ) {
                		p.addPoint(tx1, ty1);
                	}
                	
                	drawLine(g2d, mycolor, tx1, ty1, tx2, ty2);
                	
                	//drawLine(g2d, mycolor, tx1+2+1, ty1+2+1, tx2+2+1, ty2+2+1);     
            	}
            	
            	g2d.setColor(whiteTransparent);
            	g2d.fillPolygon(p);
            	
        	}
        	
           
        	if(o.isimmovable==false && state == InterfaceWorldGenericRenderer.NORMAL)
        	{
        		if(o.getSize()>0) {
        			
        			drawAngledLine(g2d,black,xScreen,yScreen,angle,(int)o.getSize());
        			
        		}	
        		else
        		{
        			
        			drawAngledLine(g2d,black,xScreen,yScreen,angle,30);
        		
        		}
        	}
        	
        	
        	//g2d.drawString("obj="+o.label, xb+10, yb+10);
        	
        	if(o.getIsAgent())
        	{
        		
        		if(state == InterfaceWorldGenericRenderer.NORMAL)
        		{      		
	        		//todo, how to access score
	        		//g2d.drawString("score="+a.score, xb+10, yb+20);
	        		
	        		
	        		int sc=a.getSensorCount();
	        		for(int s=0; s<sc; s++)
	        		{
	        			
	                	double senvalue,  sendirectionangle,  senrange;
	                		           		
	            		senvalue=a.getObjDistance(s);
	            		
	            		sendirectionangle=a.getSensorDirectionAngle(s);
	            		
	            		senrange=a.getSensorRange(s);
	            			            		
	            		drawAngledLine(g2d,red,xScreen,yScreen,angle+(sendirectionangle),senrange);
	            		drawAngledLine(g2d,green,xScreen,yScreen,angle+(sendirectionangle),senvalue);
	        		
	        		}
        		}
        		else
        		{
        			//Color c;
        			g2d.setColor(black);
        		  	
                	int tx0= ct.xToDisplay( xObjectSpace );
                	int ty0= ct.yToDisplay( yObjectSpace );
                	
                	//c=agentColors[ % ]
                	
                	drawLine(g2d, black, tx0+2, ty0+2, tx0+2, ty0+2);
                	
                	if(o.getCollisionStatus()) 
                	{
                		
                		double collisionpoint[];
                		
                		collisionpoint=o.getCollisionPoint();
                		
                		collided=true;
                		
                    	int ctx0= (int) ct.xToDisplay( collisionpoint[0] );
                    	int cty0= (int) ct.yToDisplay( collisionpoint[1] );
              	  	
                    	g2d.setColor(yellowTransparent);
                    	
                    	//g2d.drawOval(ctx0 -5, cty0 - 5 , 5, 5);
                    	
                        /*midscreen cross*/
                        g2d.drawLine(ctx0-5, cty0, ctx0+5, cty0);
                        g2d.drawLine(ctx0, cty0-5, ctx0, cty0+5);
                        
                    	g2d.setColor(black);
                		
                	}
                	
                	if(state == InterfaceWorldGenericRenderer.LAST)
                	{
                		//print score
                		
                		Color old;
                		old=g2d.getColor();
                		
                		if( ! a.dead ) {
                			g2d.setColor(textBgColor);
                		}
                		else {
                			g2d.setColor(textBgColorRed);
                		}
                  	  	g2d.fillRect((tx0+2)-10, (ty0-10)+5, 30, 15);
                    	
                    	//g2d.setBackground(textBgColor);
                    	g2d.setColor(black);
                		g2d.drawString(""+(int)a.score, (tx0+2)-10, ty0+10);
                		g2d.setColor(old);
                	}
                	
        		}
        	}
        	t++;
        }    
    }
}	
	

