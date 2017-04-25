import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


public class VideoPlayer implements Player
{		
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer player;
	private final Integer EXTRACTION_AUDIO_BITRATE = new Integer(128000);
	private final Integer EXTRACTION_VIDEO_BITRATE = new Integer(39000);
	private final Integer EXTRACTION_CHANNELS = new Integer(2);
	private final Integer EXTRACTION_SAMPLING_RATE = new Integer(44100);
	private final Integer EXTRACTION_FRAMERATE = new Integer(15);
	
	private boolean hasMedia;
	private String videoPath;
	private Dimension vidDimension;

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
		
		vidDimension = null;
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
			hasMedia = true;
			//player.start(); //TODO
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
	 * Extract audio from the current video, and save it in the output folder using the input format
	 * @param format The supported audio file format to save the file as. Supported formats are MP3 and FLAC
	 */
	public boolean extractAudio(MusicPlayer.MusicFormat format)
	{
		if(!player.isPlayable())
			return false;
		else
		{
			String fileName = "extracted" + System.currentTimeMillis() + "." + format.toString();
			File audioFile = new File("output/" + fileName);
			File currentVideoFile = new File(getVideoFilePath());

			AudioAttributes audioAtt = new AudioAttributes();
			audioAtt.setCodec(format.getCodec());
			audioAtt.setBitRate(EXTRACTION_AUDIO_BITRATE);
			audioAtt.setChannels(EXTRACTION_CHANNELS);
			audioAtt.setSamplingRate(EXTRACTION_SAMPLING_RATE);

			EncodingAttributes encAtt = new EncodingAttributes();
			encAtt.setFormat(format.toString().toLowerCase());
			encAtt.setAudioAttributes(audioAtt);

			Encoder encoder = new Encoder();

			try 
			{
				System.out.println("Extracting audio, please wait....");
				encoder.encode(currentVideoFile, audioFile, encAtt);
			} 
			catch (IllegalArgumentException | EncoderException e) 
			{
				e.printStackTrace();
				return false;
			}
			System.out.println("Audio successfully extracted. File created at " + audioFile.getAbsolutePath());
			return true;
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
			ArrayList<BufferedImage> capturedImages = new ArrayList<BufferedImage>((int)(endTime-startTime));
			player.addMediaPlayerEventListener(new MediaPlayerEventAdapter()
					{
						@Override
						public void snapshotTaken(MediaPlayer mp, String fileName)
						{
							System.out.println("Snapshot successfully taken");
							System.out.println(capturedImages.size());
						}
					});
			pauseVideo();
			player.setTime(startTime);
			
			//TODO: GIFs are really slow and have low quality
			while(player.getTime() <= endTime)
			{
				BufferedImage image = player.getVideoSurfaceContents();
				capturedImages.add(image);
				player.setTime(player.getTime() + 5);
			}
			
			BufferedImage firstImage = capturedImages.get(0);
			ImageOutputStream output = new FileImageOutputStream(new File("output/clip.gif"));
			GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), 1, true);
			
			writer.writeToSequence(firstImage);
			for(int i = 1; i < capturedImages.size(); i++)
			{
				writer.writeToSequence(capturedImages.get(i));
			}
			
			writer.close();
			output.close();
			
			System.out.println(String.format("Images from %d to %d captured", startTime,endTime));
			
			return true;
		}
	}
	
	/**
	 * Make a clip of a video
	 * @param start Time to start clip from in seconds
	 * @param finish Time to end clip from in seconds
	 */
	public boolean clipVideo(int start, int finish, VideoFormat format)
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
			vidDimension = player.getVideoDimension();
			
			int dotIndex = videoPath.indexOf('.');
			String extension = videoPath.substring(dotIndex+1, videoPath.length());
			VideoFormat currentFormat = VideoFormat.valueOf(extension.toUpperCase());
			
			//String sout = ":sout=#transcode{vcodec=" + format.getVideoCodec() + ",vb=%d,scale=%f}:duplicate{dst=file{dst=%s}}";
			String sout = ":sout=#transcode{vcodec=" + currentFormat.getVideoCodec() + ",vb=%d,scale=%f}:duplicate{dst=file{dst=%s}}";

			int bits = 4096;
			float scale = 0.5f;
			long systemTime = System.currentTimeMillis();
			String dest = "output/clip" + systemTime + "." + currentFormat.toString();
			File orig = new File(dest);
			
			
			String finalSOut =  String.format(sout, bits, scale, dest);
			player.stop();
			player.playMedia(videoPath, ":start-time="+start,":stop-time=" + finish, finalSOut);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			File output = new File("output/converted." + format.toString());
			
			AudioAttributes audioAtt = new AudioAttributes();
			audioAtt.setCodec(format.getAudioCodec());
			audioAtt.setBitRate(EXTRACTION_AUDIO_BITRATE);
			audioAtt.setChannels(EXTRACTION_CHANNELS);
			audioAtt.setSamplingRate(EXTRACTION_SAMPLING_RATE);
			
			VideoAttributes videoAtt = new VideoAttributes();
			videoAtt.setCodec(format.getVideoCodec());
			videoAtt.setBitRate(EXTRACTION_VIDEO_BITRATE);
			videoAtt.setFrameRate(EXTRACTION_FRAMERATE);
			videoAtt.setSize(new VideoSize((int)vidDimension.getWidth(), (int)vidDimension.getHeight()));
			
			EncodingAttributes encAtt = new EncodingAttributes();
			encAtt.setFormat(format.toString().toLowerCase());
			encAtt.setAudioAttributes(audioAtt);
			encAtt.setVideoAttributes(videoAtt);
			encAtt.setDuration((float)(finish*1000 - start*1000));

			Encoder encoder = new Encoder();
			try 
			{
				encoder.encode(orig, output, encAtt);
			} 
			catch (IllegalArgumentException | EncoderException e ) 
			{
				e.printStackTrace();
			}
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
		//frame.dispose();
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
		VideoPlayer v = new VideoPlayer("media libraries/video/singing_dove.mp4");
		frame.setBounds(100, 100, 500, 500);

		frame.setContentPane(v.mediaPlayerComponent);
		frame.setVisible(true);
		v.playVideo();
		Thread.sleep(2000);
		v.clear();
	}
}

