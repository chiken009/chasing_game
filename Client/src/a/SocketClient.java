package a;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.applet.AudioClip;
import java.awt.*;
import java.net.*;
import java.lang.*;
import java.awt.event.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;




public class SocketClient extends JFrame implements KeyListener  {
	public static String str;
	public static int width = 800;
	public static int height =800;
	private Thread thread;
	private boolean running,gameclear=false,timeup=false,stoprapidrun=false;
	private long targetTime;
	private Graphics2D g2d;
	private BufferedImage image;
	private BufferedReader in;
	private  PrintWriter out;
	private Entity play1,play2,play;
	private int size = 20;
	private int x1,y1,x2,y2,px1=400,py1=100,px2=400,py2=700;
	private boolean up,right,left,down,w,a,s,d;
	private int err = 0;
	public static Clip clip;
	private Integer data;
	private int  picl = 3, picr=6, picu=9, picd=0;
	private int  picl1 =3, picr1=6, picu1=9, picd1=0;
	private int nkey=9, nkey1=0;
	
	private JFrame frame1;
	private JPanel panel1; 
	private JButton back;
	private Thread t1;
	
	public void stopped() {
		t1.stop();
		clip.stop();
	}

	public void playSound() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(".\\sound\\song3.wav").getAbsoluteFile());

			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.loop(5);
		} catch(Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}


	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {

		int k = e.getKeyCode();
		data = Integer.valueOf(k);
		if (err == 4) {
			if (k == KeyEvent.VK_LEFT & stoprapidrun == false) {
				stoprapidrun = true;
//				++picl1;
//				if (picl1 == 6) {
//					picl1 = 3;
//				}
//				nkey1 = picl1;


				out.println(data);
				out.flush();

			}

			if (k == KeyEvent.VK_RIGHT & stoprapidrun == false) {
				stoprapidrun = true;
//				++picr1;
//				if (picr1 == 9) {
//					picr1 = 6;
//				}
//				nkey1 = picr1;

				out.println(data);
				out.flush();

			}

			if (k == KeyEvent.VK_UP & stoprapidrun == false) {
				stoprapidrun = true;
//				++picu1;
//				if (picu1 == 12) {
//					picu1 = 9;
//				}
//				nkey1 = picu1;

				out.println(data);
				out.flush();

			}

			if (k == KeyEvent.VK_DOWN & stoprapidrun == false) {
				stoprapidrun = true;
//				++picd1;
//				if (picd1 == 3) {
//					picd1 = 0;
//				}
//				nkey1 = picd1;

				out.println(data);
				out.flush();

			}


		} else {


			if (k == KeyEvent.VK_LEFT & stoprapidrun == false) {
				stoprapidrun = true;
				left = true;
//				++picl;
//				if (picl == 6) {
//					picl = 3;
//				}
//				nkey = picl;

				out.println(data);
				out.flush();

			}

			if (k == KeyEvent.VK_RIGHT & stoprapidrun == false) {
				stoprapidrun = true;
//				++picr;
//				if (picr == 9) {
//					picr = 6;
//				}
//				nkey = picr;

				right = true;
				out.println(data);
				out.flush();

			}

			if (k == KeyEvent.VK_UP & stoprapidrun == false) {
				stoprapidrun = true;
//				++picu;
//				if (picu == 12) {
//					picu = 9;
//				}
//				nkey = picu;

				up = true;
				out.println(data);
				out.flush();

			}

			if (k == KeyEvent.VK_DOWN & stoprapidrun == false) {
				stoprapidrun = true;
//				++picd;
//				if (picd == 3) {
//					picd = 0;
//				}
//				nkey = picd;

				down = true;
				out.println(data);
				out.flush();

			}
		}


	}
	public void keyReleased(KeyEvent e) {
		stoprapidrun = false;
	}


	public SocketClient(String str)  {
		this.setPreferredSize(new Dimension(width ,height));
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.pack();


			t1 = new Thread(new Runnable(){
			Socket socket = null;
			public void run(){ try {


				InetSocketAddress endpoint = new InetSocketAddress(str, 8000);
				socket = new Socket();
				socket.connect(endpoint, 3000);
				in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);



			} catch (UnknownHostException e) {
				System.err.println("can't connect to server adrres: " + e);
				Startmenu start = new Startmenu();
				setserver set = new setserver(start, "error with conection please try again");
			    set.setVisible(true);
			    clip.stop();
			    dispose();
				


			} catch (IOException e) {
				System.err.println("error: " + e);
				Startmenu start = new Startmenu();
				setserver set = new setserver(start, "error with conection please try again");
			    set.setVisible(true);
			    clip.stop();
			    dispose();

			}

				System.out.println("Connected to server");
				MesgRecvThread mrt = new MesgRecvThread(socket, in, out);
				mrt.start();
				//while(true);
			}});
		t1.start();
		JOptionPane.showMessageDialog(null, "Loadig.. waiting for another player", "InfoBox: " + "status", JOptionPane.INFORMATION_MESSAGE);
		playSound();


	}




	public class MesgRecvThread extends Thread {

		Socket socket;
		BufferedReader myIn;
		PrintWriter myOut;
		String str[];
        int p[] = new int[4];

		public MesgRecvThread(Socket s, BufferedReader in, PrintWriter out){
			socket = s;
			myIn = in;
			myOut = out;

		}


		public void run() {



			try {
				err = Integer.parseInt(myIn.readLine());
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}





			Thread t = new Thread(new Runnable(){
				public void run(){
					if(running) return;
					setUp();
					long start;
					long els;
					long wait;
					while(running){
						start = System.nanoTime();
						update();
						rRender();
						els = System.nanoTime() - start;
						wait = targetTime - els  / 3000;

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

			try{




				if(err==4) {

					System.out.println("switch");
				}else if(err==0) {
					try {
						JOptionPane.showMessageDialog(null, "can't conect to server ");
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				t.start();
				while(true) {

					if(timeup==true || gameclear== true) {
						break;
					}

					String s = myIn.readLine();

					String stp[] = s.split(",");
					p[0] = Integer.parseInt(stp[0]);
					p[1] = Integer.parseInt(stp[1]);
					p[2] = Integer.parseInt(stp[2]);
					p[3] = Integer.parseInt(stp[3]);



					System.out.println(p[0]+ "," + p[1]+","+ p[2]+ "," +p[3]);
					if (running) {

					
						if (p[2]<play2.getX()){

							left = true;
							++picl;
							if(picl==6) {
								picl=3;
							}
							nkey = picl;

						}

						if (p[2]>play2.getX()){

							++picr;
							if(picr==9) {
								picr=6;
							}
							nkey = picr;

						}

						if (p[3]<play2.getY()){

							++picu;
							if(picu==12) {
								picu=9;
							}
							nkey = picu;

						}

						if (p[3]>play2.getY()){

							++picd;
							if(picd==3) {
								picd=0;
							}
							nkey = picd;

						}


						if (p[0]<play1.getX()){


							++picl1;
							if(picl1==6) {
								picl1=3;
							}
							nkey1 = picl1;
						}

						if (p[0]>play1.getX()){
							++picr1;
							if(picr1==9) {
								picr1=6;
							}
							nkey1 = picr1;

						}

						if (p[1]<play1.getY()){
							++picu1;
							if(picu1==12) {
								picu1=9;
							}
							nkey1 = picu1;

						}

						if (p[1]>play1.getY()){
							++picd1;
							if(picd1==3) {
								picd1=0;
							}
							nkey1 = picd1;


						}
                             play1.setX(p[0]);
						     play1.setY(p[1]);
						     play2.setX(p[2]);
						     play2.setY(p[3]);



						if(p[0]==1 && p[1]==1) {
							gameclear = true;
						}
						if(p[0]==2 && p[1]==2) {
							timeup = true;
						}
					}else {
						socket.close();
						break;

					}

				}

			}catch (IOException e) {
				System.err.println("error happen: " + e);
			}
		}
	}

	private void rRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();

	}

	private void render(Graphics2D g2d2) {
		g2d.clearRect(0,0,width,height);

		play1.dimage(g2d,nkey1,1);


		play2.dimage(g2d,nkey,2);

		if(err==3) {
			g2d.drawString("you are girl run!" , 20, 60);
		}else {
			g2d.drawString("you are dragon catch it" , 20, 60);
		}

		if(gameclear==true){
			g2d.setColor(Color.white);
			running = false;
			if(err==3) {
				g2d.drawString("You Lose!" , 350, 400);
				JOptionPane.showMessageDialog(null, "you Lose!", "InfoBox: " + "status", JOptionPane.INFORMATION_MESSAGE);
			}else {
				g2d.drawString("You are Winner!!!" , 350, 400);
				JOptionPane.showMessageDialog(null, "you are Winner", "InfoBox: " + "status", JOptionPane.INFORMATION_MESSAGE);
			}
			System.exit(0);	
			
			
		}

		if(timeup==true){
			g2d.setColor(Color.white);
			running = false;
			if(err==4) {
				g2d.drawString("You Lose!" , 350, 400);
				JOptionPane.showMessageDialog(null, "you Lose!", "InfoBox: " + "status", JOptionPane.INFORMATION_MESSAGE);
				
			}else {
				g2d.drawString("You are winner!!!" , 350, 400);
				JOptionPane.showMessageDialog(null, "you are Winner", "InfoBox: " + "status", JOptionPane.INFORMATION_MESSAGE);
			}
			System.exit(0);
			
			
		}

	}


	private void setFPS(int x) {
		targetTime = 1000/x;
	}

	private void update() {


		if(gameclear==false && timeup==false){

			if(play1.getX() < 0) play1.setX(0);
			if(play1.getY() < 0) play1.setY(0);
			if(play1.getX() > width) play1.setX(780);
			if(play1.getY() > height) play1.setY(height-40);

			if(play2.getX() < 0) play2.setX(0);
			if(play2.getY() < 0) play2.setY(0);

			if(play2.getX() > width) play2.setX(780);
			if(play2.getY() > height) play2.setY(height-40);
		}

	}





	private void setUp() {
		image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUpLevel();
		setFPS(30);

	}

	private void setUpLevel() {
		play1 = new Entity(size);
		play2 = new Entity(size);
		play1.setPosition(400,100);
		play2.setPosition(400,700);


	}





}