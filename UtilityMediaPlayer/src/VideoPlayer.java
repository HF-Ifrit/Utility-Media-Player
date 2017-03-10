import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.media.Media;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class VideoPlayer implements Player
{
	private final JFrame frame;
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer player;
	private long videoDuration;
	
	private JPanel contentPane;
	private JButton resumeButton;
	private JButton rewindButton;
	private JButton	skipButton;
	private JButton	stopButton;
	private JSlider volumeSlider;
	private JSlider timeSlider;
	
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

		player = mediaPlayerComponent.getMediaPlayer();
	}
	
	public VideoPlayer(String filePath)
	{
		this();
		loadVideo(filePath);
	}
	
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
				if(player.isPlaying())
					pauseVideo();
				else
					playVideo();
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
	
	/**
	 * Reveals the video player and plays its selected media if one has already been selected
	 */
	@Override
	public void open()
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
		player.parseMedia();
		videoDuration = player.getMediaMeta().getLength() / 1000;
		timeSlider.setMaximum((int)videoDuration);
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
	}	
}

