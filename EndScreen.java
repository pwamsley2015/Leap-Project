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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * @author pwamsley
 * @see BeatLevelScreen 
 *
 */

@SuppressWarnings("serial")
public class EndScreen extends JFrame implements KeyListener {

	public static final int WIN = 0, LOSS = 1; 

	private EndPanel panel; 

	private MP3Player p;

	private static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public EndScreen(int win_or_loss, MainClass mainClass) {

		super("Game Over"); 

		setDefaultCloseOperation(EXIT_ON_CLOSE); 

		panel = new EndPanel(win_or_loss, mainClass); 

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel, BorderLayout.CENTER );

		addKeyListener(this);

		mainClass.getSongPlayer().pause(); 

		p = new MP3Player(); 

		File f1 = new File("sounds" + File.separator + "end.mp3");

		p.addSong(f1);

		p.play();

		graphicsDevice.setFullScreenWindow(this); 
		setVisible(true); 
	}

	@Override
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode(); 

		if (key != KeyEvent.VK_SPACE) {
			p.pause(); 
			setVisible(false); 
			new StartScreen(); 
		} 
		else {
			JOptionPane.showMessageDialog(null, "Thanks for your help, send me any bugs/suggestions");
			System.exit(0); 
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
@SuppressWarnings("serial")
class EndPanel extends JPanel {

	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
			HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private int win_or_loss; 
	private MainClass mainClass; 

	public EndPanel(int win_or_loss, MainClass mainClass)
	{
		this.win_or_loss = win_or_loss; 
		this.mainClass = mainClass; 
		setBackground(Color.black ); 
	}
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (win_or_loss == EndScreen.WIN) {

			try {
				g.setColor(Color.WHITE);
				g.drawString(mainClass.getHighScore().toString(), 300, 500); 
				Image backGroundImage = ImageIO.read(new File("Graphics" + File.separator + "win.png"));
				g.drawImage(backGroundImage, -2, -2, null);  
				g.drawString(mainClass.getHighScore().toString(), 1245, 350); 
				g.drawString(mainClass.getScore() + "", 70, 350); 
			} 
			catch (IOException e) {
				System.out.println("error reading the background image"); 
				e.printStackTrace();	
			}
		}
		else {
			try {
				Image backGroundImage = ImageIO.read(new File("Graphics" + File.separator + "loss.png"));
				g.drawImage(backGroundImage, 0, 0, null); 
				g.setColor(Color.white); 
				g.setFont(new Font("Slant", 1, 40)); 
				g.drawString(mainClass.getHighScore().toString(), 1245, 350); 
				g.drawString(mainClass.getScore() + "", 70, 350); 
			} 
			catch (IOException e) {
				System.out.println("error reading the background image"); 
				e.printStackTrace();	
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


