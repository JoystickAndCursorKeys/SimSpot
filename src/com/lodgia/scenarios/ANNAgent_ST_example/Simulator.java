/* ideas
 * 
 *
 *
 * All ai's start at the same place in the world, so get the same inputs, so get the exact same learning. Maybe putting them slightly different
 * 		will improve the variety.  A variety if this idea is to position them the same, but orientate them differently.
 * 			Tried: Solution graph went flat (never increased), and after many iterations, the pgm got stuck in Mutated duplicate "loop"
 * 
 * Thought:When increasing simlength, scores do not mean the same anymore....  Or actually they do, as they represent how much was eaten.
 * 	But higher simlength means it's harder to keep the score. so....
 *
 * Done: AvoidDuplicates end up in infinite loop, and makes the program halt
 * Done: Idea: Implement hungryness sensor (after not eaten for a while)
 * Done: Idea: Implement ping sensor, maybe several of different interfal
 * Done ( Sortoff ): Iteration length could be tied to survival of agent. Hunger > maxhunger -> dieoff for example.
 * Idea: After stagnation, kill of highest, to give others a chance.
 * Idea2: After stagnation, decrease the score of the highest with afactor related to teh stagnation.
 * Idea: Implement pruning of the population, in long iterations, kill of the utter-failures. But add a randomness in that.
 * Idea: Cross breath, with winners from previous peak iterations
 * Find //STUB, to paint the complete iteration buffer on the screen
 * Find way to add neurons dynamically, and kep relevance to earlier genomes
 * 	- Find way to expand genomes dynamicallys
 * Outputs are binary. 
 * Anything on / off behaviour instead of gradient behaviour could (maybe/probably) inhibit the slope climbing algoritm
 * Hungry buttons are push buttons.
 * Is speed/turn also on/of behaviour?  It would explain the same sizes of al curves.
 * Is it possible todetect "local winners" ?

 * No need tomake itteration longer, when the longest living indevidual does not live that long
 * 
 * IDEA: If for long time no progress, and individual do not live through whole iteration, then add hidde neuron
 * IDEA: Catch up algoritm
 * IDEA: Dynamic population size based on progress
 * 
 * IDEA: Make different phases
 * 	- Different setups for each phase
 *  - Different directories for each phase
 *  - Different "constants" for each phase
 *  - Constants defined in one place, not multiple places
 *  
 *  
 *  IDEA: Reward left / right seekers over non leftrigth seekers
 *  
 *  IDEA: Punish more in the beginning, less later on. (like, learn well when young)
 *  
 *  IDEA: Dynamic population size. Small, but with stagnation, increase.
 */

package com.lodgia.scenarios.ANNAgent_ST_example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.lodgia.genesys.genetics.GenomeDoubleFloat;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationFunction;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationResult;
import com.lodgia.genesys.genetics.interfaces.InterfaceGenericGenome;
import com.lodgia.genesys.gfx.interfaces.InterfaceWorldGenericRenderer;
import com.lodgia.genesys.lib.Logger;
import com.lodgia.genesys.main.MainProgram;
import com.lodgia.genesys.neuralnet.AF_Sigmoid;
import com.lodgia.genesys.neuralnet.AF_SpikingThreshhold;
import com.lodgia.genesys.neuralnet.AF_Threshhold;
import com.lodgia.genesys.neuralnet.AF_ThreshholdKV;
import com.lodgia.genesys.neuralnet.Layer;
import com.lodgia.genesys.neuralnet.LayeredNetwork;
import com.lodgia.genesys.neuralnet.Neuron;
import com.lodgia.genesys.neuralnet.Synapse;
import com.lodgia.genesys.neuralnet.SynapseList;
import com.lodgia.genesys.neuralnet.interfaces.InterfaceActivationFunction;
import com.lodgia.genesys.simulator.interfaces.AgentControllerOutputRecord;
import com.lodgia.genesys.simulator.interfaces.InterfaceController;
import com.lodgia.genesys.utils.Geometry2D;
import com.lodgia.genesys.utils.TypedProperties;
import com.lodgia.world2d.ButtonSensor;
import com.lodgia.world2d.InterfaceWorld2dCollisionCallBack;
import com.lodgia.world2d.PhysicalAgent;
import com.lodgia.world2d.PhysicalObject;
import com.lodgia.world2d.PingSensor;
import com.lodgia.world2d.RandSensor;
import com.lodgia.world2d.SelectiveSensor;
import com.lodgia.world2d.ThinRaySensor;
import com.lodgia.world2d.World2d;

