import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * 
 * @author Patrick Wamsley
 * @version 0.9.8 - in developement. 
 *
 */
@SuppressWarnings("serial")
public class MainClass extends JFrame implements MouseMotionListener, KeyListener, MouseListener {


	private ArrayList<Drawable> drawableList;

	public static GameArea gameArea;

	private Thread animationThread;

	private AnimationManager aniManager;

	private Gun gun; 

	private boolean devModeOn;  

	private Container contentPane; 

	private Objective objective; 

	private int level;

	private Score score; 

	private long startTime; 

	private Score highScore; 

	private MP3Player songPlayer; 

	private int repaintCounter; 

	private long resetTime; 

	private int secondCount; 

	private static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]; //gets the first screen, in future make a setting to switch that? 

	public MainClass(int level, int score, MP3Player player) {
		super("Last Will");				
		setDefaultCloseOperation(EXIT_ON_CLOSE); 

		startTime = System.currentTimeMillis(); 

		if (songPlayer == null && level == 1) {
			songPlayer = new MP3Player(); 
			songPlayer.addSong(new File("sounds" + File.separator + "main.mp3"));
			songPlayer.setVolume(.15); 
			songPlayer.play(); 
		}
		else
			songPlayer = player; 

		if (level > 5) {
			player.pause(); 
			this.score = new Score(score); 
			new EndScreen(EndScreen.WIN, this); 
		}

		else  {

			setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
					(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()); 

			this.level = level; 
			this.score = new Score(score); 

			drawableList = new ArrayList<Drawable>();

			gameArea = new GameArea(this); 

			gun = new Gun(this); 
			drawableList.add(gun); 

			int randX = (int)(Math.random()*700) + 200, randY = (int)(Math.random()*500) + 200; 

			objective = new Objective(randX, randY); 
			drawableList.add(0, objective); 

			for (int i = 0; i < 5 * level; i++) 
				drawableList.add(0, new Enemy(0, (int)(Math.random()*900), this));

			addMouseMotionListener(this); 
			addKeyListener(this); 
			addMouseListener(this); 

			contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(gameArea, BorderLayout.CENTER);

			//transparent 16 x 16 pixel cursor image
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			//new blank cursor
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

			contentPane.setCursor(blankCursor);

			aniManager = new AnimationManager(this);
			animationThread = new Thread(aniManager);
			animationThread.start();

			repaintCounter = 0; 

			resetTime = startTime; 

			graphicsDevice.setFullScreenWindow(this); 
			setVisible(true);
		}
	}

	public ArrayList<Drawable> getDrawableList() {
		if (drawableList == null) {
			System.out.println("drawableList is Empty");
			drawableList = new ArrayList<Drawable>();			
		}
		return drawableList; 
	}

	public static void main(String[] args) {
		new StartScreen(); 
	}

	public void mouseDragged(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent e) {

		int x = e.getX(), y = e.getY();

		gun.fixGunPos(x, y);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); 

		if (key == KeyEvent.VK_SHIFT)
			gun.scopeIn(); 
		if (key == KeyEvent.VK_SPACE)
			gun.shoot(); 
		if (key == KeyEvent.VK_R)
			gun.reload(); 
		if (key == KeyEvent.VK_D) {
			System.out.println("trying to get out of full screen...");
			graphicsDevice.setFullScreenWindow(null); //this sometimes causes the program to crash, and that makes me sad. Look up fixes.
			System.out.println("got out of full screen");
			aniManager.pause(true); 
			if (JOptionPane.showInputDialog("Enter code").equalsIgnoreCase("::dmo")) 
				turnDevModeOn(); 
			else
				graphicsDevice.setFullScreenWindow(this); 
			aniManager.pause(false); 
		}
		if (devModeOn && key == KeyEvent.VK_C) {
			aniManager.pause(true); 
			String command = JOptionPane.showInputDialog("Enter Command"); 
			System.out.println(command);
			if (command.equalsIgnoreCase("::pause"))
				aniManager.pause(true);
			if (command.equalsIgnoreCase("::play"))
				aniManager.pause(false); 
			if (command.equalsIgnoreCase("::spawn")) {
				int x = Integer.parseInt(JOptionPane.showInputDialog("#")); 
				for (int i = 0; i < x; i++) 
					drawableList.add(new Enemy((int)(Math.random()*1400), (int)(Math.random()*900), this)); //MAKE THIS GENERIC YAYYY
			}
			if (command.equalsIgnoreCase("::clear")) {
				for (int i = drawableList.size() - 1; i >= 0; i--) {
					Drawable d = drawableList.get(i); 
					if (d instanceof Gun)
						continue;
					drawableList.remove(d); 
				}
			}
			if (command.equalsIgnoreCase("::cv")) {

				int newV = Integer.parseInt(JOptionPane.showInputDialog("#")); 

				for (Drawable drawable : drawableList) {
					if (!(drawable instanceof Enemy))
						continue;
					Enemy enemy = (Enemy)drawable; 
					enemy.changeVel(newV); 
				}
			}
			if (command.equalsIgnoreCase("::infa"))
				gun.setDevAmmo(); 
			if (command.equalsIgnoreCase("::ss"))
				score = new Score(Integer.parseInt(JOptionPane.showInputDialog("#"))); 
			if (command.equalsIgnoreCase("::win")) {
				end(); 
				new MainClass(6, 0, songPlayer); 
			}
			if (command.equalsIgnoreCase("::sd0"))
				gun.devModeDelay();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_SHIFT)
			gun.scopeOut(); 
	}
	public void keyTyped(KeyEvent e) {}

	public void turnDevModeOn() {
		Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR); 
		contentPane.setCursor(defaultCursor); 
		devModeOn = true;  
		System.out.println("Dev Mode is now on.");
	}

	public boolean devModeOn() {
		return devModeOn; 
	}

	public Objective getObjective() {
		return objective; 
	}

	public void setObjective(Objective o) {
		objective = o; 
	}

	public void end() {
		setVisible(false); 
		aniManager.pause(true); 
	}
	/**
	 * @return current level + 1	 
	 */
	public int getNextLevel() {
		return level + 1;  
	}
	/**
	 * @return the score earned by a user for that level
	 */
	public int getScore() { //I should rename this.
		//System.out.println(score);
		return score.getVal() + (int) (100000 - (System.currentTimeMillis() - startTime)); 
	}

	public int getCurrScore() {
		return score.getVal(); 
	}

	public Gun getGun() {
		return gun; 
	}
	/**
	 * searches a saveFile for the highScore object
	 * @return the highScore
	 */
	@SuppressWarnings("resource")
	public Score getHighScore() {

		try {

			File dataFile = new File("saveFile.dat"); 

			FileInputStream fis = new FileInputStream(dataFile); 
			ObjectInputStream ois = new ObjectInputStream(fis); 

			highScore = (Score)ois.readObject(); 

		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("Error in finding score from saved file.");
			highScore = new Score(0); 
		}

		return highScore; 
	}
	/**
	 * checks to see if the user's current score is higher than the previous high score
	 * if so, saves the current score object to a saveFile
	 */
	public void saveHighScore() {

		if (getCurrScore() > getHighScore().getVal()) {

			try {        

				File saveFile = new File("saveFile.dat");    
				FileOutputStream fos = new FileOutputStream(saveFile); 

				ObjectOutputStream oos = new ObjectOutputStream(fos); 

				oos.writeObject(score);   

				oos.flush();       
				fos.flush();

				oos.close();       
				fos.close();     

			}

			catch(Exception e) {  

				e.printStackTrace(); 
				System.out.println("failed to save score to a save file"); 

			} 
		}

	}
	@Override
	public void repaint() {
		super.repaint(); 

		if (devModeOn && repaintCounter > 60) {
			
			Graphics g = gameArea.getGraphics(); 

			/*
			 * calculate mod seconds
			 */

			int miliseconds = (int) (System.currentTimeMillis() - resetTime); 

			if (miliseconds > 1000) {
				resetTime = System.currentTimeMillis(); 
				secondCount++; 
			}

			if (secondCount >= 1) {
				System.out.println("repaintCounter: " + repaintCounter + " sec count: " + secondCount + " FPS: " + repaintCounter/(secondCount*1.0));
				//g.setColor(Color.white);
				g.drawString("" + (repaintCounter/(secondCount*1.0)), 100, 100);
			}
		}
		
		repaintCounter++; 
	}

	public MP3Player getSongPlayer() {
		return songPlayer; 
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		gun.shoot(); 
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
