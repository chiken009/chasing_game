package a;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;


import a.MyServer;
import a.MyServer.Game; 


class ClientProcThread extends Thread   {
	private int number;
	private Socket incoming;
	private InputStreamReader myIsr;
	private BufferedReader myIn;
	private PrintWriter myOut;
	private String myName;
	
	public static int width = 800;
    public static int height = 800;

    private boolean running;
    PrintWriter out;    
	private Integer no; 
	private int pno;
    private Game game;
    Entity play, play2;
    
    
    
    
	public ClientProcThread(int n, Socket i, InputStreamReader isr, BufferedReader in, PrintWriter out, Game g, Entity p, Entity p2) {
		number = n;
		incoming = i;
		myIsr = isr;
		myIn = in;
		myOut = out;
		pno = number+2;
		game = g;
		play = p;
		play2 = p2;
		if(number==1) {
		 play.setPosition(400,700);
		 play2.setPosition(400,100);
		}else {
		 play.setPosition(400,100);
		 play2.setPosition(400,700);
			
		}
	} 

	
	public void run() {
		try {
			   
					   
			   no = Integer.valueOf(pno);
			   System.out.println(no);
			   myOut.println(no);
			   myOut.flush();
			
			

			while (true) {
				
				int k = Integer.parseInt(myIn.readLine());
	        	
	        	  running = game.crunning();
	        	if (running) {
	        		
	                	
	        		   
	        		game.checkkey(number, k, play);
	        		game.checkcollied(play, play2);
	        		 System.out.println(play.getX()+ ", " + play.getY()+" from client"+ number );
	        	}
	        	
	        	if(game.timeup==true || game.gameclear== true) {
	        		break;
	        	}
	        	
	        	
	        	
	        	
		  }
		} catch (Exception e) {

			System.out.println("Disconnect from client No."+number+"("+myName+")");
			MyServer.SetFlag(number, false);
		}
	}
}