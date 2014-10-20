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
 * @see BeatLevelScreen
 */
@SuppressWarnings("serial")
public class StartScreen extends JFrame implements KeyListener {

	private StartPanel panel; 
	private MP3Player player; 

	private StartScreenAnimationMan animationMan; 
	private Thread aniThread; 

	private static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public StartScreen() {

		super("Welcome to Last Will"); 
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panel = new StartPanel(); 

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel, BorderLayout.CENTER);

		animationMan = new StartScreenAnimationMan(this); 
		aniThread = new Thread(animationMan); 
		aniThread.start(); 

		addKeyListener(this); 

		player = new MP3Player(); 

		File f1 = new File("sounds" + File.separator + "start.mp3");

		player.addSong(f1);

		player.play();

		graphicsDevice.setFullScreenWindow(this); 
		setVisible(true); 

	}

	@Override
	public void keyPressed(KeyEvent e) {
		setVisible(false); 
		//animationMan.kill();  
		player.pause(); 
		new MainClass(1, 0, null); 
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

}

@SuppressWarnings("serial")
class StartPanel extends JPanel {

	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
			HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private int counter = 250; 
	private boolean colorUp = false; 

	public StartPanel()
	{
		setBackground(Color.black );
	}
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		try {
			Image backGroundImage = ImageIO.read(new File("Graphics" + File.separator + "startBack.png"));
			g.drawImage(backGroundImage, 0, 0, null); 
		} 
		catch (IOException e) {
			System.out.println("error reading the background image"); 
			e.printStackTrace();		
		}
		if (colorUp)
			counter += 10;
		else
			counter -= 10; 

		if (counter < 0) {
			counter = 0; 
			colorUp = true; 
		}
		if (counter > 250) {
			counter = 250; 
			colorUp = false;
		}


		g.setColor(new Color(counter, counter, counter));

		g.setFont(new Font("Slant", Font.PLAIN, 40)); 
		g.drawString("Press  Any   Key  To  Play", 523, 50);

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
