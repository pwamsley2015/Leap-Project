import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

/**
 * 
 * @author pwamsley
 *
 */
public class Enemy implements Drawable, Actable {

	private int x, y; 

	private static SpriteAnimation sprite, scopedSprite; 

	private int health; 

	private static MainClass mainClass; 

	private int xVel; 

	private boolean hasObjective; 

	private static Objective obj;

	private boolean alive; 

	private int deathTime; 

	public Enemy(int x, int y, MainClass m) {

		this.x = x;
		this.y = y; 

		xVel = 2; 

		alive = true; 

		deathTime = 0; 

		mainClass =  m;

		health = 10; 

		sprite = new SpriteAnimation("run", "png", 28, 1);  
		scopedSprite = new SpriteAnimation("Srun", "png", 30, 1); 

		sprite.setLoopingOn(); 
		scopedSprite.setLoopingOn(); 

		hasObjective = false; 

		obj = mainClass.getObjective(); 
	} 

	@Override
	public void act() {

		if (alive) {

			sprite.forwardAFrame(); 
			scopedSprite.forwardAFrame(); 

			//update obj in case sniper kills current one
			obj = mainClass.getObjective(); 

			if ((Math.abs(x - obj.getX()) <= 2*xVel) && y == obj.getY() && !hasObjective && !obj.isTaken()) {
				obj.setTaken(true);
				hasObjective = true; 
			}
			if (!hasObjective && !obj.isTaken()) {
				if (obj.getY() > y)
					y++; 
				else 
					y--; 
				if (obj.getX() > x)
					xVel = Math.abs(xVel); 
				else
					xVel = -Math.abs(xVel); 
			}
			else if (hasObjective) {

				xVel = Math.abs(xVel);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
				int width = (int)screenSize.getWidth();

				if (x > width) {
					mainClass.end(); 
					new EndScreen(EndScreen.LOSS, mainClass); 
				}
			} 
			else if (!hasObjective && obj.isTaken()) 
				xVel = Math.abs(xVel); 

			x += xVel; 
		}
		else {
			deathTime += 100; 

			if (deathTime > 1000)
				mainClass.getDrawableList().remove(this); 
		}
	}
	public void changeVel(int v) {
		xVel = v; 
	}

	public void kill() {
		alive = false; 
		sprite.setLoopingOff(); 
		scopedSprite.setLoopingOff(); 
	}

	public boolean hasObjective() {
		return hasObjective; 
	}

	public void setHasObj(boolean b) {
		hasObjective = b; 
	}

	@Override
	public void draw(Graphics g) {

		Gun gun = mainClass.getGun(); 

		if (!gun.isScoped()) {

			if (!hasObjective)
				g.drawImage(sprite.getCurrImage(), x, y, null); 
			else {
				g.setColor(Color.red);
				g.drawRect(x, y, getWidth(), getHeight());
				g.setColor(Color.YELLOW); 
				g.drawString("PACKAGE", x, y + 5); 
				g.drawImage(sprite.getCurrImage(), x, y, null); 
			}
		}
		else {
			if (!hasObjective)
				g.drawImage(scopedSprite.getCurrImage(), x, y, null); 
			else {
				g.setColor(Color.red);
				g.drawRect(x, y, getWidth(), getHeight());
				g.setColor(Color.YELLOW); 
				g.drawString("PAC KAGE", x, y + 5); 
				g.drawImage(scopedSprite.getCurrImage(), x, y, null); 
			}	
		}
	}

	public void setHealth(int h) {
		health = h; 
	}

	public int getHealth() {
		return health; 
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
		Gun gun = mainClass.getGun(); 
		return gun.isScoped() ? scopedSprite.getCurrImage().getWidth(null) : sprite.getCurrImage().getWidth(null); 
	}

	@Override
	public int getHeight() {
		Gun gun = mainClass.getGun(); 
		return gun.isScoped() ? scopedSprite.getCurrImage().getHeight(null) : sprite.getCurrImage().getHeight(null);  
	}	
	public boolean isAlive() {
		return alive;
	}
}
