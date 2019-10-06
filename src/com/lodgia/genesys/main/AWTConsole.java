package com.lodgia.genesys.main;

//look at url: http://zetcode.com/tutorials/javaswingtutorial/menusandtoolbars/

import com.lodgia.genesys.genetics.GenericAlgoritm;
import com.lodgia.genesys.genetics.Stats;
import com.lodgia.genesys.genetics.interfaces.InterfaceEvaluationFunction;
import com.lodgia.genesys.genetics.interfaces.InterfaceSearchAlgoritm;
import com.lodgia.genesys.gfx.interfaces.InterfaceWorldGenericRenderer;
import com.lodgia.genesys.lib.Logger;
import com.lodgia.genesys.problemdef.InterfaceProblemDefinition;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;


public class AWTConsole {

    // Display the frame
    int FRAMEWIDTH = 1200;
    int FRAMEHEIGHT = 800;
    int SCREENDELAY = 1000/75; 
    
	static int  debugLevel;
	static AWTConsole intance;
	
	ViewComponent viewComponent;
	JFrame frame;
	Panel toolbarPanel;
	private JLabel statusbar;

    Button playSingleFromLastEvaluationButton;
    JTextField playCriteriaTextBox;
    Button changePlayBackSpeedButton;
    Button pasteButton;
    Button  exitButton;
	
	MainProgram MP;
	
	Logger l;
	
    public static void launch(MainProgram MP, int pDebugLevel) throws Exception {
    	
    	debugLevel=pDebugLevel;
    	
    	intance=new AWTConsole(MP);
        
    	
    }
    

