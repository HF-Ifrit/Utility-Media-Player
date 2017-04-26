import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.After;
import org.junit.Test;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;



public class MusicPlayerTest {
	MusicPlayer testPlayer;	
	Stage mainStage;
	String workingDir = System.getProperty("user.dir");
	String fileSep = System.getProperty("file.separator");
	
	String testSong1 = workingDir + fileSep + "media libraries" + fileSep + "audio" + fileSep + "test.mp3";
	
	@Rule
	public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
	
	@Before
	public void before() {
		mainStage = new Stage();
		testPlayer = new MusicPlayer();
		testPlayer.start(mainStage);
	}
	
	@After
	public void after() {
		if (testPlayer != null) {
			testPlayer.clear();
			testPlayer = null;
		}
	}
	
	//Testing the open() method. The audio file should be loaded.
	@Test
	public void testValidFile() throws InterruptedException {
		testPlayer.open(testSong1);
		testPlayer.testButton.fire();
		assertTrue(testPlayer.songLoaded);
	}
	
	//Testing the open() method. This file cannot be loaded, as it is not a valid file type.
	public void testWrongFile() throws InterruptedException {
		testPlayer.open("media libraries/image.png");
		assertFalse(testPlayer.songLoaded);
	}

	
	@Test 
	//Testing the open() method. There is a null file being passed in, so nothing should happen.
	public void testNullFile() throws InterruptedException {
		assertFalse(testPlayer.open(null));
	}
	
	@Test
	//Testing the pause button
	public void testPause() {
		testPlayer.open(testSong1);
		testPlayer.testButton.fire();
		testPlayer.alternatePlayback();
		assertTrue(testPlayer.isPaused);
	}
	
	@Test
	//Testing the pause button multiple times
	public void testPauseMultiple() {
		testPlayer.open(testSong1);
		testPlayer.testButton.fire();
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
		testPlayer.volumeChange(20);
		assertTrue(testPlayer.player.getVolume() == 0.2);
	}
	
	@Test
	//Testing setting volume to negative value
	public void testSetVolumeNegative() {
		testPlayer.open(testSong1);
		double oldVol = testPlayer.player.getVolume();
		testPlayer.volumeChange(-0.5);
		assertTrue(testPlayer.player.getVolume() == oldVol - 0.5);
	}
	
	@Test 
	//Testing clear without opening a file first
	public void testClearEmpty() {
		assertFalse(testPlayer.clear());
	}
	
	@Test
	public void testClearOpen() {
		testPlayer.open(testSong1);
		testPlayer.testButton.fire();
		assertTrue(testPlayer.clear());		
	}
}
