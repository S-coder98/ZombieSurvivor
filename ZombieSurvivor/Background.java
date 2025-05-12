import java.awt.Graphics2D;
import java.awt.Image;

public class Background {
  	private Image bgImage;
  	private int bgImageWidth;      		// width of the background (>= panel Width)

	private GamePanel panel;

 	private int bgX;			// X-coordinate of "actual" position
	private int bgDX;			// size of the background move (in pixels)


  public Background(GamePanel panel, String imageFile, int bgDX) {
    
	this.panel = panel;
    	this.bgImage = ImageManager.loadImage(imageFile);
    	bgImageWidth = bgImage.getWidth(null);	// get width of the background

	//System.out.println ("bgImageWidth = " + bgImageWidth);

	if (bgImageWidth < panel.getWidth())
      		System.out.println("Background width < panel width");

    	this.bgDX = bgDX; // bgDX;

	bgX = 0;

  }


  public void move (int direction) {

	if (direction == 1)
		moveRight();
	else
	if (direction == 2)
		moveLeft();
  }


  public void moveLeft() {

	bgX = bgX - bgDX;
	
	if (bgX < (panel.getWidth()-bgImageWidth)) {
		bgX=panel.getWidth()-bgImageWidth;
	}

  }


  public void moveRight() {

	bgX = bgX + bgDX;
	
	if (bgX > 0) {
		bgX = 0;
	}

   }
 

  public void draw (Graphics2D g2) {
	g2.drawImage(bgImage, bgX, 0, 3840, 600, null);
  }

  public int getBGX(){
	return bgX;
  }

}