	public void build() {

		// Create a frame
        frame = new JFrame(MP.getTitle());
 
        frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        
        ImageIcon img = new ImageIcon("test.ico");
        frame.setIconImage(img.getImage());
        
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        
        // Move the window
        frame.setLocation(x, y);
        
        
                
        frame.setLayout ( new BorderLayout());        
       
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
              System.exit(0);
            }
          });
        
        
                
        ActionListener buttonListener = new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {

                String action = ae.getActionCommand();
                
                if (action.equals("Exit")) {
                    close();
                    
                    System.exit(0);
                }  
                else if (action.equals(playSingleFromLastEvaluationButton.getName())) {
                	getMasterController().threaded_callback_handleKeyEvents(
                			AWTInputEvents.IE_PLAYLASTWINNER,
                			playCriteriaTextBox.getText());
                }
                else if (action.equals(changePlayBackSpeedButton.getName())) {
                	getMasterController().threaded_callback_handleKeyEvents(AWTInputEvents.IE_REALTIME);
                }                
            }
            
        };
        
        MenuBar mb;
        /** File, Options, Help */
        Menu fm, om, hm;

        /** Options Sub-Menu */
        Menu opSubm;

        /** The MenuItem for exiting. */
        MenuItem exitItem;

        /** An option that can be on or off. */
        CheckboxMenuItem cb;
        
        mb = new MenuBar();
        frame.setMenuBar(mb); // Frame implements MenuContainer

        MenuItem mi;
        // The File Menu...
        fm = new Menu("File");
        //mi.addActionListener(this);
        //fm.addSeparator();
        fm.add(mi = new MenuItem("Exit", new MenuShortcut('Q')));
        exitItem = mi; // save for action handler
        //mi.addActionListener(this);
        mb.add(fm);
        
        //frame.add(mb, BorderLayout.NORTH);
        
        // Toolbar Panel
        toolbarPanel = new Panel();
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        playSingleFromLastEvaluationButton = new Button("Play From Last");
        playCriteriaTextBox = new JTextField("winner");
        playCriteriaTextBox.setColumns(7);
        playSingleFromLastEvaluationButton.addActionListener(buttonListener);
        playSingleFromLastEvaluationButton.setActionCommand(playSingleFromLastEvaluationButton.getName());
        toolbarPanel.add(playSingleFromLastEvaluationButton);
        toolbarPanel.add(playCriteriaTextBox);
        
        changePlayBackSpeedButton = new Button("Change Playback Speed");
        changePlayBackSpeedButton.addActionListener(buttonListener);
        changePlayBackSpeedButton.setActionCommand(changePlayBackSpeedButton.getName());
        toolbarPanel.add(changePlayBackSpeedButton);

        frame.add(toolbarPanel, BorderLayout.NORTH);
       
        viewComponent=new ViewComponent(MP);
        
        
        // Add a component with a custom paint method
        viewComponent.setFocusable(true);
        frame.add(viewComponent);         
        
        
        //start screen refresh thread
        Thread t=new Thread(viewComponent);
        t.start();
        
        
        KeyAdapter ka=new KeyAdapter() {
       
            public void keyPressed(KeyEvent ke) {
            	
            	System.out.println("1,2,3");
            	int keycode;
            	keycode=ke.getKeyCode();
            	
               	getMasterController().threaded_callback_handleKeyEvents(
               			AWTInputEvents.convert(keycode));
               
            }
        };
        
        viewComponent.addKeyListener(ka);
                
        statusbar = new JLabel(" Statusbar");
        statusbar.setBorder(BorderFactory.createEtchedBorder(
                EtchedBorder.RAISED));
        frame.add(statusbar, BorderLayout.SOUTH);
        

        frame.setVisible(true);
        
        //toolbarPanel.repaint();
        
		
	}
    
    AWTConsole(MainProgram pMI) throws Exception
	{
    	
    	MP=pMI;
    	build();
		
	}
    
    
    void closeFrame()
    {
    	frame.dispose();
       
    }//System.out.println("cycle ended stop="+stop+" status="+status);
	
    static public void close()
    {
    	intance.closeFrame();
       
    }
    
    
    public ViewComponent getWorldViewComponent()
    {
    	return viewComponent;
    }
    
    MainProgram getMasterController()
    {
    	return MP;
    }


    
    class ViewComponent extends Panel implements Runnable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;  //just to not have warnings

		MainProgram appl;
		
		Graph MyGraph;
		//boolean stop;
			
		
	    private int bufferWidth;
	    private int bufferHeight;
	    private Image bufferImage;
	    private Graphics bufferGraphics;
	    
	    private Color black, white;

		private int lastiteration=0;
	    
	    
		ViewComponent(MainProgram pMI)
		{
			super();
			
			System.out.println("New View Component");
			appl=pMI;
			l=new Logger("ViewComponent",debugLevel);
			
	        black = new Color(0,0,0);
	        white = new Color(255,255,255);
	        
			MyGraph=new Graph();
					
			
			//stop=false;
		}
		
	    public void update(Graphics g){
	        paint(g);
	    }
	    
	    
	    
		public void run() {
	    	   
    		while(true)
    		{
    			try {
    				Thread.sleep(SCREENDELAY);
    			} catch (InterruptedException e) { e.printStackTrace(); }
    			
    			if(getMasterController().stopped())
    			{
    				System.out.println("updater main stopped");
    				break;
    			}
    			repaint();
    			//toolbarPanel.repaint();
    			
    		}
    	}    
		
	    public void paint(Graphics g){
	        //    checks the buffersize with the current panelsize

	        //    or initialises the image with the first paint

	        if(bufferWidth!=getSize().width || 
	          bufferHeight!=getSize().height || 
	          bufferImage==null || bufferGraphics==null)
	            resetBuffer();
	        
	        if(bufferGraphics!=null){
	            //this clears the offscreen image, not the onscreen one

	            bufferGraphics.clearRect(0,0,bufferWidth,bufferHeight);

	            //calls the paintbuffer method with 

	            //the offscreen graphics as a param

	            paintBuffer(bufferGraphics);

	            //we finaly paint the offscreen image onto the onscreen image

	            g.drawImage(bufferImage,0,0,this);
	            //g.drawImage(bufferImage,0,0,this);
	        }
	        
	    }

	    private void resetBuffer(){
	        // always keep track of the image size

	    	
	    	
	        bufferWidth=getSize().width;
	        bufferHeight=getSize().height;

	        //    clean up the previous image

	        if(bufferGraphics!=null){
	            bufferGraphics.dispose();
	            bufferGraphics=null;
	        }
	        if(bufferImage!=null){
	            bufferImage.flush();
	            bufferImage=null;
	        }
	        System.gc();

	        //    create the new image with the size of the panel
	        System.out.println("createImage");
	        
	        bufferImage=createImage(bufferWidth,bufferHeight);
	        bufferGraphics=bufferImage.getGraphics();
	        
	        MyGraph.resetBuffer(this, bufferWidth-40,110);
	    }
	    
	    
	
		// This method is called whenever the contents needs to be painted
        public void paintBuffer(Graphics graphicsHandle) {
           
        	// Retrieve the graphics context; this object is used to paint shapes
            Graphics2D g2d = (Graphics2D)graphicsHandle;
            
            InterfaceWorldGenericRenderer renderSolutionWorld;
            InterfaceProblemDefinition r1;
            InterfaceEvaluationFunction simulation;
            InterfaceSearchAlgoritm searchAlgoritm;
            
            r1=appl.genericProblemDef;


			renderSolutionWorld=r1.getSolutionSpaceRenderer();
            
                   
            simulation=r1.getEvaluationClassCopyForPlaying();
			String dataRootDit = r1.getDataRootDir();
            searchAlgoritm=r1.getSearchAlgoritmClass();
            	
            Rectangle bounds=this.getBounds();
            Rectangle boundsWorld = this.getBounds();

            int x0=bounds.width/2;
            int y0=bounds.height/2;
            
            
            //g2d.fillRect(r.x, r.y, r.width, r.height);
            //g2d.clearRect(r.x, r.y, r.width, r.height);
            
            boundsWorld.width -= 300;
            boundsWorld.height -= 125;
            	
            renderSolutionWorld.paint(graphicsHandle, boundsWorld, 200, 0);
            
            g2d.setColor(white);
            g2d.fillRect(0, 0, 199, bounds.height);
            
            DecimalFormat df = new DecimalFormat("#.##");
             
            int y=20;
            int ystep=20;
            g2d.setColor(black);
            //g2d.drawString("Agent xZone="+df.format(ie.getNumericProperty(0)),20,y); y+=ystep;
            //g2d.drawString("Agent yZone="+df.format(ie.getNumericProperty(1)),20,y); y+=ystep;   
            //g2d.drawString("Agent fZone="+df.format(ie.getNumericProperty(2)),20,y); y+=ystep;               
            g2d.drawString("last winner score="+df.format(searchAlgoritm.winnerScore()),20,y); y+=ystep;
            g2d.drawString("last avg    score="+df.format(searchAlgoritm.avgscore()),20,y); y+=ystep;
            g2d.drawString("CurrentMember="+ searchAlgoritm.currentProcessingMember()+"/"+searchAlgoritm.populationSize(),20,y); y+=ystep;
            g2d.drawString("Iteration="+ searchAlgoritm.currentProcessingIteration(),20,y); y+=ystep;
            g2d.drawString("Iteration Len="+ searchAlgoritm.currentProcessingIterationLength(),20,y); y+=ystep;
            y+=ystep;
            y+=ystep;
            g2d.drawString("Simulation Step="+simulation.getCurrentIterationStep(),20,y); y+=ystep;
            g2d.drawString("Agent Score="+df.format(simulation.getAgentCurrentScore(0)),20,y); y+=ystep;
            g2d.drawString("Agent Healt="+df.format(simulation.getAgentCurrentHealth(0)),20,y); y+=ystep;
            g2d.drawString("Agent Hunger="+df.format(simulation.getAgentCurrentHunger(0)),20,y); y+=ystep;
            g2d.drawString("Agent Hungry="+(simulation.getAgentCurrentIsHungry()),20,y); y+=ystep;
            g2d.drawString("Agent Dead="+(simulation.getAgentCurrentIsDead()),20,y); y+=ystep;
            g2d.drawString("Agent Ground="+(simulation.getAgentCurrentGround()),20,y); y+=ystep;
            y+=ystep;
            y+=ystep;
            for( int i=0; i<simulation.getCustomLabelCount(); i++) {
            	g2d.drawString(simulation.getCustomLabel(i) + "=" + simulation.getCustomLabelValue(i),
            			20,
            			y
            	); y+=ystep;
            }

            //g2d.drawLine(x0-10, y0, x0+10, y0);
            //g2d.drawLine(x0, y0-10, x0, y0+10);
            
            MyGraph.Draw(g2d,20,bounds.height-120,searchAlgoritm.getStats(), this, searchAlgoritm.currentProcessingIteration(), false, "");

			if( searchAlgoritm.currentProcessingIteration() != lastiteration ) {
				lastiteration = searchAlgoritm.currentProcessingIteration();
				MyGraph.Draw(g2d,20,bounds.height-120,searchAlgoritm.getStats(), this, searchAlgoritm.currentProcessingIteration(), true, dataRootDit);
			}
        }
        
        
    }


    class Graph
    {
    	
    	Color colors[];	
    	
	    private int bufferWidth;
	    private int bufferHeight;
	    private Image bufferImage;
	    private Graphics bufferGraphics;
	    
	    int localTriggerValue;

	    
    	public Graph()
    	{
    		colors=new Color[2];
    		
    		colors[0]=new Color(0,128,0);
    		colors[1]=new Color(0,0,0);
    		bufferImage = null;
    		bufferGraphics = null;
    		localTriggerValue = -1;
    	}
        	
    	public void resetBuffer(ViewComponent viewComponent, int bufferWidth2, int bufferHeight2) {
    		
    	
	        bufferWidth=bufferWidth2;
	        bufferHeight=bufferHeight2;
        
	        
	        //    clean up the previous image

	        if(bufferGraphics!=null){
	            bufferGraphics.dispose();
	            bufferGraphics=null;
	        }
	        if(bufferImage!=null){
	            bufferImage.flush();
	            bufferImage=null;
	        }
	        System.gc();

	        //    create the new image with the size of the panel

	        bufferImage=viewComponent.createImage(bufferWidth,bufferHeight);
	        bufferGraphics=bufferImage.getGraphics();
	        	
		}

		public void Draw(Graphics2D destG2D, int destX, int destY, Stats stats, ViewComponent viewComponent, int updateTriggerValue, boolean DrawToDisk, String DataRootDir )
    	{
  
			Graphics2D g2d;
		
			Color black,backgroundColor,blue, grey;
			
			double syoff=0;
			double sysize;
			double scaley,scalex;
			double maxscore;
			int statcnt;
			FontMetrics fmtr;
			
			int x,graphy,w,graphh,y,h;
			
			g2d = (Graphics2D)bufferGraphics;

			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 10));
			fmtr = g2d.getFontMetrics();

			x=0;
			y=0;
			graphy=fmtr.getHeight();
			w=bufferWidth;
			h=bufferHeight;
			graphh=bufferHeight -(fmtr.getHeight()*2) ;
			
			sysize=graphh;

			if( localTriggerValue != updateTriggerValue) {
				backgroundColor=new Color(192,192,192,128);
				black=new Color(0,0,0);
				grey=new Color(128,128,128,128);
				blue=new Color(64,64,255);
	
				g2d.clearRect(x, y, w, h);
	            g2d.setColor(black);
	            g2d.drawRect(x, graphy, w-1, (graphh-1));
	            g2d.setColor(backgroundColor);
	            g2d.fillRect(x+1, graphy+1, w-2, (graphh-2));            
	            
				if(stats==null)
				{
					return;
				}
				
				
				statcnt=stats.items();
				maxscore=stats.maxValue();
				
				g2d.setColor(black);
				g2d.drawString(""+(int)maxscore,x,graphy-5); 
				g2d.drawString("0.0",x,graphy+graphh+10);
				
				
				scaley=sysize/(maxscore+1);
				scaley=scaley * .9;
				scalex=w;
				scalex=scalex/(statcnt+1);
				
				int skip=1;
				if(scalex<1.0) {
					double skipf=1/scalex;
					skip=(int)Math.floor(skipf);
				} 
				
				if(skip==0) 
				{skip=1;}
				
				int t;
				int lastt=0;
				int scalexvisibility;
				int scaleyvisibility;
				int flip;
				int flipfact;
				
				
				flip=0;
				scalexvisibility=1;
				while( (statcnt / scalexvisibility ) > 15 )
				{
					if( flip==1 ) {flipfact=2; }
					else {flipfact = 5; }
					flip=1-flip;
					scalexvisibility = scalexvisibility * flipfact;
				}
				
				flip=0;
				scaleyvisibility=1;
				while( (maxscore / scaleyvisibility ) > 5 )
				{
					if( flip==1 ) {flipfact=2; }
					else {flipfact = 5; }
					flip=1-flip;
					scaleyvisibility = scaleyvisibility * flipfact;
				}
				
				/*
				System.out.println("scaleYvisibility="+scaleyvisibility);
				g2d.setColor(grey);
				for(int i=0; i<maxscore; i+=scaleyvisibility)
				{
					int thisy=(int) (-1 + ((syoff+sysize)-(i*scaley)));
					g2d.drawLine(x+1, graphy+thisy, w-2, graphy+thisy );
				}*/

				

				t=1;
				while(t< statcnt)
				{

					double points[], lastpoints[];

					double sx0=(lastt*scalex) ;
					double sx=(t*scalex) ;


					g2d.setColor(black);
					if(t == statcnt-1 ) {
						g2d.drawString(""+t,x+(int)sx-10,graphy+graphh+10);
					}
					else if( ((t % scalexvisibility ) == 0) ) {
						g2d.drawString(""+t,x+(int)sx-5,graphy+graphh+10);
					}

					if(t == statcnt-1 || ((t % scalexvisibility ) == 0) ) {

						g2d.setColor(grey);
						g2d.drawLine(x+(int)sx,graphy,x+(int)sx,graphy+graphh-1);
					}


					points=stats.scores.get(t);
					lastpoints=stats.scores.get(lastt);
					
					for(int p=0; p<points.length; p++)
					{
						
						double mypoints;
						double lastmypoints;
						mypoints=points[p];
						lastmypoints=lastpoints[p];
										
						double sy0=-1 + ((syoff+sysize)-(lastmypoints*scaley));
						double sy=-1 + ((syoff+sysize)-(mypoints*scaley));
						
						if( p==0  && t==1 )
						{
	    					g2d.setColor(blue);
	    					g2d.drawLine(x,graphy+(int)sy,x+(int)w,graphy+(int)sy);	    						
						}
						
						g2d.setColor(colors[p]);
						g2d.drawLine(x+(int)sx0,graphy+(int)sy0,x+(int)sx,graphy+(int)sy);
							    		

					}
				
					lastt=t;
					t=t+skip ;
				}
				localTriggerValue = updateTriggerValue;
			}
			destG2D.drawImage(bufferImage,destX,destY,viewComponent);

			if( DrawToDisk ) {

				BufferedImage bi = (BufferedImage)bufferImage;
				try {
					ImageIO.write(bi, "png", new File(DataRootDir + "/graph.png"));

					PrintWriter out = new PrintWriter(DataRootDir + "/graph.txt");
					statcnt=stats.items();
					for(int s=0; s<statcnt; s++) {
						double points[];
						points = stats.scores.get(s);
						out.println( "" + points[0]);

					}
					out.close();


				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				    		
    	}
    	
    }
	
}




