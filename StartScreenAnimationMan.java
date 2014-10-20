
public class StartScreenAnimationMan implements Runnable {

	private StartScreen frame; 
	//private boolean running; 

	public StartScreenAnimationMan(StartScreen frame) {
		this.frame = frame; 
		//running = true; 
	}

	@Override
	public void run() {

		//while (running) {

		for (;;) {
			frame.repaint(); 

			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}  
		}
	}
//	public void kill() {
//		running = false; 
//	}
}
