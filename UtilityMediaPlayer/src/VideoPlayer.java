import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class VideoPlayer 
{
	private final JFrame frame;
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	
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
		
	}
	
	//Reveals the video player and plays its selected media
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
		mediaPlayerComponent.getMediaPlayer().playMedia("UtilityMediaPlayer/media libraries/video/WIN_20170227_19_51_17_Pro.mp4");
	}
	
	//Assigns the video file to the video player
	public void loadMedia(File videoFile)
	{
		
	}

	public JFrame getFrame()
	{
		return frame;
	}
	
	public static void main(String[] args)
	{
		VideoPlayer v = new VideoPlayer();
		v.open();
	}
}
