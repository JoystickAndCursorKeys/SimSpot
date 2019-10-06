package com.lodgia.world2d;

import com.lodgia.genesys.utils.Geometry2D;

//
// todo, optimize, by by putting all data in a synchronized object
//

public class PhysicalObject {

	//private World2d myWorld;
	
	public class rendercache {

		public double linepoint1_angle[],linepoint1_length[];
		public double linepoint2_angle[],linepoint2_length[];
		public double linepoint1_x[],linepoint1_y[];
		public double linepoint2_x[],linepoint2_y[];
		
		
		rendercache() {
			linepoint1_angle=null;
			linepoint1_length=null;
			linepoint2_angle=null;
			linepoint2_length=null;
			linepoint1_x=null;
			linepoint1_y=null;
			linepoint2_x=null;
			linepoint2_y=null;
		}
		
		void init(int pNOfPoints)
		{
			linepoint1_angle=new double[pNOfPoints];
			linepoint1_length=l0l; //length will not change, relative to the original, unless we take changine sizes as well
			linepoint2_angle=new double[pNOfPoints];
			linepoint2_length=l1l; //length will not change, relative to the original, unless we take changine sizes as well
			
			linepoint1_x=new double[pNOfPoints];
			linepoint1_y=new double[pNOfPoints];
			linepoint2_x=new double[pNOfPoints];
			linepoint2_y=new double[pNOfPoints];
		}
		
		
	}
	
	
	public static final int POLYGON =1;
	public static final int CIRCLE  =2;
	public static final int NOTHING = 0;
		
	public static final double TIMESLICEFACTOR = 0.01;
	public static final double MAXFORCE =		 1.00;	
		
	protected double x,y;
	protected double backupposx;
	protected double backupposy;
	protected double heading;
	protected double backupheading;
		
	private double speed;
	private double dx,dy;
	private double dxf,dyf,dyfg;
	
	public String label;
	
	//circle
	private double size,size2;
	
	//polygon
	public double l0a[],l0l[],l1a[],l1l[];
		
	//cache of translated points, both for rendering and for collision detection
	rendercache rendercache;
	public Object customRenderObject;
	
	int nOfLines,LineIx;
	public double maxdist_from_origin;
	
	public boolean tmpcalculatecolissionstatus;	
	boolean colissionstatus;
	private double colissionpoint[]; 
	public int ground;
	private double fraction;
	
	private boolean isagent;
	public  int     shapetype;
	
	public boolean isimmovable;
	
	public double mass;
	
	public double frictionconst,collisionfrictionconst; 
	
	public  int     type;


	public void reset() {

		x=0.0;
		y=0.0;
		backupposx=0.0;
		backupposy=0.0;
		heading=0.0;
		backupheading=0.0;
		speed=0.0;
		dx=0.0;
		dy=0.0;
		dxf=0.0;
		dyf=0.0;
		dyfg=0.0;
		//label=
		dyfg=0.0;
		size=-1.0;
		size2=-1.0;
		colissionstatus = false;
		ground = 0;
		
		renderQuickPositionArrays();
		customRenderObject = null;
		
		//rendercache=
		
		/*
		dmp += "nOfLines="+nOfLines+";";
		dmp += "LineIx="+LineIx+";";
		dmp += "maxdist_from_origin="+maxdist_from_origin+";";
		dmp += "tmpcalculatecolissionstatus="+tmpcalculatecolissionstatus+";";
		dmp += "colissionstatus="+colissionstatus+";";
		dmp += "ground="+ground+";";
		dmp += "fraction="+fraction+";";
		dmp += "isagent="+isagent+";";
		dmp += "shapetype="+shapetype+";";
		dmp += "isimmovable="+isimmovable+";";
		dmp += "mass="+mass+";";
		dmp += "frictionconst="+frictionconst+";";
		dmp += "collisionfrictionconst="+collisionfrictionconst+";";
		dmp += "type="+type+";";
		*/
		
	}
	
