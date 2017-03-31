import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.media.Media;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoPlayer implements Player
{
	private static final ArrayList<String> SUPPORTED_IMAGE_FORMATS = new ArrayList<String>(Arrays.asList("jpg", "png", "jpeg"));
	
	private final JFrame frame;
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer player;
	private long videoDuration;
	private boolean hasMedia;
	
	//TODO Currently using these GUI controls for testing. Remove when finished with VideoPlayer
	private JPanel contentPane;
	private JButton resumeButton;
	private JButton rewindButton;
	private JButton	skipButton;
	private JButton	stopButton;
	private JSlider volumeSlider;
	private JSlider timeSlider;
	private JButton captureButton;
	
	//Constructors
	public VideoPlayer()
	{
		videoDuration  = 0;
		
		new NativeDiscovery().discover();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);

		JPanel controlsPane = new JPanel();
		resumeButton = createPlayPauseButton();
		controlsPane.add(resumeButton);
//		rewindButton = new JButton("Rewind");
//		controlsPane.add(rewindButton);
//		skipButton = new JButton("Skip");
//		controlsPane.add(skipButton);
		stopButton = createStopButton();
		controlsPane.add(stopButton);
		
		//TODO Capture button for testing purposes; Remove when done
		captureButton = new JButton("Capture");
		captureButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				captureScreen("PNG");
			}
		});
		
		controlsPane.add(captureButton);
		contentPane.add(controlsPane, BorderLayout.SOUTH);
		

		volumeSlider = createVolumeSlider();
		timeSlider = createTimeSlider(0);
		contentPane.add(volumeSlider, BorderLayout.EAST);
		
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
		
		hasMedia = false;
		player = mediaPlayerComponent.getMediaPlayer();
	}
	
	public VideoPlayer(String filePath)
	{
		this();
		loadVideo(filePath);
	}
	
	
	//GUI control generaiton
	/**
	 * Generates a Stop Button control
	 * @return A button that can stop video playback
	 */
	private JButton createStopButton()
	{
		JButton stop = new JButton("Stop");
		stop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				stopVideo();
			}
		});
		return stop;
	}
	
	/**
	 * Generates a Play/Pause button
	 * @return A button that can pause and resume video playback
	 */
	private JButton createPlayPauseButton()
	{
		JButton playPause = new JButton("Play/Pause");
		playPause.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				alternatePlayback();
			}
		});
		
		return playPause;
	}
	
	/**
	 * Generates a volume slider
	 * @return A JSlider that controls the volume of the video playback
	 */
	private JSlider createVolumeSlider()
	{
		JSlider vSlider = new JSlider();
		vSlider.addChangeListener(new ChangeListener() 
			{
				@Override
				public void stateChanged(ChangeEvent e) 
				{
					changeVolume(vSlider.getValue());	
				}
			});
		
		return vSlider;
	}
	
	/**
	 * Generates a time seeker slide bar
	 * @return A JSlider that keeps track of the time in the playback
	 */
	private JSlider createTimeSlider(int videoTime)
	{
		JSlider tSlider = new JSlider(0, videoTime);
		tSlider.addChangeListener(new ChangeListener()
				{
					@Override
					public void stateChanged(ChangeEvent e)
					{
						System.out.println(tSlider.getValue());
						seekVideo(tSlider.getValue());
					}
				});
		
		return tSlider;
	}
	
	//VideoPlayer functionality implementation
	@Override
	public void open(String fileName)
	{
		openVideo(fileName);
	}
	
	/**
	 * Opens the video player and plays its selected media if one has already been selected
	 */
	public void openVideo(String fileName)
	{
		
		loadVideo(fileName);
		if(!frame.isVisible())
			showPlayer();
		
		playVideo();
	}
	
	/**
	 * Reveals the video player frame
	 */
	public void showPlayer()
	{
		frame.setContentPane(contentPane);
		frame.setVisible(true);
		SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						new VideoPlayer();
					}
				});
	}
	
	/**
	 * Takes a screenshot of the currently playing video if the playback is paused
	 * @param format Image format to save the image as
	 */
	private void captureScreen(String format)
	{
		if(!player.isPlaying() && SUPPORTED_IMAGE_FORMATS.contains(format.toLowerCase()))
		{
			try
			{
				long currentTime = System.currentTimeMillis();
				String fileName = "capture" + currentTime + "." + format;
				
				BufferedImage playerImage = player.getSnapshot();
				ImageIO.write(playerImage, format, new File("media libraries/image/", fileName));
				System.out.println("Screenshot saved in media libraries/image/" + fileName);
			}
			catch(IOException ex)
			{
				System.err.println(ex);
			}
		}
		else
			System.out.println("Video must be paused before screenshotting!");
	}
	
	/**
	 * Plays the current video in the media player; Plays from paused position
	 */
	public void playVideo()
	{
		if(hasVideo())
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
	 * @return Returns true if the video from filePath was loaded into the player; False otherwise
	 */
	public boolean loadVideo(String filePath)
	{
		boolean loaded = false;
		if(filePath != null)
		{
			if(player.isPlaying())
			stopVideo();
			loaded = player.prepareMedia(filePath);
			player.parseMedia();
			videoDuration = player.getMediaMeta().getLength() / 1000;
			timeSlider.setMaximum((int)videoDuration);
			hasMedia = true;
		}
		
		return loaded;
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
	
	//Getter-setters
	/**
	 * Get the JFrame of this player
	 * @return
	 */
	public JFrame getFrame()
	{
		return frame;
	}
	
	/**
	 * Get the media player associated with this VideoPlayer
	 */
	public EmbeddedMediaPlayer getPlayer()
	{
		return player;
	}
	
	//Bool conditions
	/**
	 * Determine if this player is loaded with a video
	 */
	public boolean hasVideo()
	{
		return hasMedia;
	}
	
	//Player interface implementation
	@Override
	public void volumeChange(double newVolume) 
	{
		changeVolume((int)newVolume);
	}

	@Override
	public void alternatePlayback() 
	{
		if(getPlayer().isPlaying())
			pauseVideo();
		else
			playVideo();
	}

	@Override
	public void skipPlayback() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void changePosition(long playbackPosition) 
	{
		seekVideo(playbackPosition);
	}	
	
	public static void main(String[] args) throws InterruptedException
	{
		//Testing stuff
		VideoPlayer v = new VideoPlayer("media libraries/video/kaius_presentation.mp4");
		v.showPlayer();
		v.pauseVideo();
	}
}

