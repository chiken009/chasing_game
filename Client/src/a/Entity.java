package a;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;
import a.character;

public class Entity {
	 BufferedImage pic = null;
	Image back;
    private int x, z;
    private int y;
    private int size;
    character chara;
    
    
    public Entity(int x){
        this.size = x;
        
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;

    }

    public Rectangle getBound(){
        return new Rectangle(x ,y,size, size);
    }

    public boolean isCollid(Entity e){
        if (e == this) return false;
        return getBound().intersects(e.getBound());
    }

    public void render(Graphics2D g2d){
    	
    	
    	g2d.fillRect(x, y, size, size);
        
    }

	    public void dimage(Graphics2D g2d, int num, int c) {
	    	
	    character c1 = new character();
		AffineTransform a = new AffineTransform();//

		a.scale(1,1) ;//scale()

		g2d.setTransform(a);
		
		if(c==1) {
			g2d.drawImage(character.Pic[num], x, y, null);
		}else {
			g2d.drawImage(character.Pic1[num], x, y, null);
		}
	}


}




