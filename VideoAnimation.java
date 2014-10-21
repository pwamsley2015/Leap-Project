import java.awt.Graphics;
import java.util.ArrayList;


/**
 * @author Patrick Wamsley
 */

public class VideoAnimation implements Drawable { 

	private SpriteAnimation sprite; 

	private int x, y; 

	private int nOfFrames; 

	private boolean isOver; 

	private ArrayList<Drawable> drawList; 

	public VideoAnimation(int x, int y, String baseFileName, String fileType, int nOfFrames, int numOfReps, ArrayList<Drawable> drawList) {

		this.nOfFrames = nOfFrames; 

		sprite = new SpriteAnimation(baseFileName, fileType, nOfFrames, numOfReps); 

		sprite.setLoopingOff();

		this.x = x;
		this.y = y; 

		this.drawList = drawList; 

		drawList.add(drawList.size() - 1, this);

		isOver = false; 
	}

	/**
	 * use this if you need to do something when the animation finishes playing
	 * @return true only if animation is done playing
	 */
	public boolean isOver() {
		return isOver; 
	}

	@Override
	public void draw(Graphics g) {

		sprite.forwardAFrame(); 
		g.drawImage(sprite.getCurrImage(), x, y, null); 

		//System.out.println("nOfframes = " + nOfFrames + " currImg:" + sprite.getCurrentPositionOfImage());
		if (sprite.getCurrentPositionOfImage() + 1 == nOfFrames) 
			end(); 
	}

	public void end() {
		isOver = true; 
		drawList.remove(this); 
	}
	@Override
	public int getX() {
		return y;
	}

	@Override
	public int getY() {
		return x;
	}
	@Override
	public int getWidth() {
		return 0;
	}
	@Override
	public int getHeight() {
		return 0;
	}
	public void reset() {
		sprite.reset(); 
		sprite.setLoopingOn();
		drawList.add(0, this); 
		isOver = false; 
	}
}