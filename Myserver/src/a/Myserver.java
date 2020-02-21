package a;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;




class MyServer extends JFrame{
	
	private static int maxConnection=3;
	private static Socket[] incoming;
	private static boolean[] flag;
	private static InputStreamReader[] isr;
	private static BufferedReader[] in;
	private static PrintWriter[] out;
	private static ClientProcThread[] myClientProcThread;
	private static int member;
	private  static int num =1;
	private static int size = 20;
	private static int n;
	private static boolean check;
	   
	
	
	
	
	
	
	
	public synchronized static void SendAll(String str, String myName){
		
		for(int i=1;i<=member;i++){
			if(flag[i] == true){
				out[i].println(str);
				out[i].flush();
			}
		}	
	}
	
	
	public static void SetFlag(int n, boolean value){	
		flag[n] = value;
	}
	
	public static void reset_serve(){
		check = true;
	}
	
	
	public static void main(String[] args) {
		Entity play1 = new Entity(size);
		Entity play2 = new Entity(size);
		
		incoming = new Socket[maxConnection];
		flag = new boolean[maxConnection];
		isr = new InputStreamReader[maxConnection];
		in = new BufferedReader[maxConnection];
		out = new PrintWriter[maxConnection];
		myClientProcThread = new ClientProcThread[maxConnection];
		JOptionPane.showMessageDialog(null, "Server open it", "InfoBox: " + "status", JOptionPane.INFORMATION_MESSAGE);     
		n = 1;
		member = 0;
		Game play = new Game(play1,play2);
		try {
			System.out.println("The server has launched1!");
			ServerSocket server = new ServerSocket(8000);
			while (true) {
				System.out.println(n);
				incoming[n] = server.accept();
				flag[n] = true;
				System.out.println("Accept client No." + n);
				
				isr[n] = new InputStreamReader(incoming[n].getInputStream());
				in[n] = new BufferedReader(isr[n]);
				out[n] = new PrintWriter(incoming[n].getOutputStream(), true);
				if(n==1) {
				myClientProcThread[n] = new ClientProcThread(n, incoming[n], isr[n], in[n], out[n], play, play1, play2);
				}else {
			    myClientProcThread[n] = new ClientProcThread(n, incoming[n], isr[n], in[n], out[n], play, play2, play1);
				}
				member = n;
				//myClientProcThread[n].start();
				n++;
				System.out.println("wait for another player");
		  if(n==3){
			  	myClientProcThread[1].start();
			  	myClientProcThread[2].start();
			  	play.starttime();
				play.setVisible(true);
			      }
				
		  
			}
		} catch (Exception e) {
			System.err.println("error happen when create soccet: " + e);
		}
		
		
	}
	
	
	
	
	public static class Game extends JFrame  {
		public static int Px1=400, Py1=100, Px2=400, Py2=700;
		public static int x1, y1, x2, y2;
		public static Entity play1,play2;
		public static boolean gameclear=false, timeup=false, running=true;
		public static int width = 800;
	    public static int height = 800;
	    private static  Graphics2D g2d;
		private static BufferedImage image;
		private static int size = 20;
		private static long targetTime;
		private static Thread thread;
	    

		static Thread threadtime = new Thread(() -> {
	        try {
	            Thread.sleep(60000);
	        } catch (InterruptedException e) {
	            throw new IllegalStateException(e);
	        }
	        timeup=true;
	        checkending(2);
	    });    
	    
		   
	    
	    

		public Game(Entity p1, Entity p2) {
			
			
			this.setPreferredSize(new Dimension(width ,height));
			this.setFocusable(true);
			this.requestFocus();
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    this.setLocationRelativeTo(null);
		    this.setResizable(false);
			this.pack();
			play1 = p1;
			play2 = p2;
			
			Thread t = new Thread(new Runnable(){
			    public void run(){
			        
			        setUp();
			        long start;
			        long els;
			        long wait;
			        while(running){
			            start = System.nanoTime();
			            update();
			            rRender();
			            els = System.nanoTime() - start;
			            wait = targetTime - els  / 2147483647;

			            if(wait > 0){
			                try{
			                    thread.sleep(wait);

			                }catch(Exception e){
			                    e.printStackTrace();
			                }
			            }
			        }
			    }
			});
			t.start();
			
			
			
			
			
		}




		