public class Simulator implements InterfaceEvaluationFunction {

	// int evaluationlength;
	public int steps;
	boolean wasInteruptedFlag = false;
	boolean interrupt = false;

	// simulation details
	public World2d w2d;
	java.util.Random r;
	TypedProperties cfg;

	MainProgram mainProgram;
	InterfaceWorldGenericRenderer renderer;

	int debugLevel;
	Logger l;

	agentmonitor a;
	InterfaceActivationFunction Af;
		
	final int  labelcount = 6;
	String labels[ ];
	String labelvalues[ ];
	
	hybridNeuralNet evalSingleAgentController;

	public Simulator(TypedProperties _config,
			InterfaceWorldGenericRenderer pRenderer, int pDebugLevel) {
		debugLevel = pDebugLevel;
		l = new Logger("Evaluation", pDebugLevel);
		cfg = _config;
		
		initLabels();
		evalSingleAgentController = null;
		
		// senangle=(a.getDebugValue_SensorAngleToObj(s)) / Math.PI;

		// evaluationlength=DEFAULTEVALUATIONLENGTH;
		a = null;
		Af = new AF_SpikingThreshhold();
		//((AF_ThreshholdKV)Af).setKeepFactor(.2);
		renderer = pRenderer;
	}

	// public int setEvaluationLength(int l)
	// {
	// return evaluationlength=l;
	// }

	public void initLabels() { 
		labels = new String[ labelcount ]; 
		labels[0] = "ANN inputs";
		labels[1] = "ANN outputs";
		labels[2] = "ANN hiddenlayers-width";
		labels[3] = "ANN hiddenlayers-depth";
		labels[4] = "ANN switched subnets";
		labels[5] = "Simulation World";
		
		labelvalues = new String[ labelcount ];
		
		labelvalues[0] = cfg.gets("SIM_INPUTS");
		labelvalues[1] = cfg.gets("SIM_OUTPUTS");
		labelvalues[2] = cfg.gets("SIM_HIDDENWIDTH");
		labelvalues[3] = cfg.gets("SIM_HIDDENDEPTH");
		labelvalues[4] = cfg.gets("SIM_NETS");
		labelvalues[5] = cfg.gets("SIM_WORLDCLASS");
		
	}
	
	public int getCustomLabelCount() { return labelcount; }
	public String getCustomLabel(int i) { return labels[ i ]; }
	public String getCustomLabelValue(int i)  { 
		return labelvalues[ i ];
	}
	
	public int getCurrentIterationStep() {
		return steps;
	}

	public double getAgentCurrentScore(int agentindex) {

		if (null == a) {
			return 0.0;
		}
		return this.a.score;
	}

	public int getAgentCurrentHunger(int i) {
		if (null == a) {
			return -1;
		}
		return (int) this.a.hunger;
	}

	public int getAgentCurrentGround() {
		if (null == a) {
			return 0;
		}
		return (int) this.a.ground;
	}

	public double getAgentCurrentHealth(int i) {
		if (null == a) {
			return -1;
		}
		return (int) this.a.health;
	}

	public boolean getAgentCurrentIsDead() {
		if (null == a) {
			return true;
		}
		return this.a.dead;
	}

	public boolean getAgentCurrentIsHungry() {
		if (null == a) {
			return true;
		}
		return this.a.hungry;
	}

	public double getNumericProperty(int property) {
		/*
		 * if(null==a) {return 0.0;} if(property==0) {return
		 * a.sections.getsectionx(a.getX());} else if(property==1) {return
		 * a.sections.getsectionx(a.getY());} else if(property==2) {return
		 * a.sections.getworth(a.getX(), a.getY()); }
		 */
		return 0.0;
	}

