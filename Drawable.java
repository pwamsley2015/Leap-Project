import java.awt.Graphics;

/**
 * 
 * @author Michael Maunu
 *
 */

public interface Drawable {

	public void draw(Graphics g);

	public int getX();
	public int getY();

	public int getWidth(); 
	public int getHeight(); 
}
