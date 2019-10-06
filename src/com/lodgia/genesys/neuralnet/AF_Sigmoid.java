package com.lodgia.genesys.neuralnet;

import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;

public class AF_Sigmoid  implements InterfaceActivationFunction {

	double minusvalues[];
	double plusvalues[];
	
	public AF_Sigmoid() {
		minusvalues = new double[ 101 ];
		plusvalues =  new double[ 101 ];
		
		for( int i=-100; i<101; i++ ) {
			int index = -1;
			if( i < 0 ) {
				index = -i;
				minusvalues[ index ] = SlowActivation( i / 10.0 );
			}
			else if (i==0) {
				minusvalues[ 0 ] = SlowActivation( 0.0 );
				plusvalues[ 0 ] = minusvalues[ 0 ];
			}
			else {
				plusvalues[ i ] = SlowActivation( i / 10.0  );
			}
		}
	}
	
	public double SlowActivation(double x) {
		double y;
		
		if( x < -10 )
		    y = 0;
		else if( x > 10 )
		    y = 1;
		else
		    y = 1 / (1 + Math.exp(-x));
		return y;
	} 
	
	@Override
	public double Activation(double x) {
		double y;
		if( x < -10 ) {
		    y = 0;
		}
		else if( x > 10 ) {
		    y = 1;
		}
		else if( x >= 0 ) {
			int index1, index2;
			double y1,y2,fraction;
			double x10,ceilx10;
			x10 = x*10;
			ceilx10 = Math.ceil( x10 );
			index1 = (int) Math.floor( x10 );
			index2 = (int) ceilx10;
			
			y1 = plusvalues[ index1 ];
			y2 = plusvalues[ index2 ];
			fraction = ceilx10 - ( x10 ); 
			
		    y = y1 * fraction + y2 * (1.0 - fraction);
		}
		else {
			//System.out.println("----");
			//System.out.println("x="+x);
			int index1, index2;
			double y1,y2,fraction;
			double minx10, ceilx10;
			
			minx10 = -x * 10;
			ceilx10 = Math.ceil( minx10 );
			
			index1 = (int) Math.floor( minx10 );
			index2 = (int) ceilx10;
			
			y1 = minusvalues[ index1 ];
			y2 = minusvalues[ index2 ];
			fraction = ceilx10 - ( minx10 ); 

			//System.out.println("index1=" + index1);
			//System.out.println("index2=" + index2);

			//System.out.println("y1=" + y1);
			//System.out.println("y2=" + y2);
			
			//System.out.println("fraction=" + fraction);
		
		    y = y2 * fraction + y1 * (1.0 - fraction);
		    //System.out.println("y=" + fraction);
		}
		return y;		
	} 

	public void reset() {}

	public void test() {
		
		double a1[];
		double a2[];
		
		for( int i=-1100; i<1100; i++ ) {
			double d;
			d = i + 0.0;
			d = d / 100;
			System.out.println("d=" + d );
			System.out.println("slow=" + SlowActivation( d ) );
			System.out.println("fast=" + Activation( d ) );
		}
		
		final int testsize = 11000000;
		final int testdiv = 1000000;
		a1 = new double[ 10 ];
		a2 = new double[ 10 ];
		
		long start_time1 = System.currentTimeMillis();
		for( int i=0; i<testsize; i++ ) {
			double d;
			d = i + 0.0;
			d = d / testdiv;

			a1[ i % 10 ] = SlowActivation( d );
			
		}
		long start_time2 = System.currentTimeMillis();
		for( int i=0; i<testsize; i++ ) {
			double d;
			d = i + 0.0;
			d = d / testdiv;

			a1[ i % 10 ] = Activation( d );
			
		}
		long start_time3 = System.currentTimeMillis();
		
		System.out.println("slow time " + (start_time2 - start_time1) + "ms");
		System.out.println("fast time " + (start_time3 - start_time2) + "ms");
	}
}