	public void interrupt() {
		interrupt = true;
	}

	public int getDnaLength() {

		hybridNeuralNet hNN;
		int result;

		hNN = new hybridNeuralNet(cfg.geti("SIM_NETS"), cfg.geti("SIM_INPUTS"),
				cfg.geti("SIM_HIDDENWIDTH"), cfg.geti("SIM_HIDDENDEPTH"),
				cfg.geti("SIM_OUTPUTS"));

		result = hNN.getDnaLength();

		hNN = null;

		return result;
	}

	public boolean wasInterupted() {
		return wasInteruptedFlag;
	}

	public void init() {
		r = new java.util.Random();

		wasInteruptedFlag = false;

	}

	public double evaluateMulti(ArrayList<InterfaceGenericGenome> listgg) {
		l.warning("evaluateMulti not implemented");
		return -1.0;
	}

	public InterfaceEvaluationResult evaluateSingle(
			InterfaceGenericGenome genome, int mode, boolean firstMember, int member, int members,
			int iteration, int simlength,  int batch, int batchCount )   {

		evaluationResult result = new evaluationResult();
		int agentsize;

		if (genome == null) {
			result.score = 0.0;
			return result;
		}

		interrupt = false;

		w2d = new World2d(-350, -300, 350, 300, cfg.geti("SIM_XTILES"),
				cfg.geti("SIM_YTILES"), 0.0);
		w2d.initWorld();

		if (mode == InterfaceEvaluationFunction.MODE_REPLAY
				|| mode == MODE_EVALUATE_SAVERENDERDETAILS) {
			w2d.setRenderer(renderer);
		}

		collider collider;
		collider = new collider();

		w2d.setCollideCallback(collider);
		Simulator_Entities es;

		es = new Simulator_Entities();

		ThinRaySensor bs;
		SelectiveSensor ss;
		PingSensor png;
		RandSensor rnd;
		ButtonSensor bts;
		
		String worldBuilderClassName = this.getClass().getPackage().getName()+"." + cfg.gets("SIM_WORLDCLASS");
		WorldBuilderInterface  Simulator_WorldBuilder;
		try {
			Simulator_WorldBuilder =
				  (WorldBuilderInterface) Class.forName( worldBuilderClassName  ).newInstance();
		} catch (Exception e) {
			System.err.println("Could not load worldbuilder class: " + worldBuilderClassName );
			e.printStackTrace();
			result.score = 0.0;
			return result;
		}
    	
		// if( (iteration / 5) % 2 == 0 ) {
		Simulator_WorldBuilder.buildWorld(w2d, es, cfg, iteration);
		w2d.setGravity(Simulator_WorldBuilder.getGravity());
		agentsize = Simulator_WorldBuilder.getAgentSize();
		// }
		// else
		// {
		// Simulator_WorldBuilder2.buildWorld(w2d, es, cfg, iteration );
		// }
		
		if( ( member == 0 && iteration == 0 ) || evalSingleAgentController == null ) {
			evalSingleAgentController = new hybridNeuralNet(cfg.geti("SIM_NETS"),
					cfg.geti("SIM_INPUTS"), cfg.geti("SIM_HIDDENWIDTH"),
					cfg.geti("SIM_HIDDENDEPTH"), cfg.geti("SIM_OUTPUTS"));

		}
		
		a = new agentmonitor(evalSingleAgentController);
		//a.hunger = 0;
		//a.health = 100.0;

		String ggstring;
		ggstring = genome.getAsString();
		a.label = "number " + (ggstring);

		try {

			FileWriter fr = new FileWriter(cfg.gets("states.dataDir")
					+ "/population_" + iteration + ".txt", true);
			BufferedWriter br = new BufferedWriter(fr);
			PrintWriter out = new PrintWriter(br);

			out.println(ggstring);
			out.close();
			br.close();
			fr.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
			int bp = 1;
		}

		double x = 0.0; // r.nextInt(200)-100;
		double y = 0.0; // r.nextInt(200)-100;

		for (int i = 0; i < es.entities.size(); i++) {
			Simulator_Entity e;
			e = es.entities.get(i);
			if (e.isType(WorldBuilderMazeConverterLib.type_agent)) {
				x = e.getX();
				y = e.getY();
			}
		}
		a.setInitialPosHeading((int) x, (int) y, (float) 0.5); /*
																 * ( (float) (
																 * member +
																 * iteration) )
																 * / 25 );
																 */
		a.setSpeed(0);
		a.setupPolyCircle(agentsize, cfg.geti("SIM_CIRCLEPRECISION"));

		for (int t = -5; t <= 5; t++) {
			double angle = 15 * t;
			bs = new ThinRaySensor();
			bs.setup(150, Math.toRadians(angle));
			a.addSensor(bs);
		}

		png = new PingSensor();
		png.setup(.15);
		a.addSensor(png);

		ss = new SelectiveSensor();
		ss.setup(500, Math.toRadians(0.0), collider.GOODY);
		a.addSensor(ss);

		bts = new ButtonSensor();
		bts.setup(.9);
		a.collidesensor = bts;
		a.addSensor(bts);

		bts = new ButtonSensor();
		bts.setup(.9);
		a.eatsensor = bts;
		a.addSensor(bts);

		bts = new ButtonSensor();
		bts.setup(.9);
		a.hungersensor = bts;
		a.addSensor(bts);

		// rnd=new RandSensor();
		// a.addSensor(rnd);

		try {
			a.finalizeSensors();

		} catch (Exception e) {

			e.printStackTrace();
		}

		adaptBrainToDNA(evalSingleAgentController, genome, cfg.geti("SIM_HIDDENDEPTH"));

		w2d.addObject(a);

		steps = 0;

		if ( firstMember && mode != InterfaceEvaluationFunction.MODE_REPLAY) {
			renderer.paintToFile(InterfaceWorldGenericRenderer.FIRST, member,
					iteration);
		}

		while (!mainProgram.stopped() && !interrupt) {
			a.currentiterationstep = steps;
			w2d.update(iteration);

			if (mode != InterfaceEvaluationFunction.MODE_REPLAY) {
				renderer.paintToFile(InterfaceWorldGenericRenderer.EACH,
						member, iteration);
			} else if (mode == InterfaceEvaluationFunction.MODE_REPLAY) {
				// System.out.println("steps="+steps);
				mainProgram.throttleSpeed();
			}

			if (a.dead) {
				// a.score=0;
				break;

			} else if (steps++ >= simlength) {
				if (mode != InterfaceEvaluationFunction.MODE_REPLAY) {
					break;
				}
			}
		}

		double xdif, ydif;
		xdif = a.getX() - a.originx;
		ydif = a.getY() - a.originy;
		xdif = xdif * xdif;
		ydif = ydif * ydif;

		// a.score = a.score + (Math.sqrt(xdif + ydif) * 5);

		if (mainProgram.stopped() || interrupt) {
			wasInteruptedFlag = true;
		}

		interrupt = false;

		double extrascore = a.health;

		while (extrascore > (a.score / 10)) {
			extrascore = extrascore / 2;
		}

		result.score = a.score + extrascore;

		if ((mode != InterfaceEvaluationFunction.MODE_REPLAY)) {
			renderer.paintToFile(InterfaceWorldGenericRenderer.LAST, member,
					iteration);

			if (member == (members - 1)) {
				renderer.paintToFile(InterfaceWorldGenericRenderer.LASTOFBATCH,
						member, iteration);
			}

		}

		result.simRunLength = (int) a.currentiterationstep;

		return result;

	}

