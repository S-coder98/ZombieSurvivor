import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class Bullet {
    private int x, y;
    private int width = 10; 
    private int height = 5;
    private int speed = 10; 
    private boolean isActive; // To check if the bullet is still active
    private boolean facingRight; 
    private Player player; 

    public Bullet(Player player) {
        this.player = player;
        this.isActive = false; // Start with the bullet inactive
        this.facingRight = player.isFacingRight();
    }

    // Start the bullet from the player's current position
    public void start() {
        if(facingRight==true){
            x=player.getX() + player.getWidth() - 45;
        }else{
            x = player.getX() + 45;
        }
        y = player.getY() + 85;
        isActive = true;
    }

    // Update the bullet's position
    public void update() {
        if (!isActive) return;

        if (facingRight) {
            x += speed; // Move to the right
        } else {
            x -= speed; // Move to the left
        }

        // Deactivate the bullet if it goes off-screen
        if (x < 0 || x > 1000) { 
            isActive = false;
        }
    }

    public void draw(Graphics2D g2) {
   
        g2.setColor(Color.YELLOW);
        g2.fillRect(x, y, width, height);
        //System.out.println("Drawing bullet at: " + x + ", " + y); 
        
    }

    public boolean collidesWith(Zombie zombie) {
        return isActive && getBoundingRectangle().intersects(zombie.getBoundingRectangle());
    }
    public boolean collidesWith2(Zombie2 zombie2) {
        return isActive && getBoundingRectangle().intersects(zombie2.getBoundingRectangle());
    }
    public boolean collidesWith3(Zombie3 zombie3) {
        return isActive && getBoundingRectangle().intersects(zombie3.getBoundingRectangle());
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean a) {
        isActive = a;
    }
}
