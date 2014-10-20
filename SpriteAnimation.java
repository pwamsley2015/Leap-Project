
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;


/**
 * 
 * @author Trevor Palmer, Patrick Wamsley, Micheal Maunu, Aldo Lamberti 
 *
 * Terminology:
 * frame = an image being displayed
 * number of repetitions= the number of times one frame is repeated.
 * 					  =this allows for Images to be shown longer on screen.
 * 
 */

public class SpriteAnimation {
	private int currFrame, numberOfRepitions;
	//numberOfReps is the number of times(default is !!!ONE!!!) a frame is repeated before moving onto the next frame
	private boolean isOnALoop, forwardSequence;
	private Image[] scenes;

	//usage: new SpriteAnimation( "happy", "jpg", 5,9 ) will get images happy1.jpg, happy2.jpg...happy5.jpg
	public SpriteAnimation(String baseFileName, String fileType, int nOfFrames, int numOfReps) {
		
		scenes = new Image[nOfFrames];
		numberOfRepitions=numOfReps;

		try
		{
			for (int i = 0; i < nOfFrames; i++)
				scenes[i] = ImageIO.read(new File("Graphics" + File.separator + baseFileName + (i + 1) + "." + fileType));
		}
		catch(Exception e)
		{
			System.err.println("error reading files in Sprite constructor: ");
			System.err.println("Base File Name: " + baseFileName);
			System.err.println("File Type: " + fileType);
			e.printStackTrace();
		}
		currFrame = 0;
	} 


	//returns the size of the list of images in an int
	public int getSize() {
		return (scenes.length * numberOfRepitions);
	}


	//these two methods allow the user to put a sprite animation on a continuous loop or turn it off
	public void setLoopingOn() {
		isOnALoop = true;
	}
	public void setLoopingOff() {
		isOnALoop = false;
	}


	//returns the status of the loop, either looping or not looping
	public boolean isLooping() {
		return isOnALoop;
	}


	//allows the user to move to the next image in the list
	public void forwardAFrame() {

		currFrame++;

		if (currFrame>=scenes.length*numberOfRepitions) {
			if (isLooping())
				currFrame = 0;
			else
				currFrame = (scenes.length*numberOfRepitions) - 1;
		}
	}


	//allows user to move back to an image in the list
	public void backwardsAFrame() {

		currFrame--;

		if (currFrame <= 0) {
			if (isLooping())
				currFrame = scenes.length-1;
			else
				currFrame = 0;
		}
	}


	//returns an Image variable of the current image being displayed or accessed
	public Image getCurrImage() {

		if (numberOfRepitions>0) {
			//			System.out.println("currFrame is " + currFrame);
			return scenes[currFrame/numberOfRepitions];
		}
		else
			return scenes[0];
	} 

	public int getCurrentPositionOfImage() {
		return currFrame;
	}


	public void reset() {
		if (forwardSequence)
			currFrame = 0;
		else
			currFrame = scenes.length-1;
	}
}