	public void connectToController(MainProgram pApp) {

		mainProgram = pApp;

	}

	void adaptBrainToDNA(hybridNeuralNet b, InterfaceGenericGenome gg,
			int hiddenDepth) {

		b.adaptBrainToDNA(gg);

	}

	double decodeDNA(InterfaceGenericGenome gg, int i) {

		// int items;
		double value = 0.0;
		GenomeDoubleFloat gdf;

		gdf = (GenomeDoubleFloat) gg;

		int start = 0 + (i * cfg.geti("SIM_BITLENGTH"));
		int end = start + cfg.geti("SIM_BITLENGTH");

		// String encodedsynapsevalue;
		// encodedsynapsevalue=Enc_GrayBin.grayStrToBinStr(gg.bits.substring(start,
		// end));
		// value=Enc_NormalizedSignedRealBin12.binStrToReal(encodedsynapsevalue);

		value = gdf.floats[i];

		return value;
	}

	class collider implements InterfaceWorld2dCollisionCallBack {

		static final int GOODY = 100;
		static final int EMPTY = 200;
		static final int MAX_GROUND = 30;

		public boolean Collide(PhysicalObject obj1, PhysicalObject obj2) {
			agentmonitor am;
			PhysicalObject other;

			if (obj1.getIsAgent()) {
				am = (agentmonitor) obj1;
				other = obj2;
			} else if (obj2.getIsAgent()) {
				am = (agentmonitor) obj2;
				other = obj1;
			} else {
				return false;
			}

			if (other.getType() == GOODY) {
				am.score += 5000;

				other.setupPolyCircle(15, 6);
				other.setImmovable(true);
				other.label = "empty";
				other.setType(collider.EMPTY);

				am.tmpcalculatecolissionstatus = false;
				other.tmpcalculatecolissionstatus = false;

				am.eatsensor.Activate();

				return true;
			}
			/*
			 * else if(other.getType()==EMPTY) {
			 * 
			 * am.tmpcalculatecolissionstatus=false;
			 * other.tmpcalculatecolissionstatus=false; return true; }
			 */
			else /* if(other.getType()==0) */
			{

				am.tmpcalculatecolissionstatus = true;
				other.tmpcalculatecolissionstatus = true;

				// System.out.println("collison score *.3");

				am.collidesensor.SetSensorValue(-5);

				am.undoLastMove();
				am.changeSpeed(0);
				am.reverse();

				if (am.getY() > other.getY()) {
					am.ground = MAX_GROUND;
				} else {
					am.health = am.health
							* cfg.getd("SIM_COLLISIONPENALTYMULTIPLIER");
				}

				return true;
			}

			// return false;
		}

	}

