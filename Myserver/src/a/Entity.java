/**
 * 
 */
package a;

/**
 * @author kenza
 *
 */
import java.awt.Graphics2D;
import java.awt.Rectangle;



public class Entity {

    private int x;
    private int y;
    private int size;

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

	


}