	public String dump(String string) {
		String dmp = "";
		dmp += "x="+x+";";
		dmp += "y="+y+";";
		dmp += "backupposx="+backupposx+";";
		dmp += "backupposy="+backupposy+";";
		dmp += "heading="+heading+";";
		dmp += "backupheading="+backupheading+";";
		dmp += "speed="+speed+";";
		dmp += "dx="+dx+";";
		dmp += "dy="+dy+";";
		dmp += "dxf="+dxf+";";
		dmp += "dyf="+dyf+";";
		dmp += "dyfg="+dyfg+";";
		dmp += "label="+label+";";
		dmp += "dyfg="+dyfg+";";
		dmp += "size="+size+";";
		dmp += "size2="+size2+";";
		dmp += "rendercache="+rendercache+";";
		dmp += "nOfLines="+nOfLines+";";
		dmp += "LineIx="+LineIx+";";
		dmp += "maxdist_from_origin="+maxdist_from_origin+";";
		dmp += "tmpcalculatecolissionstatus="+tmpcalculatecolissionstatus+";";
		dmp += "colissionstatus="+colissionstatus+";";
		dmp += "ground="+ground+";";
		dmp += "fraction="+fraction+";";
		dmp += "isagent="+isagent+";";
		dmp += "shapetype="+shapetype+";";
		dmp += "isimmovable="+isimmovable+";";
		dmp += "mass="+mass+";";
		dmp += "frictionconst="+frictionconst+";";
		dmp += "collisionfrictionconst="+collisionfrictionconst+";";
		dmp += "type="+type+";";
		
		return dmp;
	}

	//private boolean moving;
	
	//private int locks;
	//String lockeddby;
	
	
	public  PhysicalObject()
	{
		//myWorld=pMyWorld;MASSTIMEFACTOR
		resetCollision();
		
		//setupSize(10.0);
		setPosHeading(0,0,0);
		setSpeed(0);
		setupFractionTime(1.0);
		
		isagent=false;
		isimmovable=true;
		
		l0a=null;
		l0l=null;
		l1a=null;
		l1l=null;
		
		rendercache = new rendercache();
		customRenderObject = null;
		
		maxdist_from_origin=0.0;
		
		size=-1;
		size2=-1;
		nOfLines=-1;
				
		shapetype=NOTHING;
				
		label="unknown";
		
		frictionconst=0.9;
		collisionfrictionconst = .3;
		
		mass=100;
		
		type=0;
		//locks=0;
	}
	
	public void resetCollision()
	{
		colissionstatus=false;
		colissionpoint=null;
	}
	
	synchronized public double[] getCollisionPoint()
	{
		double rv[];
		rv=new double[2];
		
		rv[0]=0;
		rv[1]=0;
		
		if(colissionpoint!=null)
		{
			rv[0]=colissionpoint[0];
			rv[1]=colissionpoint[1];
		}
		
		return rv;
	}	
	
	
	synchronized public void setType(int pType)
	{
		type=pType;
	}	
	
	
	synchronized public void setIsAgent(boolean pIsAgent)
	{
		isagent=pIsAgent;
	}	
	
	public void setImmovable(boolean pImmovable)
	{
		isimmovable=pImmovable;
	}
	
	
	synchronized public boolean getIsAgent()
	{
	
		return isagent;				
	}
	
	synchronized public int getType()
	{
	
		return type;				
	}	
	
	/*
	public synchronized void blockObj( String mname)
	{
		//System.out.println("blo:blockObj called for "+mname);
		if(locks==0)
		{
			locks=1;
			lockeddby=mname;
			//System.out.println("blo:locks was only 0 - done for "+mname);
			return;
		}
		else
		{
			try {
				//System.out.println("blo:wait by blockObj "+mname+" !!! ------------------------------------------");
				//System.out.println("because of "+lockeddby);
				this.wait();
			} catch (InterruptedException e) {	e.printStackTrace(); }
			
			locks++;
			lockeddby=mname;
			
			//System.out.println("blo: done for "+mname);
			return;
		}
	}
	
	
	public synchronized void unblockObj(String mname)
	{
		//System.out.println("unb:unblockObj called for "+mname);
		if(locks==1)
		{
			locks=0;
			//System.out.println("unb:locks was only 1!!!");
		}
		else
		{
			locks--;
			//System.out.println("unb:notify by unblockObj!!!");
			this.notify();
		}
		
		//System.out.println("unb:done for "+mname);
		
	}
	
	*/
	
	
	synchronized public void   setupFractionTime(double pFraction)
	{
		//System.out.println("setupFractionTime");
		fraction=pFraction;
	}
	
	synchronized public void setupCircle(double pSize)
	{
		
		size=pSize;
		size2=size/2;
		shapetype=CIRCLE;
	}
	
	synchronized public void setupPolygon_Init(int pNOfPoints)
	{
		shapetype=POLYGON;
		nOfLines=pNOfPoints;
		LineIx=0;
		l0a=new double[pNOfPoints];
		l0l=new double[pNOfPoints];
		l1a=new double[pNOfPoints];
		l1l=new double[pNOfPoints];
		
		rendercache.init(pNOfPoints);
		
		maxdist_from_origin=0.0;

	}
	