	public class agentmonitor extends PhysicalAgent {

		public boolean hungry;
		public ButtonSensor hungersensor;
		static final double dontmovepenalty = 0.2;
		static final double circlepenalty = 0.5;

		double startx, starty;

		double lastx;
		double lasty;
		double dist;
		double lastdeltaheading;
		int lastdeltaheadingsame;
		double lastheading;

		double totalheading = 0.0, tmptothead;

		public double hunger;

		public ButtonSensor collidesensor, eatsensor;

		agentmonitor(InterfaceController PIOProcesser) {
			init(PIOProcesser);

			startx = x;
			starty = y;
			dist = 0;
			lastx = x;
			lasty = y;
			lastdeltaheading = 0;
			lastdeltaheadingsame = 0;
			lastheading = heading;
			dead = false;
			hunger = 0;

			reset();
			setupRectangle(600, 500);

		}

		public void IOProcess(World2d w2d) {

			super.IOProcess(w2d);

			if (getCollisionStatus()) {
				// score=score-50;
				// dead=true;
				// score=score/2;
				// x=lastx;
				// y=lasty;

				// System.out.println("Collission");
				// heading = 180 + heading;

				// return;
			}

			/*
			 * if(lastheading==heading || samedeltadirection >1) { score = score
			 * * 0.98; }
			 */

			/*
			 * totalheading+=heading; tmptothead = totalheading;
			 * if(tmptothead<0) {tmptothead = -tmptothead;} if(tmptothead>1000)
			 * { score=score/2; totalheading=0; }
			 */

			/*
			 * double thresshold=5; if(Math.abs(totalheading)>thresshold) {
			 * score-=thresshold/2; if(score<0) { score=0; }
			 * 
			 * if(totalheading<0) { totalheading+=thresshold; } else {
			 * totalheading-=thresshold; }
			 * 
			 * }
			 */

			double dist, sectionworthfactor;

			w2d.tiles.visit(x, y);
			sectionworthfactor = w2d.tiles.getworth(x, y);
			dist = Geometry2D.dist(x, y, lastx, lasty);

			lastx = x;
			lasty = y;

			score = score + sectionworthfactor; // (dist * sectionworthfactor);

			if (sectionworthfactor > .1) {
				// this.eatsensor.Activate();
				hunger = 0;
			} else {
				hunger = 1 + (hunger * 1.001);
			}

			// ( this.currentiterationstep * HUNGRYAFTERSTEPSITERATIONFACTOR)
			if (hunger > cfg.getd("SIM_STATICHUNGRYLIMIT")) {
				// this.hungersensor.Activate();
				this.hungry = true;
			} else {
				this.hungry = false;
			}

			// ( this.currentiterationstep * DEADAFTERSTEPSITERATIONFACTOR)

			if (true) {
				if (hunger > (cfg.getd("SIM_STATICHUNGRYLIMIT") * 2)) {
					this.dead = true;
				}
			}

			if (this.health < .5) {
				this.dead = true;
			}

		}

	}

