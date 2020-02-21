package a;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class character {
	private BufferedImage readImage[] = new BufferedImage[2];
	static BufferedImage picture = null;
	static BufferedImage[] Pic = new BufferedImage[12];
	static BufferedImage[] Pic1 = new BufferedImage[12];
	private int i,j;
	private int n=0;
	
	
	public character() {
		
		try {
			readImage[0] = ImageIO.read(new File(".//image//girl.png"));
			readImage[1] = ImageIO.read(new File(".//image//dragon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(i=0; i<4; i++) {
			
			for(j=0; j<3; j++) {
				Pic[n] = readImage[0].getSubimage(j*50, i*50, 50, 50);
				Pic1[n] = readImage[1].getSubimage(j*50, i*50, 50, 50);
				n++;
			}
		}
		
		
			
			
			
		
		
		
	}
	
	
		
		
		
		
		
		
		
	
	
	
	
	
}
