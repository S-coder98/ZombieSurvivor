import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;

/**
   The ImageManager class manages the loading and processing of images.
*/

public class ImageManager {
    private static Map<String, BufferedImage> imageCache = new HashMap<>();

   	public ImageManager () {

	}
	
	public static BufferedImage loadBossImage(String path) {
        if (!imageCache.containsKey(path)) {
            imageCache.put(path, loadBufferedImage(path));
        }
        return imageCache.get(path);
    }

	public static Image loadImage (String fileName) {
		return new ImageIcon(fileName).getImage();
	}

	public static BufferedImage loadBufferedImage(String filename) {
		BufferedImage bi = null;

		File file = new File (filename);
		try {
			bi = ImageIO.read(file);
		}
		catch (IOException ioe) {
			System.out.println ("Error opening file " + filename + ":" + ioe);
		}
		return bi;
	}


  	// make a copy of the BufferedImage src

	public static BufferedImage copyImage(BufferedImage src) {
		if (src == null)
			return null;


		int imWidth = src.getWidth();
		int imHeight = src.getHeight();

		BufferedImage copy = new BufferedImage (imWidth, imHeight,
							BufferedImage.TYPE_INT_ARGB);

    		Graphics2D g2d = copy.createGraphics();

    		// copy image
    		g2d.drawImage(src, 0, 0, null);
    		g2d.dispose();

    		return copy; 
	  }

	  public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
	
		BufferedImage bimage = new BufferedImage(
			img.getWidth(null), img.getHeight(null),
			BufferedImage.TYPE_INT_ARGB
		);
	
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
	
		return bimage;
	}

}


