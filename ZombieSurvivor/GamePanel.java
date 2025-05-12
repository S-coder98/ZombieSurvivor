import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
   A component that displays all the game entities
*/

public class GamePanel extends JPanel
		       implements Runnable {
   
	

	private SoundManager soundManager;

	private Player player;

	private HealthPack healthPack;
	private long lastSpawnTimeH;
	private long spawnIntervalH;

	private Powerup powerup;
	private long lastSpawnTimeP;
	private long spawnIntervalP;

	private GrayScaleFX gray;

	private ArrayList<Zombie> zombies; 
	private ArrayList<Zombie2> zombies2; 
	private ArrayList<Zombie3> zombies3; 
	private long lastSpawnTime;
	private long spawnInterval;

	private boolean playedLevelSound = false;
	private boolean gameOverSoundPlayed = false;
	private boolean winSoundPlayed = false;

	private Random rand = new Random();

	private ArrayList<Bullet> bullets; 

	private boolean isRunning;
	private boolean isPaused;
	private boolean gameOver;
	private boolean gameWon;

	private Thread gameThread;

	private int currentLevel;
	private int zombiesToSpawn;
	private int zombieCount;

	private boolean showLevelText = true;

	private long levelStartTime = System.currentTimeMillis();
	private long LEVEL_DELAY = 6000; //6 secs

	private BufferedImage image;

	private Background background;

	private boolean leftPressed = false;
	private boolean rightPressed = false;

	private int score;

	public GamePanel () {
		player = null;
		zombies = new ArrayList<>();
		zombies2 = new ArrayList<>();
		zombies3 = new ArrayList<>();

		healthPack = null;
		powerup = null;

		score = 0;

		currentLevel = 1;
		zombiesToSpawn = 10;
		zombieCount = 0;
		showLevelText = true;

		isRunning = false;
		isPaused = false;
		gameOver = false;

		lastSpawnTime = System.currentTimeMillis();
		spawnInterval = 1000 + rand.nextInt(1000);

		lastSpawnTimeH = System.currentTimeMillis();
		spawnIntervalH = 8000 + rand.nextInt(10000);

		lastSpawnTimeP = System.currentTimeMillis();
		spawnIntervalP = 8000 + rand.nextInt(10000);

		soundManager = SoundManager.getInstance();

		image = new BufferedImage (800, 600, BufferedImage.TYPE_INT_RGB);
	}


	public void createGameEntities() {

		background = new Background(this, "images/deadforest.png", 10);
		player = new Player (this, 400, 300);	
	}

	public void run () {
		try {
			isRunning = true;
			while (isRunning) {
				if (!isPaused)
					gameUpdate();
				gameRender();
				Thread.sleep (50);	
			}
		}
		catch(InterruptedException e) {}
	}


	public void gameUpdate() {
		if (showLevelText && System.currentTimeMillis() - levelStartTime < LEVEL_DELAY) {
			return;
		} else if (showLevelText) {
			showLevelText = false;
			playedLevelSound = false;
		}

			if(player.getHealth() <= 0 && !gameOver){
				player.setDead();

				gameOver = true;

				new java.util.Timer().schedule(new java.util.TimerTask() {
					public void run() {
						endGame(); // stop thread after text is displayed
					}
				}, 1500); //small delay to allow game over text to display
				
			}

			
			

			/* Randomly spawns zombies at different times and keeps a count of how many
			* zombiecount is important for level management
			*/
			long currentTime = System.currentTimeMillis();

			if (currentTime - lastSpawnTime >= spawnInterval && zombieCount < zombiesToSpawn){
				zombies.add(new Zombie(this, player));
				if(currentLevel >= 2){
					zombies2.add(new Zombie2(this, player));
				}
				if (currentLevel >=3){
					zombies3.add(new Zombie3(this, player));
				}
				
				soundManager.playClip ("zombieintro", false);
				lastSpawnTime = currentTime;
				spawnInterval = 1000 + rand.nextInt(1000);
				zombieCount++;
			}

			/*Spawn new health pack at random intervals */
			if(System.currentTimeMillis()-lastSpawnTimeH >= spawnIntervalH){
				healthPack = new HealthPack(this);
				lastSpawnTimeH = currentTime;
				spawnIntervalH = 8000 + rand.nextInt(10000);
			}

			/*Spawn new powerup at random intervals */
			if(System.currentTimeMillis()-lastSpawnTimeP >= spawnIntervalP){
				powerup = new Powerup(this);
				lastSpawnTimeP = currentTime;
				spawnIntervalP = 8000 + rand.nextInt(10000);
			}
			

			/* collision check for health packs */
			if(healthPack != null && healthPack.isActive()){
				if(player.getBoundingRectangle().intersects(healthPack.getBoundingRectangle())){
					player.addHealth(10);
					soundManager.playClip ("healthpack", false);
					healthPack.collect();
				}
			}

			/* collision check for powerups */
			if(powerup != null && powerup.isActive()){
				if(player.getBoundingRectangle().intersects(powerup.getBoundingRectangle())){
					score+=10;
					soundManager.playClip ("healthpack", false);
					powerup.collect();
				}
			}

			/*remove when collected */
			if (healthPack != null && !healthPack.isActive()) {
				healthPack = null;
			}			

			if (powerup != null && !powerup.isActive()) {
				powerup = null;
			}

			for (Zombie z: zombies){
				if(!z.isDead()){
					z.update();
					z.move();
				} else {
					z.update();
				}
			}
			for (Zombie2 z2: zombies2){
				if(!z2.isDead()){
					z2.update();
					z2.move();
				} else {
					z2.update();
				}
			}
			for (Zombie3 z3: zombies3){
				if(!z3.isDead()){
					z3.update();
					z3.move();
				} else {
					z3.update();
				}
			}

			/* Bullet logic to handle collision detection and scoring */
			bullets = player.getBullets();
			Iterator<Bullet> bulletIterator = bullets.iterator();
			while (bulletIterator.hasNext()) {
				Bullet b = bulletIterator.next();
				if (!b.isActive()) continue;

				b.update();

				for (Zombie z : zombies) {
					if (b.collidesWith(z) && b.isActive() && !z.isDead()) {
						soundManager.playClip ("zombieshot", false);
						z.setDead();
						score += 10;
						b.setActive(false);
						break;
					}
				}

				for (Zombie2 z : zombies2) {
					if (b.collidesWith2(z) && b.isActive() && !z.isDead()) {
						soundManager.playClip ("zombieshot", false);
						z.setDead();
						score += 20;
						b.setActive(false);
						break;
					}
				}
				for (Zombie3 z : zombies3) {
					if (b.collidesWith3(z) && b.isActive() && !z.isDead()) {
						soundManager.playClip ("zombieshot", false);
						z.setDead();
						score += 30;
						b.setActive(false);
						break;
					}
				}
			}

			bullets.removeIf(b -> !b.isActive());
			zombies.removeIf(z -> z.isDead() && (z.animationActive()==false));
			zombies2.removeIf(z -> z.isDead() && (z.animationActive()==false));
			zombies3.removeIf(z -> z.isDead() && (z.animationActive()==false));
			
			/*Level Management Logic 
			* Game auto starts at level 1, so we only need to do transfer from 1 to 2 here
			*/
			if(currentLevel == 1 && zombieCount == zombiesToSpawn && zombies.isEmpty() && zombies2.isEmpty() && zombies3.isEmpty()){
				currentLevel ++;
				zombiesToSpawn = 20;
				zombieCount = 0;
				showLevelText = true;
				levelStartTime = System.currentTimeMillis();
			}

			/*Level 3 */
			if(currentLevel == 2 && zombieCount==zombiesToSpawn && zombies.isEmpty()&& zombies2.isEmpty() && zombies3.isEmpty()){
				currentLevel++;
				showLevelText = true;
				levelStartTime = System.currentTimeMillis();
				zombiesToSpawn = 50;
				zombieCount = 0;
			}

			/*player won */
			if(currentLevel == 3 && zombieCount==zombiesToSpawn && zombies.isEmpty()&& zombies2.isEmpty() && zombies3.isEmpty()){
				gameWon = true;
				new java.util.Timer().schedule(new java.util.TimerTask() {
					public void run() {
						endGame(); // stop game thread after showing "YOU WIN"
					}
				}, 3000); // show "YOU WIN" for 3 seconds
			
			}
		
	}


	public void updatePlayer (int direction) {

		if (isPaused)
			return;

		if (background != null) {
			background.move(direction);
		}

		if (player != null){
			player.setIdle();
			if (leftPressed||rightPressed){
				player.move(direction);
			}else{
				player.setIdle();
			}
		}

		player.update();
		player.updateBullets();
		
	}

	public void playerMove(int direction) {
		player.move(direction);
	}

	public void playerShoot() {
		player.shoot();
	}

	private void drawPlayerHealthBar(Graphics2D g2) {
		int barWidth = 200;
		int barHeight = 20;
		int x = 20;
		int y = 20;
	
		double healthPercent = (double) player.getHealth() / player.getMaxHealth();
		int filledWidth = (int) (barWidth * healthPercent);
	
		// Draw background
		g2.setColor(Color.GRAY);
		g2.fillRect(x, y, barWidth, barHeight);
	
		// Draw current health
		g2.setColor(Color.GREEN);
		g2.fillRect(x, y, filledWidth, barHeight);
	
		// Draw border
		g2.setColor(Color.BLACK);
		g2.drawRect(x, y, barWidth, barHeight);
	}

	private void drawScore(Graphics2D g2){
		g2.setFont(new Font("Arial", Font.BOLD, 30));
		g2.setColor(Color.GREEN);
		g2.drawString("Score: " + score, 600, 40);
	}
	


	public void gameRender() {

		// draw the game objects on the image

		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		background.draw(imageContext);

		if (player != null) {
			player.draw(imageContext);
			drawPlayerHealthBar(imageContext);
			drawScore(imageContext);
		}

		if(healthPack!=null){
			healthPack.draw(imageContext);
		}

		if(powerup!=null){
			powerup.draw(imageContext);
		}

		if (zombies != null){
			for (Zombie z: zombies){
				z.draw(imageContext);
			}
		}

		if (zombies2 != null){
			for (Zombie2 z: zombies2){
				z.draw(imageContext);
			}
		}

		if (zombies3 != null){
			for (Zombie3 z: zombies3){
				z.draw(imageContext);
			}
		}
/* 
		if (tombstones != null) {
			for (Tombstone t: tombstones){
				t.draw(imageContext, player.getX());
			}
		}
*/
		if (bullets != null){
			for (Bullet b: bullets){
				b.draw(imageContext);
			}
		}

		Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
		
		g2.drawImage(image, 0, 0, 800, 600, null);

		if(gameOver){
			gray = new GrayScaleFX(image, 0, 0);
			gray.draw(g2);

			Font f = new Font("Arial", Font.BOLD, 80);
			g2.setFont(f);
			g2.setColor(Color.RED);
			g2.drawString("GAME OVER.", 150, 300 );
			if (!gameOverSoundPlayed) {
				soundManager.playClip("gameover", false);
				gameOverSoundPlayed = true;
			}
		}

		if (showLevelText) {
			g2.setColor(Color.GRAY);
			g2.setFont(new Font("Arial", Font.BOLD, 80));
			String text = "Level " + currentLevel;
			g2.drawString(text, 280, 300);
			if (!playedLevelSound) {
				soundManager.playClip("countdown", false);
				playedLevelSound = true;
			}
		}

		if(gameWon){
			g2.setColor(Color.GRAY);
			g2.setFont(new Font("Arial", Font.BOLD, 80));
			String text = "YOU WON";
			g2.drawString(text, 280, 300);

			if(!winSoundPlayed){
				soundManager.playClip ("winmusic", false);
				soundManager.playClip ("youwin", false);
				winSoundPlayed = true;
			}
		}

		imageContext.dispose();
		g2.dispose();
	}


	public void startGame() {				// initialise and start the game thread 
		if (isRunning)
			return;

		isRunning = true;
		isPaused = false;
		gameOverSoundPlayed = false;
		winSoundPlayed = false;
		playedLevelSound = false;
		currentLevel = 1;
		zombieCount = 0;
		gameOver = false;
		gameWon = false;
		showLevelText = true;
		levelStartTime = System.currentTimeMillis();
		score=0;
		zombies.clear();


		if (gameThread == null) {
			soundManager.playClip ("backgroundmusic", true);
			createGameEntities();
			gameThread = new Thread (this);			
			gameThread.start();

			if (player != null) {
				player.setIdle();
				updatePlayer(1);
			}
		}

	}

	public void pauseGame() {				// pause the game (don't update game entities)
		if (isRunning) {
			if (isPaused)
				isPaused = false;
			else
				isPaused = true;
		}
	}


	public void endGame() {					// end the game thread
		isRunning = false;
		soundManager.stopClip ("backgroundmusic");
		gameThread = null;
	}


	public boolean isOnPlayer (int x, int y) {
		return player.isOnPlayer(x, y);
	}

	public void setLeftPressed(boolean pressed){
		leftPressed = pressed;
	}

	public void setRightPressed(boolean pressed){
		rightPressed = pressed;
	}
}