	public class hybridNeuralNet implements InterfaceController {

		public class controlSynapse {

			public double weight;

			controlSynapse(double pStartweight) {
				weight = pStartweight;

			}
		}

		int nets, outputs, inputs, hiddens, hiddenlayers;

		neuralBlock neuralBlocks[];
		//controlSynapse controlSynapses[];
		neuralBlock controlBlock;
		neuralBlock winningNeuralBlock;

		AgentControllerOutputRecord or;
		SynapseList sl;
		
		
		public hybridNeuralNet(int Pnets, int Pinputs, int Phiddens,
				int pHiddenLayers, int Poutputs) {

			inputs = Pinputs;
			outputs = Poutputs;
			hiddens = Phiddens;
			nets = Pnets;
			hiddenlayers = pHiddenLayers;
			winningNeuralBlock = null;

			neuralBlocks = new neuralBlock[nets];
			sl = new SynapseList();
			//controlSynapses = new controlSynapse[nets];

			for (int i = 0; i < nets; i++) {
				neuralBlocks[i] = new neuralBlock(Pinputs, Phiddens,
						pHiddenLayers, Poutputs, sl );
			}

			//for (int i = 0; i < nets; i++) {
			//	controlSynapses[i] = new controlSynapse(.1);
			//}

			controlBlock = new neuralBlock(Pinputs, Phiddens, pHiddenLayers,
					nets, sl);

		}

		public int getInputSize() {

			return inputs;

		}

		public void adaptBrainToDNA(InterfaceGenericGenome gg) {

			for( int i=0; i< this.sl.synapses.size(); i++)
			{
				Synapse s;
				s= sl.synapses.get(i);
				s.weight = decodeDNA(gg, i );
			}

		}

		
		public int getDnaLength() {

			int sum;

			sum = 0;
			sum += controlBlock.getDnaLength();
						
			for (int i = 0; i < neuralBlocks.length; i++) {
				sum += neuralBlocks[i].getDnaLength();

			}

			return sum;
		}
		
		public void expandHiddenLayer( int layerNo )
		{
			controlBlock.expandHiddenLayer( layerNo, sl );
			
			for (int i = 0; i < neuralBlocks.length; i++) {
				neuralBlocks[i].expandHiddenLayer( layerNo, sl );

			}			
		}

		public int getOutputSize() {

			return outputs;

		}

		public AgentControllerOutputRecord getOutputs() {

			return winningNeuralBlock.getOutputs();

		}

		public void setInputs(double pinputarray[]) {
			controlBlock.setInputs(pinputarray);
			for (int i = 0; i < nets; i++) { // TODO, only the one that needs it
				neuralBlocks[i].setInputs(pinputarray);
			}
		}

