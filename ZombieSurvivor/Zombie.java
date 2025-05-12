import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.util.HashMap;
import java.util.Random;

public class Zombie {

   private JPanel panel;

   private int x;
   private int y;

   private int width;
   private int height;

   private enum State{IDLE, WALK, ATTACK, DEAD}
	private State currentState = State.IDLE;

   private long lastAttackTime=0;
   private static final long ATTACK_COOLDOWN = 1000;

	private HashMap<State, StripAnimation> animations = new HashMap<>();

   private int dx;		// increment to move along x-axis
   private int dy;		// increment to move along y-axis

   private Random random;

   private int speed;

   private Player player;
   private SoundManager soundManager;

   private boolean facingRight;

   public Zombie (JPanel p, Player player) {
      panel = p;

      random = new Random();

      setLocation();

      dx = 1;	
      dy = 1;		
      
      speed = 2;


      this.player = player;
      soundManager = SoundManager.getInstance();
      
      animations.put(State.IDLE, new StripAnimation("zombie/Idle.png", 9, true));
		animations.put(State.WALK, new StripAnimation("zombie/Walk.png", 10, true));
		animations.put(State.ATTACK, new StripAnimation("zombie/Attack_1.png", 4, true));
		animations.put(State.DEAD, new StripAnimation("zombie/Dead.png", 5, false));

      facingRight = false;

      updateSize();
   }

   public void updateSize(){
      width = animations.get(currentState).getWidth();
      height = animations.get(currentState).getHeight();
   }

   /*Randomize where the zombies spawn (not including top) */
   public void setLocation() {
      int side = random.nextInt(3); // 0 = left, 1 = right, 2 = bottom
      int panelWidth = panel.getWidth();
      int panelHeight = panel.getHeight()/2;

      if (side == 0) { // left
         x = -width;
         y = panelHeight + random.nextInt(panelHeight - height);
      } else if (side == 1) { // right
         x = panelWidth;
         y = panelHeight + random.nextInt(panelHeight - height);
      } else { // bottom
         x = random.nextInt(panelWidth - width);
         y = panelHeight;
      }
   }

   public void draw (Graphics2D g2) {
		animations.get(currentState).draw(g2, x, y, facingRight);
	}

	public void update(){
		animations.get(currentState).update();
	}

   public boolean animationActive() {
      boolean isFinished = animations.get(currentState).isStillActive(); 
      return isFinished;
  }

	public void setIdle() {
        if (currentState != State.IDLE) {
            currentState = State.IDLE;
			   animations.get(currentState).start();
        }
   }

	public void setWalking() {
        if (currentState != State.WALK) {
            currentState = State.WALK;
			   animations.get(currentState).start();
            updateSize();
        }
   }

   public void setAttacking() {
        if (currentState != State.ATTACK) {
            currentState = State.ATTACK;
			   animations.get(currentState).start();
            updateSize();
        }
   }

	public void setDead() {
        if (currentState != State.DEAD) {
            currentState = State.DEAD;
			   animations.get(currentState).start();
        }
   }

   public boolean isDead(){
      return currentState == State.DEAD;
   }


   public void move() {

      if (!panel.isVisible ()) return;

      if(player.isDead == false && isCloseToPlayer(player,40)){
         if (System.currentTimeMillis() - lastAttackTime >= ATTACK_COOLDOWN){
            setAttacking();
            soundManager.playClip ("playerhurt", false);
            player.takeDamage(1);
            lastAttackTime = System.currentTimeMillis();
         }
         
         
      }else{
         setWalking();

         // Move toward player
         int playerX = player.getX();
         int playerY = player.getY();

         // Calculate the difference in positions
         int deltaX = playerX - x;
         int deltaY = playerY - y;

         // Move in the direction of the player with a lower speed (slower than the player)
         if (deltaX != 0) {
            dx = (deltaX > 0) ? speed : -speed;
            facingRight = deltaX > 0;
         }
         if (deltaY != 0) {
            dy = (deltaY > 0) ? speed : -speed;
         }

         x = x + dx;
         y = y + dy;

      }

   }


   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, width, height);
   }

   /*this method not working as accurate as we would like for collision detection
   the collission works but the attach starts from far away visually
   keep method in case we need it for other behavior
   */
   public boolean collidesWithPlayer(Player player){
      return getBoundingRectangle().intersects(player.getBoundingRectangle()); 
   }

   /*method for collision detection so the attack visually works
   calculate the centre of the objects for attack range
   */
   public boolean isCloseToPlayer(Player player, int attackRange) {
      int playerCenterX = player.getX() + player.getWidth() / 2;
      int playerCenterY = player.getY() + player.getHeight() / 2;
  
      int zombieCenterX = x + width / 2;
      int zombieCenterY = y + height / 2;
  
      double distance = Math.sqrt(Math.pow(playerCenterX - zombieCenterX, 2) + Math.pow(playerCenterY - zombieCenterY, 2));
  
      return distance <= attackRange;
  }

}