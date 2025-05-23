import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


/**
    The StripAnimation class creates an animation from a strip file.
*/
public class StripAnimation {
	
	Animation animation;

	private int x;		// x position of animation
	private int y;		// y position of animation

	//private int frameCount;		//number of frames in the strip

	private int width;
	private int height;

	private int dx;		// increment to move along x-axis
	private int dy;		// increment to move along y-axis

	public StripAnimation(String imageName, int frameCount, boolean looping) {

		animation = new Animation(looping);	// run animation once

        dx = 0;		// increment to move along x-axis
        dy = -10;	// increment to move along y-axis

		// load images from strip file

		Image stripImage = ImageManager.loadImage("images/" + imageName);

		int imageWidth = (int) stripImage.getWidth(null) / frameCount;
		int imageHeight = stripImage.getHeight(null);

		for (int i=0; i<frameCount; i++) {

			BufferedImage frameImage = new BufferedImage (imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) frameImage.getGraphics();
     
			g.drawImage(stripImage, 
					0, 0, imageWidth, imageHeight,
					i*imageWidth, 0, (i*imageWidth)+imageWidth, imageHeight,
					null);

			animation.addFrame(frameImage, 100);

			width=  imageWidth;
			height = imageHeight;
		}
	
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}


	public void start() {
		animation.start();
	}

	
	public void update() {
		if (!animation.isStillActive())
			return;

		animation.update();
		x = x + dx;
		y = y + dy;
	}


	public void draw(Graphics2D g2, int x, int y, boolean facingRight) {

		if (animation.getImage()==null)
			return;

		if (facingRight){
			g2.drawImage(animation.getImage(), x, y, width, height, null);
		}else {
			g2.drawImage(animation.getImage(), x+width, y, -width, height, null);
		}
		
	}

	public void reset(){
		animation.reset();
	}

    public boolean isStillActive() {
        return animation.isStillActive();
    }

	public Image getCurrentFrame(){
		return animation.getImage();
	}
}
