import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.After;
import org.junit.Test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;



public class MusicPlayerTest {
	MusicPlayer testPlayer;
	String testSong1 = "media libraries/test.mp3";
	@Rule
	public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
	
	@Before
	public void before() {
		testPlayer = new MusicPlayer();
	}
	
	@After
//	public void after() {
//		testPlayer.clear();
//		testPlayer = null;
//	}
	
	@Test
	public void testTest() {
		int a = 1;
		assertTrue(a == 1);
	}
	
	//Testing the open() method. The audio file should be loaded, and its metadata displayed in the window.
	@Test
	public void testValidFile() throws InterruptedException {
		System.out.println(testPlayer.open(testSong1));
		System.out.println(testPlayer.player.getStatus());
		
		assertTrue(testPlayer.playing);
	}
	
	@Test (expected = MediaException.class)
	//Testing the open() method. This file cannot be loaded, as it is not a valid file type.
	public void testWrongFile() throws InterruptedException {
		testPlayer.open("media libraries/image.png");
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
		testPlayer.alternatePlayback();
		assertTrue(testPlayer.isPaused);
	}
}
