package com.lodgia.scenarios.ANNAgent_ST_example;

import com.lodgia.genesys.utils.TypedProperties;
import com.lodgia.world2d.PhysicalObject;
import com.lodgia.world2d.World2d;

public class WorldBuilderMazeConverterLib {
	
	static public Simulator Simulator;

	static public final int type_agent = 1;
	
	static class cell
	{
		boolean wall;
		boolean used;
		boolean entity;
		int type;
	};
	
	static class hblock
	{
		int y;
		int x0,x1;
	};
	
	static class vblock
	{
		int x;
		int y0,y1;
	};
	
	public static void buildWorld(World2d w2d, Simulator_Entities e, TypedProperties cfg, int iteration, String Maze[], int pathw) {

		PhysicalObject o;
		
		//double pathw=49;
		double xoff=-300;
		double yoff=-250;
		
		double posheading;
		
		int x,y,x2,y2;
		
		
		posheading = (iteration % 4000);
		posheading = posheading / 10000;
		if( iteration < cfg.geti("SIM_CHANGEPOINT") ) {
			posheading = 0.05;
		}

		posheading = 0.0;
		
		cell cellMaze[][];
		hblock hblocks[];
		vblock vblocks[];
		
		int xdim = Maze[0].length();
		int ydim = Maze.length;
		
		cellMaze = new cell[xdim][ydim];
		hblocks = new hblock[xdim * ydim];
		vblocks = new vblock[xdim * ydim];

		for( x=0; x<xdim ; x++) {
			for( y=0; y<ydim; y++) {
				y2=(ydim-1)-y;
				cellMaze[x][y] = new cell();
				cellMaze[x][y].used=false;
				cellMaze[x][y].wall=false;
				cellMaze[x][y].entity=false;
				String thischar =Maze[y2].substring(x, x+1);
				if( thischar.equals("#") ) {
					cellMaze[x][y].wall=true;
				}
				else if( !thischar.equals(" ") ) {
					cellMaze[x][y].entity=true;
					cellMaze[x][y].type=getType( thischar.charAt( 0 ) );
				}
				
			}
		}
		

		x=0;
		int hblockcnt=0;
		for( y=0; y<ydim; y++) {
			x=0;
			while( x< xdim )
			{

				if( cellMaze[x][y].wall) {
					x2=x;
					while( x2< xdim && cellMaze[x2][y].wall) {
							x2++;
					}
					
					x2--;
					
					if( x2>x ) {
						@SuppressWarnings("unused")
						int bp2=1;
						hblocks[ hblockcnt ] = new hblock();
						hblocks[ hblockcnt ].y = y;
						hblocks[ hblockcnt ].x0 = x;
						hblocks[ hblockcnt ].x1 = x2;
						hblockcnt++;
						for( int i=x; i<=x2; i++) {
							cellMaze[i][y].used = true;
						}
						
						
					}
					
					x=x2 + 1;
					
					
				}
				x++; 
			}
		}
		
		//--
		
		y=0;
		int vblockcnt=0;
		for( x=0; x<xdim; x++) {
			y=0;
			while( y< ydim )
			{
				if( cellMaze[x][y].wall && !cellMaze[x][y].used) {
					y2=y;
					while( y2< ydim && cellMaze[x][y2].wall && !cellMaze[x][y2].used) {
							y2++;
					}
					
					y2--;
					
					if( y2>y ) {
						@SuppressWarnings("unused")
						int bp2=1;
						vblocks[ vblockcnt ] = new vblock();
						vblocks[ vblockcnt ].x = x;
						vblocks[ vblockcnt ].y0 = y;
						vblocks[ vblockcnt ].y1 = y2;
						vblockcnt++;
						for( int i=y; i<=y2; i++) {
							cellMaze[x][i].used = true;
						}
					}
					y=y2 + 1;
				}
				y++; 
			}
		}
		
		
		xoff = -  ( ((xdim-1) * pathw) / 2.0);
		yoff = -  ( ((ydim -1 )* pathw) / 2.0);
		
		for( int i=0; i<hblockcnt; i++) {
			o=new PhysicalObject();
			double thisx;
			thisx = ( (hblocks[i].x0*pathw) + (hblocks[i].x1*pathw) ) /2;
			o.setPosHeading(xoff+ thisx,  yoff + (hblocks[i].y*pathw) ,posheading);
			o.setupRectangle(pathw * ( (hblocks[i].x1- hblocks[i].x0) + 1 )+.01,pathw+.01);
			o.setImmovable(true);
			o.label="smallbox";
			w2d.addObject(o);
		}
		
		for( int i=0; i<vblockcnt; i++) {
			o=new PhysicalObject();
			double thisy;
			thisy = ( (vblocks[i].y0*pathw) + (vblocks[i].y1*pathw) ) /2;
			o.setPosHeading(xoff + (vblocks[i].x*pathw), yoff+ thisy ,posheading);
			o.setupRectangle(pathw+.01, pathw * ( (vblocks[i].y1- vblocks[i].y0) + 1 )+.01);
			o.setImmovable(true);
			o.label="smallbox";
			w2d.addObject(o);
		}	
		
		
		for( int ry=0; ry<Maze.length ; ry++) {
			y=ry;
			for( x=0; x<Maze[y].length() ; x++) {
				if( !cellMaze[x][y].used && cellMaze[x][y].wall) {
					o=new PhysicalObject();
					o.setPosHeading(xoff+ (x*pathw),  yoff + (y*pathw) ,posheading);
					o.setupRectangle(pathw+.01,pathw+.01);
					o.setImmovable(true);
					o.label="smallbox";
					w2d.addObject(o);
				}
				else if( !cellMaze[x][y].used && cellMaze[x][y].entity) {
					e.Add(new Simulator_Entity(xoff+ (x*pathw),  yoff + (y*pathw), cellMaze[x][y].type));			
				}
			}
		}
	}

	private static int getType(char charAt) {
		if( charAt == '*') {
			return type_agent;
		}
		return 0;
	}
	

}
