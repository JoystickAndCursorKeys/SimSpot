package com.lodgia.world2d;

import java.util.ArrayList;

import com.lodgia.genesys.simulator.interfaces.AgentControllerOutputRecord;
import com.lodgia.genesys.simulator.interfaces.InterfaceController;


public class PhysicalAgent extends PhysicalObject {

	protected ArrayList<GenericSensor> sensors ;
	
	protected double inputs[];
	protected AgentControllerOutputRecord outputs;
	protected InterfaceController IOProcesser;
		
	public double score;
	public int samedeltadirection,lastdeltadirection;
	public double originx, originy;
	public double currentiterationstep;
	public boolean dead;
	public boolean always_on_ground;
	public double health;
	//public boolean ground;

	public void reset() {
		
		super.reset();
		score = 0.0;				
		samedeltadirection = 0;
		lastdeltadirection = 0;
		originx = 0;
		originy = 0;
		currentiterationstep = 0;
		dead = false;
		always_on_ground = true;
		health = 100.0;	//why? where is it set otherwise
		
		GenericSensor s;
		
		
		for(int t=0; t< sensors.size(); t++)
		{
			s=sensors.get(t);
			
			s.reset();
			
		}		
	}
	
	public String dump(String string) {
		String dmp = "";
		
		dmp += "score="+score+";";
		dmp += "samedeltadirection="+samedeltadirection+";";
		dmp += "originx="+originx+";";
		dmp += "originy="+originy+";";
		dmp += "currentiterationstep="+currentiterationstep+";";
		dmp += "dead="+dead+";";
		dmp += "always_on_ground="+always_on_ground+";";
		dmp += "health="+health+";";
		
		dmp = dmp + ">" + super.dump( string );
		
		return dmp;
	}

	
	public PhysicalAgent()
	{
		//uninitialized constructor, must call init explicit
		
		samedeltadirection = 0;
		lastdeltadirection=0;
		dead = false;
		ground = 0;
		always_on_ground = true;
		
	}
	
	
	public void setInitialPosHeading(float _x, float _y, float _a)
	{
		setPosHeading(_x,_y,_a);
		originx=_x;
		originy=_y;
		super.stop();
		this.dead = false;
		samedeltadirection = 0;
		lastdeltadirection=0;
	}
	
	public PhysicalAgent(InterfaceController PIOProcesser)
	{
		init(PIOProcesser);
		
	}

	public void init(InterfaceController PIOProcesser)
	{
		setIsAgent(true);
		setImmovable(false);
		sensors=new ArrayList<GenericSensor>();
		IOProcesser=PIOProcesser;
		
		inputs=new double[PIOProcesser.getInputSize()];
		always_on_ground = true;
		
	}
	
	public void setController(InterfaceController PIOProcesser)
	{
		IOProcesser=PIOProcesser;
		
	}	
	
	public void addSensor(GenericSensor gs)
	{
		sensors.add(gs);
	}
	
	public void finalizeSensors() throws Exception
	{
		if(sensors.size()!=inputs.length)
		{
			System.err.println("Error(PhysicalAgent.finalizeSensors): Sensor array size ("+sensors.size()+") not the same as the inputs array size ("+inputs.length+")");
			throw new Exception();
		}
		
	}
	
	synchronized public void move( double gravity )
	{
		super.move( gravity );
	}
	
	
	public int getSensorCount()
	{
		
			return sensors.size();
			
	}

	
	public double getSensorValue(int sensorix)
	{
		
		if(sensors.size()>sensorix)
		{
			return sensors.get(sensorix).getSensorValue();
		}
		
		return 0;
			
	}
	
	public double getObjDistance(int sensorix)
	{
		
		if(sensors.size()>sensorix)
		{
			return sensors.get(sensorix).getObjDistance();
		}
		
		return 0;
			
	}	
	
	

	
	public double getSensorRange(int sensorix)
	{
		
		if(sensors.size()>sensorix)
		{
			return sensors.get(sensorix).getSensorRange();
		}
		
		return 0;
			
	}

	
	public double getSensorDirectionAngle(int sensorix)
	{
		
		if(sensors.size()>sensorix)
		{
			return sensors.get(sensorix).getSensorDirectionAngle();
		}
		
		return 0;
			
	}

	
	public void IOProcess(World2d w2d)
	{
		
		GenericSensor s;
		
	
		for(int t=0; t< sensors.size(); t++)
		{
			s=sensors.get(t);
			
			s.sense(w2d, (PhysicalObject) this);
			
		}
		
		
		for(int t=0; t<sensors.size(); t++)
		{
			inputs[t]=sensors.get(t).value;
		}
		
		IOProcesser.setInputs(inputs);
		
		IOProcesser.process();
		
		outputs=IOProcesser.getOutputs();
		
		for(int t=0; t<outputs.outputsarray.length; t++)
		{
			actOnOutput(outputs.connectarray[t],outputs.outputsarray[t]);
		}
		
	
	}
	
	
	
	public void actOnOutput(int outputid, double value)
	{
	
		int deltadirection=0;
		
		if( ground>0 || always_on_ground ) {
		
			if(outputid==0)
			{
				this.applyForwardForce(limit(value,0,1) / 5);
				deltadirection = 0;
			}
			else if(outputid==1)
			{
				this.changePosHeading(0, 0, limit(value,0,.03));
				deltadirection = 1;
			}	
			else if(outputid==2)
			{
				this.changePosHeading(0, 0, -limit(value,0,.03));
				deltadirection = 2;
			}			
			else if(outputid==3)
			{
				if( value > 0.1 ) {
					this.Break( value );
				}
				deltadirection = 0;
			}
	
		
		if(lastdeltadirection == deltadirection)
		{
			samedeltadirection++;
		}
		else
		{
			samedeltadirection=0;
		}
		lastdeltadirection = deltadirection;
		}
		
	}
	
	



	public double getDebugValue_SensorAngleToObj(int sensorix)
	{
		
		if(sensors.size()>sensorix)
		{
			return sensors.get(sensorix).getDebugValue_SensorAngleToObj();
		}
		
		return 0;
			
	}

	double limit(double val, double min, double max)
	{
		if(val<min) {return min;}
		else if(val>max) {return max;}
		return val;
	}
	
	public String debugDumpController()
	{
		return  IOProcesser.debugDump();
	}





	

}

