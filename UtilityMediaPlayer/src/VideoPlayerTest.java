import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class VideoPlayerTest 
{
	VideoPlayer testPlayer;
	String video1 = "media libraries/video/kaius_presentation.mp4";
	String video2 = "media libraries/video/WIN_20170227_19_51_17_Pro.mp4";
	
	@Before
	public void Before() 
	{
		testPlayer = new VideoPlayer();
	}
	
	@After
	public void After()
	{
		testPlayer.getPlayer().release();
		testPlayer.getFrame().dispose();
		testPlayer = null;
	}
	 
	
	//Testing playVideo method

	//Player shouldn't be able to play if there is no file loaded
	@Test
	public void testPlayVideoNoFile() throws InterruptedException 
	{
		testPlayer.showPlayer(); //player must be shown on a component before it can commence playback
		testPlayer.playVideo();
		
		Thread.sleep(1000); //have to sleep to give time for the video player to play before checking if its playing
		assertFalse(testPlayer.getPlayer().isPlaying());
	}

	
	//Player should play back video if there is one
	@Test
	public void testPlayVideoValidFile() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		testPlayer.showPlayer(); //player must be shown on a component before it can commence playback
		testPlayer.playVideo();
		
		Thread.sleep(1000); //have to sleep to give time for the video player to play before checking if its playing
		assertTrue(testPlayer.getPlayer().isPlaying());
	}
	
	//Player shouldn't be able to play back if null is passed in for a file path
	@Test
	public void testPlayVideoNullFile() throws InterruptedException
	{
		testPlayer.loadVideo(null);
		testPlayer.showPlayer();
		testPlayer.playVideo();
		
		Thread.sleep(1000);
		assertFalse(testPlayer.getPlayer().isPlaying());
	}
	
	//Testing pauseVideo method
	
	//Playback is paused when player is currently playing
	@Test
	public void testPauseWhilePlaying() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		testPlayer.showPlayer();
		testPlayer.playVideo();
		
		Thread.sleep(1500);
		testPlayer.pauseVideo();
		
		Thread.sleep(1000);
		assertFalse(testPlayer.getPlayer().isPlaying());
	}
	
	//Nothing should happen when trying to pause and there is no video loaded
	@Test
	public void testPauseNoVideo() throws InterruptedException
	{
		testPlayer.showPlayer();
		testPlayer.pauseVideo();
		
		assertFalse(testPlayer.getPlayer().isPlaying());
		assertFalse(testPlayer.hasVideo());
	}
	
	//Testing stopVideo method
	
	//Player should set playback back to beginning of video when stopped and should not be playing
	@Test
	public void testStopWhilePlaying() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		testPlayer.showPlayer();
		testPlayer.playVideo();
		
		Thread.sleep(2000);
		testPlayer.stopVideo();
		assertFalse(testPlayer.getPlayer().isPlaying());
		assertTrue(testPlayer.getPlayer().getPosition() < 0);
	}
	
	//Stopping when there is no video should not do anything
	@Test
	public void testStopNoVideo()
	{
		testPlayer.showPlayer();
		testPlayer.stopVideo();
		
		assertFalse(testPlayer.getPlayer().isPlayable());
	}
	
	//Stopping when paused should set playback to beginning of video
	@Test
	public void testStopWhilePaused() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		testPlayer.showPlayer();
		testPlayer.playVideo();
		
		Thread.sleep(1000);
		testPlayer.pauseVideo();
		testPlayer.stopVideo();
		
		assertTrue(testPlayer.getPlayer().getPosition() < 0);
	}
	
	//Testing changePosition
	
	//Playback should start from the specified position if it is valid when paused
	@Test
	public void testValidChangePositionWhilePaused() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		testPlayer.showPlayer();
		testPlayer.playVideo();
		Thread.sleep(1000);
		
		testPlayer.pauseVideo();
		float initPos = testPlayer.getPlayer().getPosition();
		
		testPlayer.changePosition((int)initPos + 5);
		
		assertTrue(testPlayer.getPlayer().getPosition() == (int)initPos + 5);
	}
	
	//Playback should start from the specified position if it is valid when playing
	@Test
	public void testValidChangePositionWhilePlaying()
	{
		testPlayer.loadVideo(video1);
		testPlayer.showPlayer();
		testPlayer.playVideo();
		
		float initPos = testPlayer.getPlayer().getPosition();
		testPlayer.changePosition((long)initPos + 200);
		assertTrue(testPlayer.getPlayer().getPosition() > initPos);
	}
}
