package com.lodgia.world2d;

import java.util.ArrayList;

import com.lodgia.genesys.utils.Geometry2D;

/*
 * Sensor Idea #1
 *  Calculate line to circleobj center
 *  Calculate angle to circleobj
 *  See if angle is between angle a and c (=beam left and beam right)
 *  if so, return (distance_to_obj_center - obj_radius)
 *  else
 *  	if angle_circleobj < b (=beam midle)
 *  		fake circle_obj_x += obj_Radius
 *  	else 
 *			fake circle_obj_x -= obj_Radius
 *  		Calculate fake line to fake_circleobj center
 *  		Calculate fake angle to fake_circleobj
 *  	
 *  		See if angle is between angle a and c (=beam left and beam right)
 *  		if so, return (distance_to_fake_obj_center - sinus_magic_to_be_figured_out)
 *    
 */

public class SelectiveSensor extends GenericSensor {

	public double distance;
	int goodytype;

	public SelectiveSensor() {
		// pass
	}

	public void setup(double pRange, 
			double pDirectionAngle,
			int _goodytype) {
		range = pRange;
		directionangle = pDirectionAngle;
		goodytype=_goodytype;
		value = 0;
		//System.out.println("setup");

	}

	public double getSensorValue() {
		return value;
	}
	
	public double getObjDistance() {
		return distance;
	}

	public double getDebugValue_SensorAngleToObj() {
		return foundangle;
	}

	public double getSensorRange() {
		return range;
	}

	public double getSensorDirectionAngle() {
		return directionangle;
	}

	void calculateValue() {
		
		if(distance==0.0) {value=0.0;}
		else
		{
			value=1.0-(distance/range);
			if(value>1.0 || value<0.0)
			{
				@SuppressWarnings("unused")
				int breakpoint=1;
			}
		}
		
	}
	
	public void sense(World2d w2d, PhysicalObject host) {
		int t = 0;
		ArrayList<PhysicalObject> Objects;
		PhysicalObject o;
		double x, y, size, x2, y2, size2, dist;
		double newvalue, minangle, maxangle, hostheading;

		Objects = w2d.ObjectList();

		x = host.getX();
		y = host.getY();
		size = host.getSize();
		hostheading = host.getHeading();

		distance = 0;
		boolean found=false;
		
		t=0;
		while (t < Objects.size()) {

			o = Objects.get(t);
			t++;
			
			if (o != host) {

				x2 = o.getX();
				y2 = o.getY();
				
				double linestart[] = { x, y };
				double lineend[] = {
						x + Math.cos(directionangle + hostheading) * range,
						y + Math.sin(directionangle + hostheading) * range 
				};
			
				/*if(o.shapetype==PhysicalObject.CIRCLE)
				{
					
					size2 = o.getRadiusSize();

					double objdist = Geometry2D.dist(x2, y2, x, y);

					if ((objdist - size2) > range) {
						//System.out.println("too far");
						continue;
					}

					double ccenter[] = { x2, y2 };

					double objangle, tmpfoundangle, tmpvalue;
					
					objangle = Geometry2D.normalizeAngle((directionangle + hostheading)
							- Math.atan2(y2 - y, x2 - x), Math.PI);
					
					tmpfoundangle = objangle;
					
					double anglerange;
					anglerange = objangle / Math.PI;

					if (anglerange > .5 && anglerange < 1.5) {
						continue; 
					} else {
						double values[];
						values = Geometry2D.getIntersectionCircleWithLineSeg(linestart, lineend,
								ccenter, size2);

						if (values != null) {
							tmpvalue = Geometry2D.dist(x, y, values[0], values[1]);
							
							if(!found || tmpvalue<value) 
							{
								value=tmpvalue; foundangle=tmpfoundangle;
							}
							
						}
						else {continue; }
					}
					
					
				}
				else */
				
				if (o.shapetype==PhysicalObject.POLYGON && o.type==goodytype)
				{
					
					double maxpointdistance=o.maxdist_from_origin;
					double objdist = Geometry2D.dist(x2, y2, x, y);
					
					
					if ((objdist - maxpointdistance) > range) {
						//System.out.println("poly too far" + ((objdist + maxpointdistance)));
						continue;
						
					}
					
					
					double coordinate[];
					double angle = o.getHeading();
					boolean foundline=true;
					
					for(int i=0; i<o.l0a.length; i++)
	            	{
	            		double L0a,L0l,L1a,L1l;
	            		double lx0,ly0,lx1,ly1;
	            			            			            		
	            		L0a=o.l0a[i]+angle;
	            		L0l=o.l0l[i];
	            		L1a=o.l1a[i]+angle;
	            		L1l=o.l1l[i];
	            		
	            		lx0=x2+(Math.cos(L0a)*L0l);
	            		ly0=y2+(Math.sin(L0a)*L0l);
	            		
	            		lx1=x2+(Math.cos(L1a)*L1l);
	            		ly1=y2+(Math.sin(L1a)*L1l);
						
	            		coordinate=Geometry2D.getIntersectionLineSeg(
								linestart[0], linestart[1],
								lineend[0], lineend[1], 
								lx0, 
								ly0, 
								lx1,
								ly1
								);
	            		
	            		if(coordinate!=null)
	            		{
	    					double objangle;
	    					objangle = Geometry2D.normalizeAngle((directionangle + hostheading)
	    							- Math.atan2(y2 - y, x2 - x), Math.PI);
	    					
	    					double tmpfoundangle, tmpvalue;
	    					
	    					tmpfoundangle = objangle;
	    					tmpvalue = Geometry2D.dist(x, y, coordinate[0], coordinate[1]);

							if(!found || tmpvalue<distance) 
							{
								distance=tmpvalue; foundangle=tmpfoundangle;
								
								//value=distance; 
								calculateValue();
								found=true;
							}
	    					
	            		}
	            		else
	            		{
	            			foundline=false;
	            		}
	            		
	            		if(!foundline) {continue;}
	            	}
					
				}

			}
			
		}
	}

}



