import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.media.Media;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class VideoPlayer 
{
	private final JFrame frame;
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer player;
	
	public VideoPlayer()
	{
		frame = new JFrame("Video Player");
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosing(WindowEvent e)
					{
						mediaPlayerComponent.release();
						System.exit(0);
					}
				});
		new NativeDiscovery().discover();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		player = mediaPlayerComponent.getMediaPlayer();
		
	}
	
	public VideoPlayer(String filePath)
	{
		this();
		player.prepareMedia(filePath);
	}
	
	/**
	 * Reveals the video player and plays its selected media
	 */
	public void open()
	{
		frame.setContentPane(mediaPlayerComponent);
		frame.setVisible(true);
		SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						new VideoPlayer();
					}
				});
		
		if(player.isPlayable())
			playVideo();
		
	}
	
	/**
	 * Plays the current video in the media player; Plays from paused position
	 */
	public void playVideo()
	{
		player.play();
	}
	
	/**
	 * Pauses playback of the current video
	 */
	public void pauseVideo()
	{
		player.pause();
		
	}
	
	/**
	 * Stops playback of the current video
	 */
	public void stopVideo()
	{
		player.stop();
	}
	
	/**
	 * Assigns the video file to the video player
	 * @param filePath The local file path of the video to be played
	 */
	public void loadVideo(String filePath)
	{
		player.prepareMedia(filePath);
	}
	
	/**
	 * Play video from a certain position
	 * @param time Time in milliseconds to play from
	 */
	public void seekVideo(long time)
	{
		if(player.isSeekable())
			player.setTime(time);
	}
	
	/**
	 * Change the volume of the current playing video
	 * @param volumePercentage The new percentage to set the volume to
	 */
	public void changeVolume(int volumePercentage)
	{
		player.setVolume(volumePercentage);
	}
	
	/**
	 * Get the JFrame of this player
	 * @return
	 */
	public JFrame getFrame()
	{
		return frame;
	}
	
	public static void main(String[] args)
	{
		//Testing stuff
		VideoPlayer v = new VideoPlayer("media libraries/video/kaius_presentation.mp4");
		v.open();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}
		
		v.pauseVideo();
	}
}
