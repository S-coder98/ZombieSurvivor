import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class HealthPack {
    private int x, y;
    private int size = 30;
    private boolean active = true;
    private GamePanel panel;
    private Image image;

    public HealthPack(GamePanel panel) {
        this.panel = panel;
        Random rand = new Random();
        this.x = rand.nextInt(3800 - size); //full length of background
        this.y = panel.getHeight()/2 + rand.nextInt(panel.getHeight()/2 - size); // avoid bottom HUD, playable area only

        image = ImageManager.loadImage("images/health.png");
    }

    public void draw(Graphics2D g2) {
        if (active) {
            g2.drawImage(image, x, y, size, size, null);
        }
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, size, size);
    }

    public boolean isActive() {
        return active;
    }

    public void collect() {
        active = false;
    }
}