	synchronized public void setupPolygon_AddLine(double pX0, double pY0, double pX1, double pY1)
	{
		double L0a,L0l,L1a,L1l;
		
		L0a=Math.atan2(pY0, pX0);
		L0l=Math.sqrt(pX0*pX0 + pY0*pY0);
		
		L1a=Math.atan2(pY1, pX1);
		L1l=Math.sqrt(pX1*pX1 + pY1*pY1);		
		
		//store the points as angle,length instead as x,y
		l0a[LineIx]=L0a;
		l0l[LineIx]=L0l;
		l1a[LineIx]=L1a;
		l1l[LineIx]=L1l;
		
		if(L0l>maxdist_from_origin)
		{
			maxdist_from_origin=L0l;
		}
		
		if(L1l>maxdist_from_origin)
		{
			maxdist_from_origin=L1l;
		}
		
		LineIx++;
						
	}
	
	synchronized public void setupPolygon_FinalizeLines()
	{
		renderQuickPositionArrays();
	}
	

	
	synchronized public void setupRectangle(double w,double h)
	{
		setupPolygon_Init(4);
		double w2,h2;
		
		w2=w/2;
		h2=h/2;
		
		setupPolygon_AddLine(-w2,-h2,-w2,h2);
		setupPolygon_AddLine(-w2,h2,w2,h2);
		setupPolygon_AddLine(w2,h2,w2,-h2);
		setupPolygon_AddLine(w2,-h2,-w2,-h2);
		
		setupPolygon_FinalizeLines();
			
	}
	
	
	synchronized public void setupPolyCircle(double r,int segments)
	{
		setupPolygon_Init(segments);
		
		double anglestep;
		
		anglestep=Math.toRadians(360/segments);
		
		for(int t=0; t<segments; t++)
		{
			double x1,y1,x2,y2, angle1,angle2;
			angle1 = t * anglestep;
			angle2= (t+1) * anglestep;
			
			x1=Math.cos(angle1)*r;
			y1=Math.sin(angle1)*r;
			x2=Math.cos(angle2)*r;
			y2=Math.sin(angle2)*r;

			setupPolygon_AddLine(x1,y1,x2,y2);
			
		}
		
		setupPolygon_FinalizeLines();
				
	}
	
	
	synchronized public void renderQuickPositionArrays()
	{
		if(l0a==null) {return;}
		
		for(int t=0; t<l0a.length; t++)
		{
					
			rendercache.linepoint1_angle[t]=l0a[t] + heading;
			rendercache.linepoint2_angle[t]=l1a[t] + heading;
			
			rendercache.linepoint1_x[t]=(Math.cos(rendercache.linepoint1_angle[t])*l0l[t]) + x;
			rendercache.linepoint1_y[t]=(Math.sin(rendercache.linepoint1_angle[t])*l0l[t]) + y;
			
			rendercache.linepoint2_x[t]=(Math.cos(rendercache.linepoint2_angle[t])*l1l[t]) + x;
			rendercache.linepoint2_y[t]=(Math.sin(rendercache.linepoint2_angle[t])*l1l[t]) + y;
			
		}
	}
	
	
	
	synchronized public void setPosHeading(double pX,double pY, double pHeading)
	{
		//System.out.println("setupPos");
		
		saveLastPosAndHeading();
		
		x = pX;
		y = pY;
		
		heading = pHeading;
		
		renderQuickPositionArrays();
		
	}
	
	
	synchronized public void changePosHeading(double pDX,double pDY, double pDHeading)
	{
		//System.out.println("setupPos");
		saveLastPosAndHeading();
		
		x+=pDX;
		y+=pDY;
		
		heading+=pDHeading;
		
		renderQuickPositionArrays();
		
	}
	
	synchronized public void applyForwardForce(double pForce)
	{

		double dspeed;

		if( pForce >= 0) {
			if(pForce>MAXFORCE)
			{
				pForce=MAXFORCE;
			}
			if(pForce<0.0)
			{
				pForce=0.0;
			}
		}
		else
		{
			if(pForce>-MAXFORCE)
			{
				pForce=-MAXFORCE;
			}
			if(pForce<0.0)
			{
				pForce=0.0;
			}
		}
		
		dspeed=( pForce * mass) * TIMESLICEFACTOR;
		
		speed+=dspeed;
		
	}
	
	
	synchronized public void Break(double pForce)
	{

		
		speed=speed / ( pForce * 10 );
		
	}
	
	synchronized public void applyBackwardForce(double pForce)
	{

		if(pForce>MAXFORCE)
		{
			pForce=MAXFORCE;
		}
		if(pForce<MAXFORCE)
		{
			pForce=MAXFORCE;
		}			
		applyForwardForce(-pForce);
		
	}
	
