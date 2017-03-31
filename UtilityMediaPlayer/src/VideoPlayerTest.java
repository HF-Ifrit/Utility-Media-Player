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
	public void After()	{
		testPlayer.getPlayer().release();
		testPlayer.getFrame().dispose();
		testPlayer = null;
	}
	 
	
	//Testing playVideo method

	//Player shouldn't be able to play if there is no file loaded
	@Test
	public void testPlayVideoNoFile() throws InterruptedException 
	{
		 //player must be shown on a component before it can commence playback
		testPlayer.playVideo();
		
		Thread.sleep(1000); //have to sleep to give time for the video player to play before checking if its playing
		assertFalse(testPlayer.getPlayer().isPlaying());
	}

	
	//Player should play back video if there is one
	@Test
	public void testPlayVideoValidFile() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		 //player must be shown on a component before it can commence playback
		testPlayer.playVideo();
		
		Thread.sleep(1000); //have to sleep to give time for the video player to play before checking if its playing
		assertTrue(testPlayer.getPlayer().isPlaying());
	}
	
	//Player shouldn't be able to play back if null is passed in for a file path
	@Test
	public void testPlayVideoNullFile() throws InterruptedException
	{
		testPlayer.loadVideo(null);
		
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
		
		testPlayer.playVideo();
		
		Thread.sleep(2000);
		testPlayer.stopVideo();
		assertFalse(testPlayer.getPlayer().isPlaying());
		assertTrue(testPlayer.getPlayer().getTime() < 0);
	}
	
	//Stopping when there is no video should not do anything
	@Test
	public void testStopNoVideo()
	{
		
		testPlayer.stopVideo();
		
		assertFalse(testPlayer.getPlayer().isPlayable());
	}
	
	//Stopping when paused should set playback to beginning of video
	@Test
	public void testStopWhilePaused() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		
		testPlayer.playVideo();
		
		Thread.sleep(1000);
		testPlayer.pauseVideo();
		testPlayer.stopVideo();
		
		assertTrue(testPlayer.getPlayer().getTime() < 0);
	}
	
	//Testing changePosition
	
	//Playback should start from the specified position if it is valid when paused
	@Test
	public void testValidChangePositionWhilePaused() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		
		testPlayer.playVideo();
		Thread.sleep(1000);
		
		testPlayer.pauseVideo();
		float initPos = testPlayer.getPlayer().getTime();
		
		testPlayer.changePosition((int)initPos + 5);
		
		assertTrue(testPlayer.getPlayer().getTime() == (int)initPos + 5);
	}
	
	//Playback should start from the specified position if it is valid when playing
	@Test
	public void testValidChangePositionWhilePlaying() throws InterruptedException
	{
		testPlayer.loadVideo(video1);
		
		testPlayer.playVideo();
		
		Thread.sleep(1000);
		float initPos = testPlayer.getPlayer().getTime();
		testPlayer.changePosition((long)initPos + 200);
		testPlayer.pauseVideo();
		assertTrue(testPlayer.getPlayer().getTime() > initPos);
	}
	
	//Player should have position moved even if video has only been loaded before changing
	@Test
	public void testChangePositionAfterLoad()
	{
		
		testPlayer.loadVideo(video1);

		long targetTime = 1000;
		testPlayer.changePosition(targetTime);
		
		System.out.println(testPlayer.getPlayer().getTime());
		assertTrue(testPlayer.getPlayer().getTime() == targetTime);
	}
	
	/*
	 * Nothing should happen when trying to change the position of the player to a value outside of the bounds for its duration
	 * Case: Long.MAX_VALUE
	 */
	@Test
	public void testChangePositionMAXVALUE()
	{
		testPlayer.loadVideo(video1);
		
		
		float initPos = testPlayer.getPlayer().getTime();
		testPlayer.changePosition(Long.MAX_VALUE);
		
		assertTrue(testPlayer.getPlayer().getTime() == initPos);
	}
	
	/*
	 * Nothing should happen when trying to change the position of the player to a value outs
	 * Case: Long.MIN_VALUE
	 */
	@Test
	public void testChangePositionMINVALUE()
	{
		testPlayer.loadVideo(video1);
		
		
		float initPos = testPlayer.getPlayer().getTime();
		testPlayer.changePosition(Long.MIN_VALUE);
		
		assertTrue(testPlayer.getPlayer().getTime() == initPos);
	}
	
	//Player should be unable to change position if there is no video
	@Test
	public void testChangePositionNoVideo()
	{
		float initPos = testPlayer.getPlayer().getTime();
		
		long attemptedChangeTime = 100;
		testPlayer.changePosition(attemptedChangeTime);
		
		assertTrue(testPlayer.getPlayer().getTime() == initPos);
		assertFalse(testPlayer.getPlayer().isSeekable());
	}
	
	
	//Test captureScreen
	
	
}
