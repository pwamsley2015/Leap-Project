import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author pwamsley
 *
 */
@SuppressWarnings("serial")
public class BeatLevelScreen extends JFrame implements KeyListener {


	private BeatPanel panel; 

	private int level; 

	private int score; 

	private MP3Player songPlayer; 

	private static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public BeatLevelScreen(int level, int score, MainClass mainClass) {

		super(""); 

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mainClass.saveHighScore();

		this.level = level; 
		this.score = score; 

		songPlayer = mainClass.getSongPlayer(); 

		panel = new BeatPanel(score, mainClass); 

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel, BorderLayout.CENTER );

		addKeyListener(this); 

		graphicsDevice.setFullScreenWindow(this); 
		setVisible(true); 
	}

	@Override
	public void keyPressed(KeyEvent e) {
		setVisible(false); 
		System.out.println(score); 
		new MainClass(level, score, songPlayer); 
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}

class BeatPanel extends JPanel {

	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
			HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	private String score; 

	public BeatPanel(int score, MainClass m)
	{
		this.score = "" + score; 
		setBackground(Color.black );
	}
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		try {
			Image backGroundImage = ImageIO.read(new File("Graphics" + File.separator + "beatlevelscreen.jpg"));
			g.drawImage(backGroundImage, 0, 0, null); 
		} 
		catch (IOException e) {
			System.out.println("error reading the background image"); 
			e.printStackTrace();		
		}

		g.setColor(Color.white); 
		g.setFont(new Font("Slant", 1, 20)); 
		g.drawString(score, 1275, 340); 
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
