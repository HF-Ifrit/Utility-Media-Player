import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VideoPlayer implements Player
{		
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer player;
	private PlayerControlsPanel controlPanel;
	
	private final int FFMPEG_GIF_PARAMETERS = 12;
	private final int FFMPEG_EXTRACT_AUDIO_PARAMETERS = 7;
	private final int FFMPEG_CROP_AUDIO_PARAMETERS = 10;
	private final int FFMPEG_CLIP_VIDEO_PARAMETERS = 10;
	
	private boolean hasMedia;
	private boolean finishedPlaying;
	private String videoPath;
	private MainFrame controller;
	
	private final String workingDir = System.getProperty("user.dir");
	private final String fileSep = System.getProperty("file.separator");
	private final String outputPath = workingDir + fileSep + "output";
	private final String ffmpegPath = workingDir + fileSep + "jars" + fileSep + "ffmpeg.exe";
	
	enum VideoFormat 
	{
		WEBM("vp8", "vorb"),
		MP4("mp4v", "mp4a");
		
		private VideoFormat(String vEncoder, String aEncoder)
	   	 {
	   		this.vcodec = vEncoder; 
	   		this.acodec = aEncoder;
	   	 }
	   	 
	   	 private final String vcodec;
	   	 private final String acodec;
	   	 
	   	 public String getVideoCodec()
	   	 {
	   		 return vcodec;
	   	 }
	   	 
	   	 public String getAudioCodec()
	   	 {
	   		 return acodec;
	   	 }
	}

	//Control panel for position and time sliders from 
	//https://github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj/test/basic/PlayerControlsPanel.java
	private class PlayerControlsPanel extends JPanel 
	{

		private static final long serialVersionUID = 1L;

		private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		private final EmbeddedMediaPlayer mediaPlayer;

		private JLabel timeLabel;
		//		    private JProgressBar positionProgressBar;
		private JSlider positionSlider;

		private boolean mousePressedPlaying = false;

		public PlayerControlsPanel(EmbeddedMediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;

			createUI();

			executorService.scheduleAtFixedRate(new UpdateRunnable(mediaPlayer), 0L, 1L, TimeUnit.SECONDS);
		}

		private void createUI() {
			createControls();
			layoutControls();
			registerListeners();
		}

		private void createControls() {
			timeLabel = new JLabel("hh:mm:ss");

			positionSlider = new JSlider();
			positionSlider.setMinimum(0);
			positionSlider.setMaximum(1000);
			positionSlider.setValue(0);
			positionSlider.setToolTipText("Position");
		}

		private void layoutControls() 
		{
			setBorder(new EmptyBorder(4, 4, 4, 4));

			setLayout(new BorderLayout());

			JPanel positionPanel = new JPanel();
			positionPanel.setLayout(new GridLayout(1, 1));
			// positionPanel.add(positionProgressBar);
			positionPanel.add(positionSlider);

			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BorderLayout(8, 0));

			topPanel.add(timeLabel, BorderLayout.WEST);
			topPanel.add(positionPanel, BorderLayout.CENTER);

			add(topPanel, BorderLayout.NORTH);
		}

		/**
		 * Broken out position setting, handles updating mediaPlayer
		 */
		private void setSliderBasedPosition() 
		{
			if(!mediaPlayer.isSeekable())
			{
				return;
			}
			float positionValue = positionSlider.getValue() / 1000.0f;
			// Avoid end of file freeze-up
			if(positionValue > 0.99f) {
				positionValue = 0.99f;
			}
			mediaPlayer.setPosition(positionValue);
		}

		private void updateUIState() 
		{
			if(!mediaPlayer.isPlaying()) 
			{
				// Resume play or play a few frames then pause to show current position in video
				mediaPlayer.play();
				if(!mousePressedPlaying) 
				{
					try 
					{
						// Half a second probably gets an iframe
						Thread.sleep(500);
					}
					catch(InterruptedException e) 
					{
						// Don't care if unblocked early
					}
					mediaPlayer.pause();
				}
			}
			long time = mediaPlayer.getTime();
			int position = (int)(mediaPlayer.getPosition() * 1000.0f);

			updateTime(time);
			updatePosition(position);
		}

		private void skip(int skipTime) {
			// Only skip time if can handle time setting
			if(mediaPlayer.getLength() > 0) {
				mediaPlayer.skip(skipTime);
				updateUIState();
			}
		}

		private void registerListeners() 
		{
			positionSlider.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent e) 
				{
					if(mediaPlayer.isPlaying()) {
						mousePressedPlaying = true;
						mediaPlayer.pause();
					}
					else {
						mousePressedPlaying = false;
					}
					setSliderBasedPosition();
				}

				@Override
				public void mouseReleased(MouseEvent e) 
				{
					setSliderBasedPosition();
					updateUIState();
				}
			});
		}

		private final class UpdateRunnable implements Runnable 
		{

			private final MediaPlayer mediaPlayer;

			private UpdateRunnable(MediaPlayer mediaPlayer) {
				this.mediaPlayer = mediaPlayer;
			}

			@Override
			public void run() 
			{
				final long time = mediaPlayer.getTime();
				final int position = (int)(mediaPlayer.getPosition() * 1000.0f);


				// Updates to user interface components must be executed on the Event
				// Dispatch Thread
				SwingUtilities.invokeLater(new Runnable() 
				{
					@Override
					public void run() {
						if(mediaPlayer.isPlaying()) {
							updateTime(time);
							updatePosition(position);
						}
					}
				});
			}
		}

		private void updateTime(long millis) {
			String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
			timeLabel.setText(s);
		}

		private void updatePosition(int value) {
			positionSlider.setValue(value);
		}
	}
	
	//Constructors
	public VideoPlayer()
	{
		//Set location of VLC dll installation and plugins folder
		File vlcFolder = new File("lib");
		String pluginPath = vlcFolder.getPath() + "\\plugins";
		uk.co.caprica.vlcj.binding.LibC.INSTANCE._putenv("VLC_PLUGIN_PATH="+vlcFolder.getPath());
		
		
		System.out.println("lib folder location: " + vlcFolder.getPath());
		System.out.println("plugins folder location: " + pluginPath);
		
		
		NativeLibrary.addSearchPath("libvlc", vlcFolder.getPath());
		
		
		//If previous method fails, just use native discovery with an installed version of VLC
		//new NativeDiscovery().discover();
		
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		this.controller = null;
		hasMedia = false;
		finishedPlaying = false;
		player = mediaPlayerComponent.getMediaPlayer();	
		player.addMediaPlayerEventListener(new MediaPlayerEventAdapter()
				{
					@Override
					public void finished(MediaPlayer mediaPlayer)
					{
						
						SwingUtilities.invokeLater(new Runnable()
								{
									@Override
									public void run()
									{
										controller.forwardFile();
									}
								});
					}	
				});
		controlPanel = new PlayerControlsPanel(player);
		mediaPlayerComponent.add(controlPanel, BorderLayout.SOUTH);
	}

	public VideoPlayer(String filePath)
	{
		this();
		loadVideo(filePath);
	}
	
	public VideoPlayer(MainFrame controller)
	{
		this();
		this.controller = controller;
	}
	
	public VideoPlayer(MainFrame controller, String filePath)
	{
		this();
		this.controller = controller;
		loadVideo(filePath);
	}

	//VideoPlayer functionality implementation

	/**
	 * Opens the video player and plays its selected media if one has already been selected
	 */
	public boolean openVideo(String fileName)
	{
		loadVideo(fileName);
		
		playVideo();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(player.isPlaying())
			return true;
		else
			return false;
	}

	/**
	 * Creates the player on a new thread
	 */
	private void showPlayer()
	{	
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new VideoPlayer(controller);
			}
		});
		controlPanel.setVisible(true);
	}

	/**
	 * Takes a screenshot of the currently playing video if the playback is paused
	 * @param format Image format to save the image as
	 */
	public void captureScreen(ImageViewer.ImageFormat format)
	{
		if(!player.isPlaying())
		{
			try
			{
				long currentTime = player.getTime();
				long maxLength = player.getLength();
				String fileName = "capture" + currentTime + "_" + maxLength + "." + format.toString().toLowerCase();

				BufferedImage playerImage = player.getSnapshot();
				ImageIO.write(playerImage, format.toString().toLowerCase(), new File("output/", fileName));
				System.out.println("Screenshot saved in output/" + fileName);
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
			videoPath = filePath;
			if(player.isPlaying())
				stopVideo();
			loaded = player.prepareMedia(filePath);
			player.parseMedia();
			showPlayer();
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
		if(player.isSeekable() && time >= 0.0 && time <= player.getLength())
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
	 * Extracts the entire audio clip from the video
	 * @param format The supported audio file format to save the file as. Supported formats are MP3 and FLAC
	 */
	public boolean extractAudio(MusicPlayer.MusicFormat format)
	{
		return extractAudio(0, (long)(player.getLength()/1000), format);
	}
	
	/**
	 * Extract audio from the current video at an input time, and save it in the output folder using the input format
	 * @param startTime The time in seconds to create the clip from
	 * @param endTime The time in seconds to end the audio clip
	 * @param format The supported audio file format to save the file as. Supported formats are MP3 and FLAC
	 */
	public boolean extractAudio(long startTime, long endTime, MusicPlayer.MusicFormat format)
	{
		if(!player.isPlayable()
				|| startTime == endTime
				|| startTime < 0 
				|| startTime > player.getLength() 
				|| endTime < 0 
				|| endTime > player.getLength()
				|| startTime > endTime)
			return false;
		else
		{
			//ffmpeg command for extracting audio from video:
			// [ffmpeg path] -i [video path] -vn -ab 256 [output.mp3]
			
			String[] ffmpegCommands = new String[FFMPEG_EXTRACT_AUDIO_PARAMETERS];
			ffmpegCommands[0] = ffmpegPath;
			ffmpegCommands[1] = "-i";
			ffmpegCommands[2] = videoPath;
			ffmpegCommands[3] = "-vn";
			ffmpegCommands[4] = "-ab";
			ffmpegCommands[5] = Integer.toString(256);
			
			String output = outputPath + fileSep + "extracted_audio_" + System.currentTimeMillis() + "." + format.toString().toLowerCase();
			ffmpegCommands[6] = output;
			
			int exitVal = 1;
			try 
			{
				Process proc = Runtime.getRuntime().exec(ffmpegCommands);
				
				 // any error message?
	            StreamGobbler errorGobbler = new 
	                StreamGobbler(proc.getErrorStream(), "ERROR");            
	            
	            // any output?
	            StreamGobbler outputGobbler = new 
	                StreamGobbler(proc.getInputStream(), "OUTPUT");
	                
	            // kick them off
	            errorGobbler.start();
	            outputGobbler.start();
	            
				//Block until ffmpeg has finished and given us an answer
	            exitVal = proc.waitFor();
	             
			} 
			catch (IOException | InterruptedException e1) 
			{
				System.out.println(e1.getMessage());
				return false;
			}
			
			if(startTime == endTime) //Determine if we need to crop any audio for a clip or if we can leave the entire file saved
			{
				System.out.println("Audio extracted to " + output);
				return (exitVal == 0);
			}
				
			else
			{
				//ffmpeg for cropping an audio file
				//ffmpeg -ss 00:01:30 -t 30 -acodec copy -i inputfile.mp3 outputfile.mp3

				ffmpegCommands = new String[FFMPEG_CROP_AUDIO_PARAMETERS];
				ffmpegCommands[0] = ffmpegPath;
				ffmpegCommands[1] = "-ss";
				ffmpegCommands[2] = Long.toString(startTime);
				ffmpegCommands[3] = "-t";
				ffmpegCommands[4] = Long.toString(endTime - startTime);
				ffmpegCommands[5] = "-acodec";
				ffmpegCommands[6] = "copy";
				ffmpegCommands[7] = "-i";
				ffmpegCommands[8] = output;
				ffmpegCommands[9] = output;

				//exec returns 0 on successful call
				try 
				{
					Process proc = Runtime.getRuntime().exec(ffmpegCommands);

					// any error message?
					StreamGobbler errorGobbler = new 
							StreamGobbler(proc.getErrorStream(), "ERROR");            

					// any output?
					StreamGobbler outputGobbler = new 
							StreamGobbler(proc.getInputStream(), "OUTPUT");

					// kick them off
					errorGobbler.start();
					outputGobbler.start();

					//Block until ffmpeg has finished and given us an answer
					exitVal = proc.waitFor();
					System.out.println("Audio extracted to " + output);
					return (exitVal == 0);
				} 
				catch (IOException | InterruptedException e1) 
				{
					System.out.println(e1.getMessage());
					return false;
				}
			}		
		}	
	}

	/**
	 * Create an animated GIF sequence from the input start time of the video to the input end time of the video
	 * @param startTime Time to start making the gif from in milliseconds
	 * @param endTime Time to end the gif in milliseconds
	 * @throws InterruptedException 
	 * @throws AWTException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public boolean gifClip(long startTime, long endTime)
	{
		if(!hasMedia
				|| startTime == endTime
				|| startTime < 0 
				|| startTime * 1000 > player.getLength() 
				|| endTime < 0 
				|| endTime * 1000> player.getLength()
				|| startTime > endTime)
		{
			System.out.println("Invalid time input. Either not within length of video (" + (player.getLength()/1000) + "s)"
					+ "or start and end times are equal / swapped");
			return false;
		}
		else
		{
			//ffmpeg command for converting video to gif:
			// [ffmpeg path] -ss [HH:MM:SS] -i [video file] -vf scale=[width]:-1 -t [endTime - startTime] -r 10 [output path]
			
			String[] ffmpegCommands = new String[FFMPEG_GIF_PARAMETERS];
			ffmpegCommands[0] = ffmpegPath;
			ffmpegCommands[1] = "-ss";
			ffmpegCommands[2] = Long.toString(startTime);
			ffmpegCommands[3] = "-i";
			ffmpegCommands[4] = "\""+ videoPath + "\"";
			ffmpegCommands[5] = "-vf";
			ffmpegCommands[6] = "scale=500:-1";
			ffmpegCommands[7] = "-t";
			ffmpegCommands[8] = Long.toString(endTime-startTime);
			ffmpegCommands[9] = "-r";
			
			int frameRate = 30;
			ffmpegCommands[10] = Integer.toString(frameRate);
			
			String output = outputPath + fileSep + "new_gif_" + System.currentTimeMillis() + ".gif";
			ffmpegCommands[11] = output;
			
			try 
			{
				Process proc = Runtime.getRuntime().exec(ffmpegCommands);
				
				 // any error message?
	            StreamGobbler errorGobbler = new 
	                StreamGobbler(proc.getErrorStream(), "ERROR");            
	            
	            // any output?
	            StreamGobbler outputGobbler = new 
	                StreamGobbler(proc.getInputStream(), "OUTPUT");
	                
	            // kick them off
	            errorGobbler.start();
	            outputGobbler.start();
	            
				//Block until ffmpeg has finished and given us an answer
	            int exitVal = proc.waitFor();
	            System.out.println("Gif created at " + output);
	            //exec returns 0 on successful call
	            return (exitVal == 0);
			} 
			catch (IOException | InterruptedException e1) 
			{
				System.out.println(e1.getMessage());
				return false;
			}
		}
	}
	
	/**
	 * Make a clip of a video
	 * @param start Time to start clip from in seconds
	 * @param finish Time to end clip from in seconds
	 */
	public boolean clipVideo(int start, int finish)
	{
		if(!hasMedia
				|| start == finish
				|| start < 0 
				|| start*1000 > player.getLength() 
				|| finish < 0 
				|| finish*1000 > player.getLength()
				|| start > finish)
		{
			System.out.println("Invalid time input. Either not within length of video (" + (player.getLength()/1000) + "s)"
					+ "or start and end times are equal / swapped");
			return false;
		}
		else
		{		
			//ffmpeg command for clipping a video:
			// [ffmpeg path] -i [input path] -ss [start time] -codec copy -t [finish - start] [output path]
			
			String extension = videoPath.substring(videoPath.lastIndexOf('.'));
			String[] ffmpegCommands = new String[FFMPEG_CLIP_VIDEO_PARAMETERS];
			ffmpegCommands[0] = ffmpegPath;
			ffmpegCommands[1] = "-i";
			ffmpegCommands[2] = "\"" + videoPath + "\"";
			ffmpegCommands[3] = "-ss";
			ffmpegCommands[4] = Integer.toString(start);
			ffmpegCommands[5] = "-codec";
			ffmpegCommands[6] = "copy";
			ffmpegCommands[7] = "-t";
			ffmpegCommands[8] = Integer.toString(finish - start);
			String output = outputPath + fileSep + "new_video_clip" + System.currentTimeMillis() + extension;
			ffmpegCommands[9] = output;
			
			try 
			{
				Process proc = Runtime.getRuntime().exec(ffmpegCommands);
				
				 // any error message?
	            StreamGobbler errorGobbler = new 
	                StreamGobbler(proc.getErrorStream(), "ERROR");            
	            
	            // any output?
	            StreamGobbler outputGobbler = new 
	                StreamGobbler(proc.getInputStream(), "OUTPUT");
	                
	            // kick them off
	            errorGobbler.start();
	            outputGobbler.start();
	            
				//Block until ffmpeg has finished and given us an answer
	            int exitVal = proc.waitFor();
	            System.out.println("Video clip created at " + output);
	            //exec returns 0 on successful call
	            return (exitVal == 0);
			} 
			catch (IOException | InterruptedException e1) 
			{
				System.out.println(e1.getMessage());
				return false;
			}
		}
	}

	//Getter-setters
	/**
	 * Get the media player associated with this VideoPlayer
	 */
	public EmbeddedMediaPlayer getPlayer()
	{
		return player;
	}
	
	/**
	 * Get the media player component for the Video Player
	 */
	public EmbeddedMediaPlayerComponent getPlayerComponent()
	{
		return mediaPlayerComponent;
	}

	/**
	 * Get the file path of the video being played in the player
	 */
	public String getVideoFilePath()
	{
		return videoPath;
	}
	
	/**
	 * Get the length of the playing video file
	 */
	public long getVideoLength()
	{
		return player.getLength();
	}

	//Bool conditions
	/**
	 * Determine if this player is loaded with a video
	 */
	public boolean hasVideo()
	{
		return hasMedia;
	}
	
	/**
	 * Determine if player has finished playing a video
	 */
	public boolean finishedPlaying()
	{
		return finishedPlaying;
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
		player.setTime(player.getLength());
	}

	@Override
	public void changePosition(long playbackPosition) 
	{
		seekVideo(playbackPosition);
	}	

	@Override
	public boolean open(String fileName)
	{
		return openVideo(fileName);
	}

	@Override
	public boolean clear() 
	{
		stopVideo();
		player.release();
		hasMedia = false;
		return hasMedia;
	}
	
	@Override
	public Component showView() 
	{
		Canvas c = mediaPlayerComponent.getVideoSurface();
		 
		
		return mediaPlayerComponent;
		//return mediaPlayerComponent.getVideoSurface();
	}
	
	public static void main(String[] args) throws InterruptedException, AWTException, FileNotFoundException, IOException
	{
		JFrame frame = new JFrame();
		VideoPlayer v = new VideoPlayer("media libraries/video/singing_dove.mp4");
		frame.setContentPane(v.getPlayerComponent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		v.playVideo();
	}

	@Override
	public boolean openFullFilePath(String fileName) {
		
		return false;
	}
}