		private void rRender() {
		  render(g2d);
		  Graphics g = getGraphics();
		  g.drawImage(image,0,0,null);
		  g.dispose();

		}

		private void render(Graphics2D g2d2) {
		  g2d2.clearRect(0,0,width,height);
		  g2d.setColor(Color.blue);
		  play1.render(g2d);
		  
		  g2d.setColor(Color.GREEN);
		  play2.render(g2d);
		 

		  end();

		}


		private void setFPS(int x) {
		  targetTime = 1000/x;
		}

		private void update() {


		  if(gameclear==true || timeup==true){
		     
		          play1.setPosition(Px1, Py1);
		      

		     
		          play2.setPosition(Px2,Py2);
		          running = false;

		  }

		  
		 

		  }


		public void starttime(){
			threadtime.start();
		}


		private void setUp() {
		  image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		  g2d = image.createGraphics();
		  running = true;
		  setUpLevel();
		  setFPS(30);
		  //threadtime.start();
		}

		private void setUpLevel() {
		  
		  play1.setPosition(400,100);
		  play2.setPosition(400,700);
		  
		  
		}

	    
		private synchronized  void end() {

			  if(gameclear==true){
			      g2d.setColor(Color.white);
			      g2d.drawString("Player 1 is Winner!!!" , 350, 400);
		    
			  }

			  if(timeup==true){
			      g2d.setColor(Color.white);
			      g2d.drawString("Player 2 is Winner!!!" , 350, 400);
			     
			  }
			
		}
		
		
		public synchronized static void checkkey(int a, int b, Entity play) {
			String s;
			try {
				//Thread.sleep(1000);
			}catch(Exception e){

			}
			   if(a==1) {

					      if (b == KeyEvent.VK_LEFT){
					    	  
					    	  x1 = play.getX();
					    	  play.setX(x1-20);
					    	  
					      }

					      if (b == KeyEvent.VK_RIGHT){
					    	  
					    	  x1 = play.getX();
					    	  play.setX(x1+20);
					      }

					      if (b == KeyEvent.VK_UP){
					    	  
					    	  y1 = play.getY();
					    	  play.setY(y1-20);
						      
					      }

					      if (b == KeyEvent.VK_DOWN){
					    	  
					    	  y1 = play.getY();
					    	  play.setY(y1+20);
					      }
					
							
					      
			         } else if(a==2){

					    	 if (b == KeyEvent.VK_LEFT){
						    	  
						    	  x2 = play.getX();
						    	  play.setX(x2-20);
						    	  
						      }

						      if (b == KeyEvent.VK_RIGHT){
						    	  
						    	  x2 = play.getX();
						    	  play.setX(x2+20);

						      }

						      if (b == KeyEvent.VK_UP){
						    	  
						    	  y2 = play.getY();
						    	  play.setY(y2-20);
						      }

						      if (b == KeyEvent.VK_DOWN){
						    	  
						    	  y2 = play.getY();
						    	  play.setY(y2+20);
						   
						      }
						      
						    	  
									
									
								
						   }
			      if(play.getX() < 0) play.setX(0);
			      if(play.getY() < 0) play.setY(0);
			      if(play.getX() > width) play.setX(780);
			      if(play.getY() > height) play.setY(height-40);
                                     s = play1.getX() + ","+ play1.getY() + "," + play2.getX() + "," + play2.getY();
			              MyServer.SendAll(s, "hello");


		}
				
				public synchronized static void checkcollied(Entity p1, Entity p2) {
					if(p1.isCollid(p2)) {
					 gameclear=true;
				     p1.setPosition(-100,900);
				     p2.setPosition(900,900);
				     threadtime.stop();
				     checkending(1);
					}

					
				}
				
				public synchronized static void checkending(int c) {
					if(c==1) {
					  String s = "1,1,1,1";
						SendAll( s, "p1 win");
						//myClientProcThread[1].stop();
					  	//myClientProcThread[2].stop();
					  	System.exit(0);
						
					}else if(c==2){
						String s = "2,2,2,2";
					    SendAll(s, "p2 win");
					    //myClientProcThread[1].stop();
					  	//myClientProcThread[2].stop();
					  	System.exit(0);
					}
					
					
				}
				
		  public static boolean crunning() {
			  return running;
		  }
		



		}
	
	
	
	
	
	
}