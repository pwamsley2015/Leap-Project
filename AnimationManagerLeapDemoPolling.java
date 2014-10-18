import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Vector;

/*
 * An example of a class that runs in its own thread (it is Runnable) and uses Leap data
 * to control an onscreen ship. This class is responsible for polling the Leap every so often
 * in a timed, controlled way (the DESIRED_DELAY variable is the number of milliseconds that
 * each frame of the animation should consume, so 1000/DESIRED_DELAY is frames per second).
 */
public class AnimationManagerLeapDemoPolling implements Runnable
{
	//This maintains the ship so that it can manipulate El Shippo based on Leap input
	//and the main window is saved so that the manager can force a repaint of the screen
	private Ship ship;		
	private JFrame mainWindow;
	
	
	//These are used in the timing structure, which becomes important if you really start
	//to crunch a lot of data or on lower-end systems. It's better to have closed-loop control
	//over the speed of your animation rather than letting it run in an open loop (not monitored)
	private long timeAtEndOfLoop = 0, timeAtBeginningOfLoop = 0, pause = 0;
	private final long DESIRED_DELAY = 40;
	
	
	//The Leap itself and the current frame object for the leap (which is really the frame of
	//data...which normally comes streaming fast and quick, but we will poll it based on the
	//timing of our animation loop).
	private Controller leapMotionController;
	private Frame currFrame;
	
	
	public AnimationManagerLeapDemoPolling( Ship s, JFrame mw )
	{
		ship = s;
		mainWindow = mw;
		leapMotionController = new Controller();
		//if you don't specifically enable gestures, they aren't available to you
		leapMotionController.enableGesture(Type.TYPE_SCREEN_TAP);
	}
	
	
	/*
	 * When you pass a Runnable object like this to a Thread constructor, the Thread object
	 * will automatically call the run() method for you. It only calls it once; if the run()
	 * method exits, then your separate thread dies off. However, to keep the party going in
	 * this run() method, I will use a lovely infinite loop. This code will then execute over
	 * and over but in a separate thread so that other code can run concurrently in the main 
	 * thread or any others that you might need to create.
	 */
	public void run()
	{
		
		//we want to keep animating FOREVER!!
		//If this method ends, then the animation ends...so keep drawing in an infinite
		//loop. Optionally, create a boolean variable for the condition and then give this
		//class a method that can change the value of that variable; this gives you a way to
		//turn the animation off. Or, you could call the stop() method on your Thread object
		//(the one you passed this instance to back in the main JFrame class).
		while(true)	 
		{
			//part of the timing structure...not Leap-related necessarily
			timeAtBeginningOfLoop = System.currentTimeMillis();
			
			
			//get front-most finger data from the Leap, via the current frame
			currFrame = leapMotionController.frame();
			FingerList fingers = currFrame.fingers();
			Finger frontFinger = fingers.frontmost();
			Vector fingerTip = frontFinger.tipPosition();
			//or, in one line:
			//Vector fingerTip = leapMotionController.frame().fingers().frontmost().tipPosition();
			
			//access the location of that fingertip in Leap's coordinate system
			float fingerX = fingerTip.getX();
			float fingerY = fingerTip.getY();
			float fingerZ = fingerTip.getZ();
			
			
			
			
			//now move the ship based on data
			if( fingerX < 10 && fingerX > -10 )
			{
				//Do nothing...this is called a dead band. If the hand isn't off-center
				//enough, if it doesn't cross a certain THRESHOLD, then ignore the data
			}
			else 
			{
				//a weak attempt at mapping the Leap's X-value to some in-game amount
				//if fingerX is positive, x gets increased...else, x gets decreased
				//dividing by 10 just scales the amount...
				//the Leap's x-coord has a real-world unit of millimeters from the Leap's middle
				ship.changeX((int)(fingerX/10));
			}
			
			
			if( fingerY > 170 && fingerY < 200 )
			{
				//do nothing...another dead band				
			}
			else
			{
				//map Leap Y-value to in-game value
				//feels backwards (subtracting y-ccord) because it is...
				//the up on the Leap is the positive y-axis, 
				//but the down direction onscreen is the positive y-axis
				//the real-world units on fingerY is millimeters above the Leap
				ship.changeY( (int)( (185 - fingerY)/10 ) );
			}
			
			
			//uncomment to see the Z data...can be useful to look at a part of the data stream
			//System.out.println(fingerZ);
			if( fingerZ < 10 && fingerZ > -10 )
			{
				//again...just a dead band...I'm just emphasizing this idea
			}
			else
			{
				//again, modify the Leap values to suit your needs
				ship.changeRadius((int)(fingerZ/35));
			}
			
			
			
			//For fun, check for a SCREEN_TAP Gesture...ignoring all others but they shouldn't
			//register anyways since I only enabled screen taps in the constructor above.
			//You must manually enable the gestures that you are interested in processing.
			GestureList gestures = currFrame.gestures();
			Iterator<Gesture> gestIter = gestures.iterator();
			while(gestIter.hasNext())
			{
				Gesture currGesture = gestIter.next();
				if(currGesture.type() == Type.TYPE_SCREEN_TAP)
				{
					ship.changeColor();
					break;
				}
			}
			
			
			
			
			
			//redraw...if you don't repaint(), then you will not SEE the changes
			mainWindow.repaint();
			
			
			
			
			
			
			//PAUSE...timing things to achieve consistent framerate
			timeAtEndOfLoop = System.currentTimeMillis();
			pause = DESIRED_DELAY - (timeAtEndOfLoop - timeAtBeginningOfLoop);
			if(pause < 0) pause = 1;
			try
			{
				Thread.sleep(pause);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//print to see how long these operations take...in reality, the code above
			//runs really quickly because not much is happening, but this would help
			//smooth out issues on different computers (slow vs fast hardware)
			//System.out.println(pause);	
		}
	}
}