	synchronized public void setSpeed(double pSpeed)
	{

		speed=pSpeed;
		
	}
	
	
	synchronized public void changeSpeed(double pSpeed)
	{

		speed=pSpeed;
		
	}
	
	
	synchronized public double getHeading()
	{
		return heading;
		
	}
	

	synchronized public double getSpeed()
	{
		
		double rv;
		rv=speed;
		
		return rv;
					
	}
	
	synchronized public double getSize()
	{
		return size;			
	}
	
	synchronized public double getRadiusSize()
	{
		return size2;			
	}
	
	synchronized private void calculateDxDy()
	{
		//System.out.println("private call to calculateDxDy");
		//headingradians = Math.toRadians(heading);
		
		dx=Math.cos(heading)*speed;
		dy=Math.sin(heading)*speed;
		
		dxf=dx*fraction;
		dyf=dy*fraction;
			
	}
	

	synchronized public void move( double gravity )
	{
		
		if( !this.isimmovable ) {
			
			saveLastPosAndHeading();
			calculateDxDy();
			
			if( ground == 0 ) {
				y-=dyfg;
				dyfg+=gravity;
			}
			else {
				dyfg=0;
			}
			if(speed!=0.0)
			{
				
				
				x+=dxf;
				y+=(dyf);
				
				
				speed=speed *  frictionconst;
				
				
				//System.out.println("moved with speed "+this.speed+ " to "+this.x+","+this.y);
	
			}
			
			renderQuickPositionArrays();
		}
		
	}
	
	
	synchronized public void undoLastMove()
	{

		this.x=this.backupposx;
		this.y=this.backupposy;
		this.heading=this.backupheading;
					
	}
	
	
	synchronized private void saveLastPosAndHeading()
	{

		this.backupposx = this.x;
		this.backupposy = this.y;
		this.backupheading = this.heading;
					
	}
	
	
	synchronized public boolean checkCollisionSetCollision(PhysicalObject that, boolean debugme)
	{
		boolean newstatus;
		
		if( this == that ) { return false ; }
		
		//System.out.println("check collission between "+this.label+" and "+that.label); 
		
		newstatus=false;

		double thisroughsize=this.maxdist_from_origin;
		double thatroughsize=that.maxdist_from_origin;
		
		double distx=Math.abs(this.x-that.x);
		
		
		if(distx>(thisroughsize+thatroughsize)) {;}
		else
		{

			double disty=Math.abs(this.y-that.y);
			if(disty>(thisroughsize+thatroughsize) || false) {;}
			else
			{
				for(int i=0; i< this.nOfLines; i++)
				{
					for(int j=0; j< that.nOfLines; j++)
					{
						double point[]=Geometry2D.getIntersectionLineSeg(
								
								this.rendercache.linepoint1_x[i], this.rendercache.linepoint1_y[i], this.rendercache.linepoint2_x[i], this.rendercache.linepoint2_y[i],
								that.rendercache.linepoint1_x[j], that.rendercache.linepoint1_y[j], that.rendercache.linepoint2_x[j], that.rendercache.linepoint2_y[j]	                                                        
						);
					
						if(point!=null)
						{
							
							newstatus=true;
							colissionpoint=point;
									
							break;
						}
					}					
				}
			}
		}
		
		if(newstatus) {
			
			//System.out.println("collission between "+this.label+" and "+that.label); 
		
			this.tmpcalculatecolissionstatus=true;
			that.tmpcalculatecolissionstatus=true;

			that.colissionpoint=this.colissionpoint;
			
		} 

		return newstatus;
		
	}
		

	synchronized public void handleColission(PhysicalObject that)
	{
		
		this.naturalFrontalCollide();
		that.naturalFrontalCollide();
		
	}
	
	
	synchronized public void naturalFrontalCollide()
	{
		if(isimmovable == false)
		{
			speed= - speed * collisionfrictionconst;
		
			//double newangle=heading+Math.toRadians(180);
			//heading=newangle;
			
			undoLastMove();
		}
	}
	
	synchronized public void stop()
	{
		dx=0.0;
		dy=0.0;
		speed=0.0;
	}
	
	synchronized public void reverse()
	{
		dx=-dx;
		dy=-dy;
	}
	
	synchronized public double getX()
	{
		return x;
	}
	
	synchronized public double getY()
	{
		return y;
	}	
	
	synchronized public boolean getCollisionStatus()
	{

		return colissionstatus;
	}


	
}
