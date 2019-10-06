package com.lodgia.genesys.utils;

public class Geometry2D {
	private static final double TWO_PI = 2 * Math.PI;

	public static double normalizeAngle(double a, double center) {
		return a - TWO_PI * Math.floor((a + Math.PI - center) / TWO_PI);
	}
	
	
	static public double[] getIntersectionCircleWithLineSeg(double lp1[], double lp2[],
			double cp[], double cr) {
		// Function sphere_line_intersection (x1#,y1#,z1#, x2#,y2#,z2#,
		// x3#,y3#,z3#,r#)

		double mu;

		double dx1 = (lp2[0] - lp1[0]);
		double dy1 = (lp2[1] - lp1[1]);
		// double dz1=(z2-z1);

		double dx2 = (lp1[0] - cp[0]);
		double dy2 = (lp1[1] - cp[1]);
		// double dz2=(z1-z3);

		double a = dx1 * dx1 + dy1 * dy1; // + dz1*dz1;
		double b = 2 * (dx1 * dx2 + dy1 * dy2 /* + dz1*dz2 */);
		double c = (cp[0] * cp[0]) + (cp[1] * cp[1])/* +(z3*z3) */
				+ (lp1[0] * lp1[0]) + (lp1[1] * lp1[1])/* +(z1*z1) */- 2
				* (cp[0] * lp1[0] + cp[1] * lp1[1]/* +z3*z1 */) - (cr * cr);

		double bm = (b * b);
		double da = 2 * a;
		double fac = (4 * a * c);

		double i = bm - fac;

		double sqi = Math.sqrt(i);

		// ;if segment crosses the sphere
		if (i >= 0) {

			double rv[];
			rv = new double[4];

			// ;incoming intersection
			mu = (-b - sqi) / da;
			rv[0] = lp1[0] + mu * dx1;
			rv[1] = lp1[1] + mu * dy1;
			// picked1(2) = z1 + mu*dz1

			// ;
			// ;outgoing intersection
			// ;remove this if you only need the first intersection point
			mu = (-b + sqi) / da;
			rv[2] = lp1[0] + mu * dx1;
			rv[3] = lp1[1] + mu * dy1;
			// picked2(2) = z1 + mu*dz1
			// ;

			return rv;
		}

		return null;

	}

	static public double[] getIntersectionLineSegWithLineSeg_new_not_working(double p1_x, double p1_y,
			double p2_x, double p2_y, double p3_x, double p3_y, double p4_x,
			double p4_y) {
	
			//intersection copied from http://www.ahristov.com/tutorial/geometry-games/intersection-lines.html
			double i[];
			
		    double d = (p1_x-p2_x)*(p3_y-p4_y) - (p1_y-p2_y)*(p3_x-p4_x);
		    if (d == 0.0) return null;
		    
		    double xi = ((p3_x-p4_x)*(p1_x*p2_y-p1_y*p2_x)-(p1_x-p2_x)*(p3_x*p4_y-p3_y*p4_x))/d;
		    double yi = ((p3_y-p4_y)*(p1_x*p2_y-p1_y*p2_x)-(p1_y-p2_y)*(p3_x*p4_y-p3_y*p4_x))/d;
		    
			i = new double[2];
			i[0]=xi;
			i[1]=yi;
		    return i;
		
	}
	
	
	static public double[] getIntersectionLineSeg(double p0_x, double p0_y,
			double p1_x, double p1_y, double p2_x, double p2_y, double p3_x,
			double p3_y) {
		double s1_x, s1_y, s2_x, s2_y;
		double i[];

		s1_x = p1_x - p0_x;
		s1_y = p1_y - p0_y;
		s2_x = p3_x - p2_x;
		s2_y = p3_y - p2_y;

		if(s1_x==0.0 && s2_x==0.0)
		{
			if(p0_x != p2_x)
			{
				return null; //workaround for vertical lines
			}
		}
		else if(s1_y==0.0 && s2_y==0.0)
		{
			if(p0_y != p2_y)
			{
				return null; //workaround for horizontal lines
			}
		}
		
		
		double s, supperpart, sdiv, t, tupperpart, tdiv;

		supperpart = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y));
		sdiv = (-s2_x * s1_y + s1_x * s2_y);

		tupperpart = (s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x));
		tdiv = (-s2_x * s1_y + s1_x * s2_y);

		if (sdiv == 0) {
			if (supperpart > 0) {
				s = Double.MAX_VALUE;
			} else {
				s = Double.MIN_VALUE;
			}
		} else {
			s = supperpart / sdiv;
		}

		if (tdiv == 0) {
			if (tupperpart > 0) {
				t = Double.MAX_VALUE;
			} else {
				t = Double.MIN_VALUE;
			}
		} else {
			t = tupperpart / tdiv;
		}

		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			// Collision detected
			i = new double[2];

			i[0] = p0_x + (t * s1_x);
			i[1] = p0_y + (t * s1_y);

			return i;
		}

		return null; // No collision

	}

	static public double dist(double x, double y, double xx, double yy) {

		double a, b;
		a = xx - x;
		b = yy - y;

		return Math.sqrt((a * a) + (b * b));
	}
	
}
