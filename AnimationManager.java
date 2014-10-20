import java.util.ArrayList;

public class AnimationManager implements Runnable
{
	private static MainClass gameObject; 

	private boolean notPaused = true; 

	private long lastTime; 

	public AnimationManager(MainClass mg)
	{
		gameObject = mg;
		notPaused = true;

		lastTime = System.currentTimeMillis(); 
	}

	public void run()
	{
		ArrayList<Drawable> list;

		while(true)
		{
			if(notPaused)
			{
				list = gameObject.getDrawableList();

				for( int pos = 0 ; pos < list.size() ; pos++ )
				{
					Drawable d = list.get(pos);
					if( d instanceof Actable )
					{
						((Actable)d).act();
					}
				}
				gameObject.repaint(); 
			}

			try
			{
				System.out.println(System.currentTimeMillis() - lastTime); 
				lastTime = System.currentTimeMillis(); 
				
				Thread.sleep(16); //DAT 60 FPS
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public void pause(boolean p)
	{
		notPaused = !p;
	}
}
