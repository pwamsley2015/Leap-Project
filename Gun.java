import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Gun implements Drawable, Actable {

	/**
	 * NOT CENTER OF SCOPE. 
	 * Top left corner of scope, used to draw scope
	 */
	private int x, y; 
	/**
	 * x + X_OFF = center X of scope
	 * y + Y_OFF = center Y of scope
	 */
	private static int X_OFF, Y_OFF; 

	private Image img, blacknessImg; 

	private boolean isScopedIn; 

	private long timeSinceLastShot;

	private MainClass mainClass; 

	private int bulletsLeft; 

	private MP3Player player; 

	private int delayTime; 

	private int ammoInClip; 

	private static int fullClip = 4; 

	private boolean reloading; 

	private static VideoAnimation reloadingAnimation;

	public Gun(MainClass m) {

		x = 0;
		y = 0; 

		try {
			img = ImageIO.read(new File("Graphics" + File.separator + "scope.png"));
			blacknessImg = ImageIO.read(new File("Graphics" + File.separator + "black.png")); 
		} 
		catch (IOException e) {
			System.out.println("error reading scope image"); 
			e.printStackTrace();
		} 

		X_OFF = img.getWidth(null)/2; 
		Y_OFF = img.getHeight(null)/2; 

		bulletsLeft = 200; 

		delayTime = 1000; 

		isScopedIn = false; 

		ammoInClip = fullClip; 

		reloading = false; 

		timeSinceLastShot = Integer.MAX_VALUE;  

		mainClass = m; 
	}

	@Override
	public void act() {

		if (reloadingAnimation != null && reloading) 
			if (reloadingAnimation.isOver())
				reloading = false; 

		ArrayList<Drawable> list = mainClass.getDrawableList(); 

		boolean enemyLeft = false; 
		boolean enemyDrawn = false; 

		for (Drawable drawable : list) {
			if (drawable instanceof Enemy) {
				enemyDrawn = true; 
				Enemy enemy = (Enemy)drawable; 
				if (enemy.isAlive()) {
					enemyLeft = true;
					break; 
				}
			}
		}

		if (!enemyDrawn)
			enemyLeft = true; 
		//thus ends the worstly named/logicaly structured code ive ever writen that somehow makes this work. 

		if (!enemyLeft) {
			mainClass.saveHighScore(); 
			mainClass.end(); 
			new BeatLevelScreen(mainClass.getNextLevel(), mainClass.getScore(), mainClass); 
		}

		timeSinceLastShot += 100; 

	}

	public void shoot() {

		if (timeSinceLastShot > delayTime && bulletsLeft > 0) {

			playShotSound(); 

			ArrayList<Drawable> list = mainClass.getDrawableList(); 

			int centerX = x + X_OFF, centerY = y + Y_OFF; 

			for (int i = 0; i < list.size(); i++) {

				Drawable currDrawable = list.get(i); 

				if (currDrawable instanceof Gun || currDrawable instanceof Objective)
					continue;

				Rectangle collisionRectAroundEnemy = new Rectangle(currDrawable.getX(), currDrawable.getY(), 
						currDrawable.getWidth(), currDrawable.getHeight());

				Rectangle bulletRect;

				if (isScopedIn)
					bulletRect = new Rectangle(centerX, centerY, 5, 5);  
				else
					bulletRect = new Rectangle(centerX, centerY, 2, 2); 

				if (collisionRectAroundEnemy.intersects(bulletRect)) {
					if (mainClass.devModeOn())
						System.out.println("enemy hit"); 
					if (!(currDrawable instanceof Enemy))
						throw new UnsupportedOperationException("haven't added other enemies"); 
					else {
						Enemy enemyHit = (Enemy)currDrawable; 
						enemyHit.setHealth(enemyHit.getHealth() - 5);
						if (enemyHit.getHealth() <= 0 && enemyHit.hasObjective()) {
							mainClass.setObjective(new Objective(enemyHit.getX(), enemyHit.getY())); 
							mainClass.getDrawableList().add(mainClass.getObjective()); 
							enemyHit.setHasObj(false); 
							enemyHit.kill(); 
						}
						else if (enemyHit.getHealth() <= 0)
							enemyHit.kill(); 
					}
				}
			}

			timeSinceLastShot = 0; 
			ammoInClip--;  

			if (ammoInClip == 0) 
				reload(); 
		}

	}

	public void scopeIn() {
		isScopedIn = true;
	}

	public void scopeOut() {
		isScopedIn = false; 
	}

	public void fixGunPos(int x, int y) {
		this.x = x - X_OFF;
		this.y = y - Y_OFF;
	}

	public void reload() {

		//reloadingAnimation = new VideoAnimation(300, 300, "Reload Sprite ", "png", 72, 1, mainClass.getDrawableList());
		if (reloadingAnimation == null) {
			reloadingAnimation = new VideoAnimation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2) - X_OFF, 
					(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - Y_OFF, 
					"Reload Sprite ", "png", 73, 1, mainClass.getDrawableList());
		}	
		else {
			reloadingAnimation.reset(); 
		}

		reloading = true; 

		bulletsLeft -= fullClip - ammoInClip; 
		ammoInClip = fullClip; 

		timeSinceLastShot = -1000; 
	}

	@Override
	public void draw(Graphics g) {

		if (!isScopedIn) {
			g.setColor(Color.red); 
			g.fillOval(x + X_OFF, y + Y_OFF, 5, 5);
		}
		else if (!reloading) { //reloading animation handled in VideoAnimation class
			g.drawImage(img, x, y, null);
			g.drawImage(blacknessImg, x - (blacknessImg.getWidth(null)/2) + X_OFF, y - (blacknessImg.getHeight(null)/2) + Y_OFF, null); 
			if (bulletsLeft > 0 ) {
				g.setColor(Color.yellow);
				g.drawString("AMMO LEFT: " + bulletsLeft, x, y); 
				g.drawString("CLIP: " + ammoInClip, x, y - 15);
			}
			else {
				if (System.currentTimeMillis() % 2 == 0) {
					g.setColor(Color.RED); 
					g.drawString("OUT OF AMMO", x, y);
				}
				else {
					g.setColor(Color.WHITE);
					g.drawString("OUT OF AMMO", x, y);
				}
			}
		}
	}

	private void playShotSound() {

		player = new MP3Player(); //make it a new player to reset audio playlist

		File shootingAudioFile = new File("sounds" + File.separator + "shot.mp3"); 
		player.addSong(shootingAudioFile); 
		player.play();

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
		return img.getWidth(null); 
	}

	@Override
	public int getHeight() {
		return img.getHeight(null); 
	} 
	public void setDevAmmo() {
		bulletsLeft = Integer.MAX_VALUE; 
	}
	public void devModeDelay() {
		delayTime = 0; 
	}
	public boolean isScoped() {
		return isScopedIn; 
	}

	public boolean isReloading() {
		return reloading;
	}
}
