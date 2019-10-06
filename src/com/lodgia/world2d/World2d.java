package com.lodgia.world2d;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import com.lodgia.genesys.gfx.interfaces.InterfaceGraphicalWorld;
import com.lodgia.genesys.gfx.interfaces.InterfaceWorldGenericRenderer;


//todo, perhaps can be done with only block on object level
//so, use locklist only when editing the list
//use lock object when editing the object


public class World2d implements InterfaceGraphicalWorld{

	ArrayList<PhysicalObject> Objects ;
	//int objPointer_;
	int _listlocks;
	//public int currentiteration_;
	double gravity;
	
	InterfaceWorld2dGenericRenderer renderer;
	
	InterfaceWorld2dCollisionCallBack ccb;
	
	public tiles tiles;
	
	public class tiles {
		public double storage[][];
		int minx,miny,maxx,maxy;
		int rangex,rangey;
		
		int xsize = 20;
		int ysize = 20;
		
		double maxvalue;
		boolean ground;
		
		tiles(int _minx,int _miny,int _maxx,int _maxy, int _xsize, int _ysize)
		{
			
			minx = _minx;
			maxx = _maxx;
			miny = _miny;
			maxy = _maxy;
			xsize = _xsize;
			ysize = _ysize;
			
			//-300 -> 300
			// 0 -> 5
			// 0 -> -300 (x = x + _minx)
			//x = ((x2-minx) / (rangex)) * 10;  = (300 / 600 ) / 10;
			
			rangex = (maxx - minx);
			rangey = (maxy - miny);
			
			storage = new double[xsize][ysize];
			
			maxvalue = 0.0;
			
			for(int x=0; x<xsize; x++) {
				for(int y=0; y<ysize; y++) {

					storage[x][y] = 0.1 * (x+y);
					
					if( storage[x][y] > maxvalue ) {
						maxvalue = storage[x][y];
					}
				}					
			}
		}
		
		void clear()
		{
			maxvalue = 0.0;
			
			for(int x=0; x<xsize; x++) {
				for(int y=0; y<ysize; y++) {

					storage[x][y] = 0.1 * (x+y);
					
					if( storage[x][y] > maxvalue ) {
						maxvalue = storage[x][y];
					}
				}					
			}		
		}
		
		private int getsectionx(double _x)
		{
			double fx;
			int x;
			
			fx = (_x-minx);
			fx=fx / (rangex);
			fx=fx * xsize;
			x= (int) Math.floor( fx );
			
			if( x < 0) { return 0 ; }
			if( x >= xsize) { return xsize-1 ; }
			return x;
		}
		
		private int getsectiony(double _y)
		{
			double fy;
			int y;
			
			fy = (_y-miny);
			fy=fy / (rangey);
			fy=fy * ysize;
			y= (int) Math.floor( fy );
			
			if( y < 0) { return 0 ; }
			if( y >= ysize) { return ysize-1 ; }			
			return y;
		}
		
		public void visit(double _x,double _y)
		{
						
			int x,y;
			
			x=getsectionx(_x);
			y=getsectiony(_y);
			
			try {
				storage[x][y] =  storage[x][y] * (double) 0.5;
			}
			catch (Exception e) {
				
			}
			
			
		}
		
		public double getworth(double _x,double _y)
		{
			
			int x,y;
			
			x=getsectionx(_x);
			y=getsectiony(_y);
			
			return storage[x][y];
		}

		public int getLeft(int xIndex) {
			
			double x,step;
			
			step = ( rangex / xsize );
			x = minx + (xIndex * step);
			
			return (int) Math.floor( x );
		}

		public int getRight(int xIndex) {
			return getLeft( xIndex + 1 );
		}
		
		public int getBottom(int yIndex) {
			double y,step;
			
			step = ( rangey / ysize );
			y = miny + (yIndex * step);
			
			return (int) Math.floor( y );

		}

		public int getTop(int yIndex) {
			return getBottom( yIndex + 1 );
	
		}

		public double getMaxValue() {
			return maxvalue;
		}
	}
	
	public World2d(  int _minx,int _miny,int _maxx,int _maxy, int _xsize, int _ysize, double _gravity )
	{
		
		renderer=null;
		ccb=null;
		gravity = _gravity;
		tiles = new tiles( _minx,_miny,_maxx,_maxy, _xsize, _ysize);
	}
	
	
	public void resetTiles()
	{
		tiles.clear();
	}
	
	public void setCollideCallback(InterfaceWorld2dCollisionCallBack pCCB)
	{
		ccb=pCCB;
	}
	

	
	public void initWorld()
	{
		Objects=null;
		Objects = new ArrayList<PhysicalObject>();
	}
	
	synchronized public void setRenderer(InterfaceWorldGenericRenderer  r)
	{
		renderer=(InterfaceWorld2dGenericRenderer) r;
		r.setWorld(this);
	}
	
	
	public void addObject(PhysicalObject o)
	{
		Objects.add(o);
		
	}
	
	public void removeObject(PhysicalObject o)
	{
		boolean removed = Objects.remove(o);
		if( removed == false ) {
			System.err.println( "Removal failed of " + o );
		}
		//System.out.println( removed );
	}
		
	
	public synchronized ArrayList<PhysicalObject>  ObjectList()
	{
		return Objects;
	}
	


	
	
	public void update(int iteration)
	{
		
			
		int i,j;
		PhysicalObject o,p;
		
		//currentiteration = currentiteration;
		
		i=0;
		while(i<Objects.size())
		{
			o=Objects.get(i);
			//o.move( gravity );
			o.tmpcalculatecolissionstatus=false;
			i++;
			if(o.ground>0) {
				o.ground--;
			}

		}
		    	
		
		boolean thedebuggedone;
		
		thedebuggedone=false;
		
		i=0;
		j=0;
		while(i<Objects.size())
		{
			
			o=Objects.get(i);
			o.move( gravity );
			
			if(true)
			{
				
				j=i+1;
				while(j<Objects.size())
				{
					thedebuggedone=false;
					
					p=Objects.get(j);
					
					if( o.isimmovable && p.isimmovable ) {
						j++;
						continue;
					}
					
					if(o.checkCollisionSetCollision(p,thedebuggedone))
					{
						o.tmpcalculatecolissionstatus = true;
						p.tmpcalculatecolissionstatus = true;
						
						if(ccb!=null)
						{
							boolean overridebehaviour=ccb.Collide(o,p);
							
							if(!overridebehaviour)
							{
								o.handleColission(p);
							}
						}
						else
						{
							o.handleColission(p);
						}
					}
						
					j++;
				}
				
			}
		
			i++;
		}
				
		
		i=0;
		while(i<Objects.size())
		{
			o=Objects.get(i);
			
			o.colissionstatus=o.tmpcalculatecolissionstatus;
			
			if(o.getIsAgent())
			{
				PhysicalAgent a;
				a=(PhysicalAgent)o;
				
				a.IOProcess(this);
				
			}
			i++;
		}
		
	}

	
	synchronized public void callRenderer(Object paintobject, Rectangle bounds, int xoffset, int yoffset) {
		
		if(renderer!=null)
		{
			renderer.paintToBuffer(paintobject, bounds, xoffset, yoffset);
		}
		
	}

	public void setGravity(double _gravity) {
		gravity = _gravity;
		
	}
	
	
}