		void CalculateWinningNeuralBlock() {
			double max;
			int index = 0;

			AgentControllerOutputRecord controlRecOutputs;

			controlRecOutputs = controlBlock.getOutputs();
			max = controlRecOutputs.outputsarray[0];
			
			index = 0;
			winningNeuralBlock = neuralBlocks[ index ];
			
			for (int t = 0; t < nets; t++) {
				if (controlRecOutputs.outputsarray[t] > max) {
					max = controlRecOutputs.outputsarray[t];
					index = t;
				}
			}

			winningNeuralBlock = neuralBlocks[ index ];
		}

		public void process() {

			controlBlock.process();

			CalculateWinningNeuralBlock();
			
			winningNeuralBlock.process();

		}


		public String debugDump() {
			String dump = "Not yet implemented------------------\n";

			return dump;
		}

		synchronized String debugDumpLayer(Layer layer) {

			String dump = "";

			int layersize;

			layersize = layer.neurons.size();

			try {

				dump += "Layer " + layer.name + " has " + layersize
						+ " neurons \n";
				for (int t = 0; t < layersize; t++) {
					Neuron n;
					Neuron n2;
					String nstring = "";

					n = layer.neurons.get(t);
					nstring += n;

					int inputsize;

					inputsize = n.synapseInputs.size();

					inputsize = n.synapseInputs.size();

					dump += " Neuron<" + shortaddr(nstring) + "> has ("
							+ inputsize + ") inputs, and fires at (" + n.value
							+ ")\n";
					dump += "  Inputs: ";

					try {
						for (int u = 0; u < inputsize; u++) {
							Synapse s;

							s = n.synapseInputs.get(u);
							n2 = s.inputCell;
							nstring += n2;

							dump += "<" + shortaddr(nstring) + ",W:"
									+ truncate(s.weight) + "> ";
						}
						dump += "\n";

					} catch (Exception e) {

						dump += e.toString() + ".i\n";
					}
				}
			} catch (Exception e) {
				dump += e.toString() + ".o\n";
			}

			return dump;

		}

		private double truncate(double x) {
			long y = (long) (x * 100);
			return (double) y / 100;
		}

		private String shortaddr(String s) {
			String s2[];
			String s3;

			s2 = s.split("[.]");

			s3 = s2[s2.length - 1];

			return s3;
		}

	}

	public class evaluationResult implements InterfaceEvaluationResult {

		public double score;
		int simRunLength;

		public double getScore() {
			return score;
		}

		public double getSimRunLength() {
			return simRunLength;
		}
		
		public void reset() {
			score = -1;
			simRunLength = -1;
		}

	}

	public class neuralBlock implements InterfaceController {

		LayeredNetwork net;

		int outputs, inputs, hiddenlayerwidth, hiddenlayers;

		public Layer myinputs;
		public Layer myhiddens[];
		public Layer myoutputs;
		

		AgentControllerOutputRecord or;

		public neuralBlock(int Pinputs, int PhiddenLayerWidth, int pHiddenLayers,
				int Poutputs, SynapseList sl ) {

			inputs = Pinputs;
			outputs = Poutputs;
			hiddenlayerwidth = PhiddenLayerWidth;
			hiddenlayers = pHiddenLayers;
			// outputsarray=new double[2][outputs];

			or = new AgentControllerOutputRecord(outputs);

			net = new LayeredNetwork("network");

			myinputs = net.newInputLayer("inputs", inputs, Af);
			myhiddens = new Layer[pHiddenLayers];
			for (int i = 0; i < pHiddenLayers; i++) {
				myhiddens[i] = net.newHiddenLayer("hidden" + i, hiddenlayerwidth, Af);
			}
			myoutputs = net.newOutputLayer("outputs", outputs, Af);

			// input to hidden
			net.connectAllForward(myinputs, myhiddens[0],
					-1.0, 1.0, sl );

			// hidden to output
			net.connectAllForward(
					myhiddens[pHiddenLayers - 1], myoutputs, -1.0, 1.0, sl);

			for (int i = 0; i < (pHiddenLayers - 1); i++) {
				net.connectAllForward(myhiddens[i],
						myhiddens[i + 1], -1.0, 1.0, sl);
			}

		}
		
		
		public void expandHiddenLayer(int layerNo, SynapseList sl) {

			Neuron newNeuron;
			Layer previousLayer,nextLayer;
			
			newNeuron = myhiddens[ layerNo ].addNeuron( Af );
			if( layerNo == 0 ) {
				previousLayer = myinputs;
			}
			else {
				previousLayer = myhiddens[ layerNo - 1 ];
			}
			
			nextLayer = myoutputs;
			if( layerNo + 1 <  myhiddens.length ) { 
				nextLayer  = myhiddens[ layerNo + 1 ];
			}

			net.connectLayerToNeuronForward(previousLayer, newNeuron, -1.0, 1.0, sl);
			net.connectNeuronToLayerForward(newNeuron, nextLayer, -1.0, 1.0, sl);
			
		}


