import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author pwamsley, Aldo Lamberti, Michael Maunu
 *
 */

@SuppressWarnings("serial")
public class GameArea extends JPanel
{
	public static final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
			HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	private MainClass gameObject; 

	public GameArea(MainClass g)
	{
		gameObject = g;
		setBackground(Color.black);
		
		
	}
	/**
	 * called automatically from the JFrame, handles on screen drawing. 
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		try {
			Image backGroundImage = ImageIO.read(new File("Graphics" + File.separator + "grass.png"));
			g.drawImage(backGroundImage, 0, 0, null); 
		} 
		catch (IOException e) {
			System.out.println("error reading the background image"); 
			e.printStackTrace();
		} 

		ArrayList<Drawable> list = gameObject.getDrawableList();

		for(int pos = 0; pos < list.size(); pos++)
			list.get(pos).draw(g);

		if (gameObject.devModeOn()) {
			for(int pos = 0; pos < list.size(); pos++) {
				Drawable d = list.get(pos); 
				g.setColor(Color.white);
				g.drawRect(d.getX(), d.getY(), d.getWidth(), d.getHeight()); 
			}
		}
	}

	public Dimension getSize()
	{
		return new Dimension( WIDTH, HEIGHT );
	}
	public Dimension getMinimumSize()
	{
		return getSize();
	}
	public Dimension getMaximumSize()
	{
		return getSize();
	}
	public Dimension getPreferredSize()
	{
		return getSize();
	}
	public int getWidth()
	{
		return WIDTH;
	}
	public int getHeight()
	{
		return HEIGHT;
	}
}

