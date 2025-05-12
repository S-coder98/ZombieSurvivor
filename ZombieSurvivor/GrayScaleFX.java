import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GrayScaleFX implements ImageFX {

	private static final int WIDTH = 800;		// width of the image
	private static final int HEIGHT = 600;		// height of the image

	private int x;
	private int y;
    GamePanel panel;
	private BufferedImage originalImage;		// image for sprite effect
	private BufferedImage grayImage;		// copy of image

	public GrayScaleFX (BufferedImage image, int x, int y) {
		this.originalImage = image;
		this.x = x;
		this.y = y;
		copyToGray();
	}


	private int toGray (int pixel) {

  		int alpha, red, green, blue, gray;
		int newPixel;

		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;

		// Calculate the value for gray

		gray = (red + green + blue) / 3;

		// Set red, green, and blue channels to gray

		red = green = blue = gray;

		newPixel = blue | (green << 8) | (red << 16) | (alpha << 24);

		return newPixel;
	}


	private void copyToGray() {
		int imWidth = originalImage.getWidth();
		int imHeight = originalImage.getHeight();

    		int [] pixels = new int[imWidth * imHeight];
    		originalImage.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
			pixels[i] = toGray(pixels[i]);
		}
  
    		originalImage.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
	}	


	public void draw (Graphics2D g2) {
        g2.drawImage(originalImage, x, y, WIDTH, HEIGHT, null);
		
	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, WIDTH, HEIGHT);
	}


	public void update() {				// modify time and change the effect if necessary

	}

}