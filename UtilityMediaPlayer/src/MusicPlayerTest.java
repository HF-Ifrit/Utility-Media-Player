import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;


public class MusicPlayerTest {
	MusicPlayer testPlayer;
	String testSong1 = "media libraries/test.mp3";
	
	@Before
	public void before() {
		testPlayer = new MusicPlayer();
	}
	
	@After
	public void after() {
		testPlayer.clear();
		testPlayer = null;
	}
	
	//Testing the open() method. The audio file should be loaded, and its metadata displayed in the window.
	@Test
	public void testValidFile() {
		testPlayer.open(testSong1);
	}
}
