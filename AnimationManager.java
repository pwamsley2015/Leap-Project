import java.util.ArrayList;
import java.util.Iterator;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Vector;

public class AnimationManager implements Runnable
{
	private static MainClass gameObject; 

	private boolean notPaused = true; 

	private long lastTime; 

	private int desiredPauseTime;
	private long startTime; 

	private Controller leapController; 
	private Frame currFrame; 

	public AnimationManager(MainClass mg)
	{
		gameObject = mg;
		notPaused = true;

		leapController = new Controller(); 

		leapController.enableGesture(Type.TYPE_SCREEN_TAP); //shoot
		leapController.enableGesture(Type.TYPE_SWIPE); //reload

		lastTime = System.currentTimeMillis(); 

		desiredPauseTime = 16; //60 fps
	}

	public void run()
	{
		ArrayList<Drawable> list;

		while(true)
		{
			if(notPaused)
			{
				startTime = System.currentTimeMillis(); 
				
				currFrame = leapController.frame();
				
				Vector fingerTip = leapController.frame().fingers().frontmost().tipPosition();

				//access the location of that fingertip in Leap's coordinate system
//				float fingerX = fingerTip.getX();
//				float fingerY = fingerTip.getY();
//				
//				int dX = 0, dY = 0; 
//
//				if (fingerX < 10 && fingerX > -10);
//				else 
//					 dX = (int)fingerX/10; 
//				if(fingerY > 170 && fingerY < 200);
//				else
//					dY = (int)((185 - fingerY)/10); 
//				
				Gun gun = gameObject.getGun(); 
//				gun.fixGunPos(dX, dY);
				
				GestureList gestures = currFrame.gestures();
				Iterator<Gesture> gestIter = gestures.iterator();
				while(gestIter.hasNext()) {
					Gesture currGesture = gestIter.next();
					if (currGesture.type() == Type.TYPE_SCREEN_TAP) {
						gun.shoot(); 
						break;
					}
					else if (currGesture.type() == Type.TYPE_SWIPE && !(gun.isReloading())) {
						gun.reload();
						break; 
					}
				}
		
				list = gameObject.getDrawableList(); 
				
				for (int curr = 0; curr < list.size(); curr++) {
					Drawable drawable = list.get(curr); 
					if (drawable instanceof Actable) {
						((Actable)drawable).act(); 
					}
				}

				gameObject.repaint();
			}
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
	public void pause(boolean p)
	{
		notPaused = !p;
	}
}
