import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author pwamsley
 */

public class Objective implements Drawable {

	/**
	 * x and y position on screen
	 */
	private int x, y; 
	/**
	 * Image drawn on screen
	 */
	private Image img; 
	/**
	 * used to know if objective is "taken" by a enemy object
	 */
	private boolean taken; 

	/**
	 * only constructor 
	 * @param x
	 * @param y
	 */
	public Objective(int x, int y) {

		this.x = x;
		this.y = y; 

		try {
			img = ImageIO.read(new File("Graphics" + File.separator + "package.png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		taken = false; 
	}

	/**
	 * draws the object on screen if it is not taken
	 */
	@Override
	public void draw(Graphics g) {

		if (img != null && !taken)
			g.drawImage(img, x, y, null); 
		else if (taken); 
		else {
			g.setColor(Color.BLUE); 
			g.fillRect(x, y, getHeight(), getWidth());
		}
	}
	/**
	 * @return true if taken
	 */
	public boolean isTaken() {
		return taken; 
	}
	/**
	 * @param true if being set to taken
	 */
	public void setTaken(boolean t) {
		taken = t; 
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		if (img == null) {
			System.out.println("Objective img is currently null");
			return 10; 
		}
		return img.getWidth(null); 
	}

	@Override
	public int getHeight() {
		if (img == null) {
			System.out.println("Objective img is currently null");
			return 10; 
		}
		return img.getHeight(null); 
	}

}
