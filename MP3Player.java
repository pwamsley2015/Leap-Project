import java.io.File;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class allows you to create a playlist of MP3s and then play them.
 * @author mmaunu
 *
 */
public class MP3Player
{
	private ArrayList<Media> songList;
	private MediaPlayer songPlayer;
	private int currSongIndex;
	private double currVolume;
	private double currSeekTime;

	public MP3Player()
	{
		songList = new ArrayList<Media>();
		//required for the MediaPlayer object to work...
		@SuppressWarnings("unused")
		JFXPanel fxPanel = new JFXPanel();
		currSongIndex = 0;
		currVolume = 1;
		songPlayer = null;
		setCurrSeekTime(0);
	}

	public void addSong(String songName)
	{
		File file = new File( songName );
		songList.add( new Media(file.toURI().toString()) );
	}

	public void addSong(File file)
	{
		songList.add( new Media(file.toURI().toString()) );
	}

	public void play()
	{

		if(songList.size() == 0)
			return;

		if(currSongIndex >= songList.size())
			currSongIndex = 0; 

		if(songPlayer == null || songPlayer.getStatus() == MediaPlayer.Status.STOPPED)
		{
			//System.out.println("1" + "songPlayer:" + songPlayer);
			Media currMedia = songList.get(currSongIndex);
			songPlayer = new MediaPlayer(currMedia);
			//System.out.println("2" + " songPlayer:" + songPlayer);
		}

		//System.out.println(songPlayer.getStatus());

		if(songPlayer.getStatus() != MediaPlayer.Status.PLAYING)
		{
			//Setting the volume here...each new MediaPlayer object will need to have
			//its volume set and a new MediaPlayer object is definitely created each time 
			//the track changes...so just set the volume before each call to play
			songPlayer.setVolume(currVolume);
			songPlayer.play();
		}
	}

	private void stop()
	{
		if(songPlayer != null && songPlayer.getStatus() != MediaPlayer.Status.STOPPED)
			songPlayer.stop();
	}

	public void pause()
	{
		if(songPlayer != null && songPlayer.getStatus() == MediaPlayer.Status.PLAYING)
			songPlayer.pause();
	}

	public void restartCurrentTrack()
	{
		boolean wasPlaying = isPlaying();

		stop();

		//if it was playing, then play song automatically
		if(wasPlaying)
			play();
	}

	public void nextTrack()
	{
		boolean wasPlaying = isPlaying();

		stop();
		currSongIndex++;
		if(currSongIndex >= songList.size())
			currSongIndex = 0;

		//if it was playing, fast forward and then play next song automatically
		if(wasPlaying)
			play();
	}

	public boolean isPlaying()
	{
		if(songPlayer != null)
			return songPlayer.getStatus() == MediaPlayer.Status.PLAYING;
		else
			return false;
	}

	public boolean isPaused()
	{
		if(songPlayer != null)
			return songPlayer.getStatus() == MediaPlayer.Status.PAUSED;
		else
			return false;
	}

	public void previousTrack()
	{
		boolean wasPlaying = isPlaying();

		stop();
		currSongIndex--;
		if(currSongIndex < 0)			//wrap-around logic
			currSongIndex = songList.size() - 1;
		if(currSongIndex < 0)			//if size is 0, then index is -1 after last line
			currSongIndex = 0;

		//if it was playing, rewind and then play previous song automatically
		if(wasPlaying)
			play();
	}

	public double getVolume()
	{
		return currVolume;
	}

	public void setVolume(double volume)
	{
		if(volume < 0)
			currVolume = 0;
		else if (volume > 1)
			currVolume = 1;
		else
			currVolume = volume;

		if(songPlayer != null)
			songPlayer.setVolume(currVolume);
	}

	public void increaseVolume(double deltaVolume)
	{
		setVolume( getVolume() + deltaVolume );
	}

	public void decreaseVolume(double deltaVolume)
	{
		setVolume( getVolume() - deltaVolume );
	}
	@Override
	public String toString() {
		return "MP3Player: currently playing: " + songList.get(currSongIndex) + "- paused:" + isPaused(); 
	}
	public void printList() {

		for (Media mediaObject : songList) 
		{
			System.out.println(mediaObject.toString());
		}
	}
	public MediaPlayer getPlayer() {
		return songPlayer; 
	}

	public double getCurrSeekTime() {
		return currSeekTime;
	}

	public void setCurrSeekTime(double currSeekTime) {
		this.currSeekTime = currSeekTime;
	}

	//	public static void main(String[] args) {
	//		MP3Player p = new MP3Player(); 
	//
	//		File f1 = new File("sounds" + File.separator + "test.mp3");
	//
	//		p.addSong(f1);
	//
	//		p.play();
	//	}
}