		public int getInputSize() {

			return inputs;

		}

		
		public int getDnaLength() {

			return (
					( inputs  *  hiddenlayerwidth ) + 
					( outputs * hiddenlayerwidth ) +
					(( hiddenlayers - 1) * ( hiddenlayerwidth * hiddenlayerwidth ) )
					) ;
		}

		public int getOutputSize() {

			return outputs;

		}

		public AgentControllerOutputRecord getOutputs() {

			for (int t = 0; t < outputs; t++) {
				or.outputsarray[t] = myoutputs.neurons.get(t).value;
				or.connectarray[t] = t;
			}

			return or;

		}

		public void setInputs(double pinputarray[]) {
			Neuron n;
			for (int t = 0; t < inputs; t++) {

				try {

					n = myinputs.neurons.get(t);

					n.clampTo(pinputarray[t]);

					if (pinputarray[t] != 0.0) {
						if (pinputarray[t] > 1.0 || pinputarray[t] < 0.0) {
							int bp = 1;
						}
					}

				} catch (Exception e) {
					;
				}

			}

		}

		public void process() {
			net.doSimpleFeedForward();
		}



		public String debugDump() {

			String dump = "------------------\n";

			dump += debugDumpLayer(myinputs) + "\n";
			for (int i = 0; i < hiddenlayers; i++) {
				dump += debugDumpLayer(myhiddens[i]) + "\n";
			}
			dump += debugDumpLayer(myoutputs) + "\n";

			return dump;

		}

		synchronized String debugDumpLayer(Layer layer) {

			String dump = "";

			int layersize;

			layersize = layer.neurons.size();

			try {

				dump += "Layer " + layer.name + " has " + layersize
						+ " neurons \n";
				for (int t = 0; t < layersize; t++) {
					Neuron n;
					Neuron n2;
					String nstring = "";

					n = layer.neurons.get(t);
					nstring += n;

					int inputsize;

					inputsize = n.synapseInputs.size();

					inputsize = n.synapseInputs.size();

					dump += " Neuron<" + shortaddr(nstring) + "> has ("
							+ inputsize + ") inputs, and fires at (" + n.value
							+ ")\n";
					dump += "  Inputs: ";

					try {
						for (int u = 0; u < inputsize; u++) {
							Synapse s;

							s = n.synapseInputs.get(u);
							n2 = s.inputCell;
							nstring += n2;

							dump += "<" + shortaddr(nstring) + ",W:"
									+ truncate(s.weight) + "> ";
						}
						dump += "\n";

					} catch (Exception e) {

						dump += e.toString() + ".i\n";
					}
				}
			} catch (Exception e) {
				dump += e.toString() + ".o\n";
			}

			return dump;

		}

		private double truncate(double x) {
			long y = (long) (x * 100);
			return (double) y / 100;
		}

		private String shortaddr(String s) {
			String s2[];
			String s3;

			s2 = s.split("[.]");

			s3 = s2[s2.length - 1];

			return s3;
		}

	}


}
