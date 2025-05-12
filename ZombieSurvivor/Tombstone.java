/*Originakky planned to use as a solid object that the player had
 * to navigate around.
 * However, they do not behave so they were eliminated :(
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Tombstone {
    private int x, y;
    private int size = 40;
    private int width, height;
    private GamePanel panel;
    private Image image;

    public Tombstone(GamePanel panel, int x, int y, int width, int height) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        image = ImageManager.loadImage("images/tombstone.png");
    }

    public void draw(Graphics2D g2, int playerX) {
        g2.drawImage(image, x-playerX, y, width, height, null);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean collidesWith(Rectangle rect){
        return getBoundingRectangle().intersects(rect);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getSize(){
        return size;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
