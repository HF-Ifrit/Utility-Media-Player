import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.After;
import org.junit.Test;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;



public class MusicPlayerTest {
	MusicPlayer testPlayer;	
	String workingDir = System.getProperty("user.dir");
	String fileSep = System.getProperty("file.separator");
	
	String testSong1 = workingDir + fileSep + "media libraries" + fileSep + "test.mp3";
	
	@Rule
	public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
	
	@Before
	public void before() {
		testPlayer = new MusicPlayer();
	}
	
	//Testing the open() method. The audio file should be loaded, and its metadata displayed in the window.
	@Test
	public void testValidFile() throws InterruptedException {
		System.out.println(testSong1);	
		testPlayer.open(testSong1);
		assertTrue(testPlayer.playing);
	}
	
	//Testing the open() method. This file cannot be loaded, as it is not a valid file type.
	public void testWrongFile() throws InterruptedException {
		testPlayer.open("media libraries/image.png");
		assertFalse(testPlayer.songLoaded);
		testPlayer.open(testSong1);
	}
	
	@Test 
	//Testing the open() method. There is a null file being passed in, so nothing should happen.
	public void testNullFile() throws InterruptedException {
		assertFalse(testPlayer.open(null));
		testPlayer.open(testSong1);
	}
	
	@Test
	//Testing the pause button
	public void testPause() {
		testPlayer.open(testSong1);
		testPlayer.alternatePlayback();
		assertTrue(testPlayer.isPaused);
	}
	
	@Test
	//Testing the pause button multiple times
	public void testPauseMultiple() {
		testPlayer.open(testSong1);
		
		testPlayer.alternatePlayback();
		assertTrue(testPlayer.isPaused);
		testPlayer.alternatePlayback();
		assertFalse(testPlayer.isPaused);
		testPlayer.alternatePlayback();
		assertTrue(testPlayer.isPaused);
		testPlayer.alternatePlayback();
		assertFalse(testPlayer.isPaused);
	}
	
	@Test
	//Testing setting new volume to medium value
	public void testSetVolumeMedium() {
		
		testPlayer.open(testSong1);
		testPlayer.volumeChange(0.5);
		assertTrue(testPlayer.player.getVolume() == 0.5);
	}
	
	@Test
	//Testing setting volume to negative value
	public void testSetVolumeNegative() {
		testPlayer.open(testSong1);
		testPlayer.volumeChange(-0.5);
	}
	
	@Test
	//Testing clear without opening a file first
	public void testClearEmpty() {
		testPlayer.open(testSong1);
		assertTrue(testPlayer.clear());
		testPlayer.open(testSong1);
	}
	
	@Test
	public void testClearOpen() {
		testPlayer.open(testSong1);
		assertTrue(testPlayer.clear());
		testPlayer.open(testSong1);
	}
}
