import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


public class VideoPlayer implements Player
{		
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer player;
	
	private final Integer FFMPEG_GIF_PARAMETERS = new Integer(12);
	private final Integer FFMPEG_EXTRACT_AUDIO_PARAMETERS = new Integer(7);
	private final Integer FFMPEG_CROP_AUDIO_PARAMETERS = new Integer(10);
	
	private boolean hasMedia;
	private String videoPath;
	
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

	//Constructors
	public VideoPlayer()
	{
		new NativeDiscovery().discover();
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		
		hasMedia = false;
		player = mediaPlayerComponent.getMediaPlayer();	
	}

	public VideoPlayer(String filePath)
	{
		this();
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
		if(player.isPlaying())
			return true;
		else
			return false;
	}

	/**
	 * Reveals the video player frame
	 */
	private void showPlayer()
	{	
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
		if(player.isSeekable() && time > 0.0 && time < player.getLength())
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
			
			if(startTime == endTime)
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
	public boolean gifClip(long startTime, long endTime) throws IOException
	{
		if(!hasMedia
				|| startTime < 0 
				|| startTime > player.getLength() 
				|| endTime < 0 
				|| endTime > player.getLength()
				|| startTime > endTime)
			return false;
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
				|| start < 0 
				|| start > player.getLength() 
				|| finish < 0 
				|| finish > player.getLength()
				|| start > finish)
		{
			return false;
		}
		else
		{			
			int dotIndex = videoPath.indexOf('.');
			String extension = videoPath.substring(dotIndex+1, videoPath.length());
			VideoFormat currentFormat = VideoFormat.valueOf(extension.toUpperCase());
			
			//String sout = ":sout=#transcode{vcodec=" + format.getVideoCodec() + ",vb=%d,scale=%f}:duplicate{dst=file{dst=%s}}";
			String sout = ":sout=#transcode{vcodec=" + currentFormat.getVideoCodec() + ",vb=%d,scale=%f}:duplicate{dst=file{dst=%s}}";

			int bits = 4096;
			float scale = 0.5f;
			long systemTime = System.currentTimeMillis();
			String dest = "output/clip" + systemTime + "." + currentFormat.toString();
			
			String finalSOut =  String.format(sout, bits, scale, dest);
			player.stop();
			try
			{
				Thread.sleep(2000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			player.playMedia(videoPath, ":start-time="+start,":stop-time=" + finish, finalSOut);

			System.out.println("Video clipped at " + dest);
			return true;
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
		return mediaPlayerComponent.getVideoSurface();
	}
	
	public static void main(String[] args) throws InterruptedException, AWTException, FileNotFoundException, IOException
	{
		JFrame frame = new JFrame("Video Player");
		VideoPlayer v = new VideoPlayer("media libraries/video/kaius_presentation.mp4");
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(v.mediaPlayerComponent);
		frame.setVisible(true);
		v.playVideo();
		Thread.sleep(1000);
		v.extractAudio(MusicPlayer.MusicFormat.MP3);
	}
}

