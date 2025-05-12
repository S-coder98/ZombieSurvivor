import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class RedTintFX implements ImageFX {

	private static final int WIDTH = 120;
	private static final int HEIGHT = 120;

	private int x;
	private int y;

	private BufferedImage spriteImage;
	private BufferedImage tintedImage;

	public RedTintFX(BufferedImage originalImage) {
		this.spriteImage = originalImage;
		this.tintedImage = applyRedTint(originalImage);
	}

	public BufferedImage applyRedTint(BufferedImage src) {
		BufferedImage tinted = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < src.getHeight(); y++) {
			for (int x = 0; x < src.getWidth(); x++) {
				int pixel = src.getRGB(x, y);
				tinted.setRGB(x, y, tintPixelRed(pixel));
			}
		}

		return tinted;
	}

	private int tintPixelRed(int pixel) {
		int alpha = (pixel >> 24) & 255;
		int red = (pixel >> 16) & 255;
		int green = (pixel >> 8) & 255;
		int blue = pixel & 255;

		// Increase red, reduce green and blue to make it look reddish
		red = truncate(red + 50);
		green = truncate(green - 30);
		blue = truncate(blue - 30);

		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	private int truncate(int value) {
		if (value < 0) return 0;
		if (value > 255) return 255;
		return value;
	}

	public void draw(Graphics2D g2) {
		g2.drawImage(tintedImage, x, y, WIDTH, HEIGHT, null);
	}

	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
	}

	public void update() {
		// No dynamic update needed for fixed tint effect
	}
}
