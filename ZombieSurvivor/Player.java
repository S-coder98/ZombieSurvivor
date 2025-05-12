import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.HashMap;


public class Player {

	private JPanel panel;
	private int x;
	private int y;
	private int playerWidth;
	private int playerHeight;

	private int dx;
	private int dy;

	private boolean facingRight;

	private int maxHealth = 100;
	private int currentHealth;
	public boolean isDead =false;

	private SoundManager soundManager;

	private enum State{IDLE, WALK, ATTACK, DEAD, HURT}
	private State currentState = State.IDLE;

	private HashMap<State, StripAnimation> animations = new HashMap<>();

	private ArrayList<Bullet> bullets = new ArrayList<>();

	public Player (JPanel p, int xPos, int yPos) {
		panel = p;
		x = xPos;
		y = yPos;

		dx = 5;					// set to zero since background moves instead
		dy = 5;				// size of vertical movement

		animations.put(State.IDLE, new StripAnimation("player/Idle.png", 7, true));
		animations.put(State.WALK, new StripAnimation("player/Walk.png", 8, true));
		animations.put(State.ATTACK, new StripAnimation("player/Attack.png", 5, false));
		animations.put(State.DEAD, new StripAnimation("player/Dead.png", 5, false));
		animations.put(State.HURT, new StripAnimation("player/Hurt.png", 4, false));

		facingRight=true;

		currentHealth = maxHealth;

		soundManager = SoundManager.getInstance();
	}


	public void draw (Graphics2D g2) {
		Image currentFrame = animations.get(currentState).getCurrentFrame();
		BufferedImage frameImage = ImageManager.toBufferedImage(currentFrame);

		if(currentHealth <= 30){
			RedTintFX tint = new RedTintFX(frameImage);
			frameImage = tint.applyRedTint(frameImage);
		}
		if (facingRight) {
			g2.drawImage(frameImage, x, y, getWidth(), getHeight(), null);
		} else {
			g2.drawImage(frameImage, x + getWidth(), y, -getWidth(), getHeight(), null);
		}
	}

	public void update(){
		animations.get(currentState).update();
	}

	public void shoot(){
		setAttacking();
		soundManager.playClip ("gunshot", false);
		Bullet bullet = new Bullet(this);
		bullet.start();
		bullets.add(bullet);

	}

	public ArrayList<Bullet> getBullets(){
		return bullets;
	}

	public void updateBullets(){
		for (Bullet b : bullets){
			b.update();
		}
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
        }
    }

    public void setAttacking() {
        if (currentState != State.ATTACK) {
            currentState = State.ATTACK;
			animations.get(currentState).start();
        }
    }

	public void setDead() {
        if (currentState != State.DEAD) {
            currentState = State.DEAD;
			isDead=true;
			animations.get(currentState).start();
        }
    }
	
	public void setHurt() {
        if (currentState != State.HURT) {
            currentState = State.HURT;
			animations.get(currentState).start();
        }
    }


	public void move (int direction) {

		if (!panel.isVisible ()) return;
        
		playerWidth = animations.get(currentState).getWidth();
    	playerHeight = animations.get(currentState).getHeight();
		
		
			if (direction == 1) {
				facingRight=false;
				setWalking();

					x = x - dx;			// move left
					if(x < 0){
						x = 0;
					}
			
				
			}
			else 
			if (direction == 2) {
				facingRight=true;
				setWalking();
				
					x = x + dx;			// move right
					if(x > panel.getWidth() - playerWidth){
						x = panel.getWidth() - playerWidth;
					}
				
				
			}
			else
			if (direction == 3){ //move up
				setWalking();
		
					y = y - dy;
					if (y < (panel.getHeight()/3)){
						y = panel.getHeight()/3;
					}
				
				
			}
			else
			if (direction == 4){ //move down
				setWalking();
				
					y = y + dy;
					if (y > (panel.getHeight()- playerHeight)){
						y = panel.getHeight() - playerHeight;
					}
				
				
			}

		
		
	}





	public boolean isFacingRight() {
        return facingRight;
    }


	public boolean isOnPlayer (int x, int y) {
		Rectangle2D.Double myRectangle = getBoundingRectangle();
		return myRectangle.contains(x, y);
	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, playerWidth, playerHeight);
	}

	public int getX() {
		return x;
	}
  
	 public int getY() {
		return y;
	}

	public int getWidth() {
		return playerWidth;
	}
  
	 public int getHeight() {
		return playerHeight;
	}

	public int getHealth(){
		return currentHealth;
	}

	public int getMaxHealth(){
		return maxHealth;
	}

	public void addHealth(int amount){
		if(!isDead()){
			currentHealth += amount;
			if (currentHealth>=100) {
				currentHealth = 100;  
			}
		}
	}

	public void takeDamage(int amount){
		if(currentState == State.DEAD) return;

		currentHealth -= amount;
		setHurt();

		if (currentHealth < 0){
			currentHealth = 0;
			setDead();
		}
	}

	public boolean isDead(){
		return isDead;
	}

}