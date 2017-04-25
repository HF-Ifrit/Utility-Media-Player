import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ImageViewerTest {
	
	/*
	@Test
	public void () {
		assertEquals(false, false);
	}
	*/
	
	//Provide JavaFX thread support for this JUnit test
	@Rule
	public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
	
	ImageViewer testViewer;
	
	String workingDir = System.getProperty("user.dir");
	String fileSep = System.getProperty("file.separator");
	String outputPath = workingDir + fileSep + "output";
	
	String imagePath;
	String gifPath;
	String badFormatPath;
	
	@Before
	public void Before() 
	{
		testViewer = new ImageViewer();
		
		imagePath = workingDir + fileSep + "media libraries" + fileSep + "images" + fileSep + "image.png";
		gifPath = workingDir + fileSep + "media libraries" + fileSep + "images" + fileSep + "gif.gif";
		badFormatPath = workingDir + fileSep + ".project";
	}

	@Test
	public void openNull() {
		assertEquals(false, testViewer.open(null));
	}
	
	@Test
	public void openWrongFormat() {
		assertEquals(false, testViewer.open(badFormatPath));
	}
	
	@Test
	public void openImage() {
		assertEquals(true, testViewer.open(imagePath));
	}
	
	@Test
	public void openGif() {
		assertEquals(true, testViewer.open(gifPath));
	}
	
	@Test
	public void getSceneUninitialized() {
		assertEquals(null, testViewer.getScene());
	}
	
	@Test
	public void getSceneInitialized() {
		testViewer.open(imagePath);
		
		assertNotNull(testViewer.getScene());
	}
	
	@Test
	public void rotateImageUninitialized() {
		assertFalse(testViewer.rotateImage(true));
		assertFalse(testViewer.rotateImage(false));
	}
	
	@Test
	public void rotateImageInitialized() {
		testViewer.open(imagePath);
		
		assertTrue(testViewer.rotateImage(true));
		
		testViewer.open(imagePath);
		
		assertTrue(testViewer.rotateImage(false));
		
		testViewer.open(gifPath);
		
		assertTrue(testViewer.rotateImage(true));
		
		testViewer.open(gifPath);
		
		assertTrue(testViewer.rotateImage(false));
	}
	
	@Test
	public void rotateMany() {
		testViewer.open(imagePath);
		assertTrue(testViewer.rotateImage(true));
		assertTrue(testViewer.rotateImage(true));
		assertTrue(testViewer.rotateImage(true));
		assertTrue(testViewer.rotateImage(true));
		assertTrue(testViewer.rotateImage(true));
	}
	
	@Test
	public void mirrorUninitialized() {
		assertFalse(testViewer.mirrorImage());
	}
	
	@Test
	public void mirrorInitialized() {
		testViewer.open(imagePath);
		assertTrue(testViewer.mirrorImage());
	}
	
	@Test
	public void clearUninitialized() {
		assertTrue(testViewer.clear());
		assertTrue(testViewer.clear());
		assertTrue(testViewer.clear());
		assertTrue(testViewer.clear());
		assertTrue(testViewer.clear());
	}
	
	@Test
	public void clearInitialized() {
		testViewer.open(imagePath);
		assertNotNull(testViewer.getScene());
		
		assertTrue(testViewer.clear());
		
		assertNull(testViewer.getScene());
	}
	
	@Test
	public void imageProperties() {
		testViewer.open(imagePath);
		testViewer.imageProperties();
	}
	
	@Test
	public void gifToMP4BadFormat() {
		testViewer.open(imagePath);
		assertFalse(testViewer.gifToVideo(VideoPlayer.VideoFormat.MP4));
	}

	@Test
	public void gifToMP4Good() {
		File outputFile = new File(outputPath);
		ArrayList<File> oldFiles = new ArrayList<File>(Arrays.asList(outputFile.listFiles()));
		
		testViewer.open(gifPath);
		assertTrue(testViewer.gifToVideo(VideoPlayer.VideoFormat.MP4));
		
		ArrayList<File> newFiles = new ArrayList<File>(Arrays.asList(outputFile.listFiles()));
		
		//cleanup all created files by getting all that were just added to output
		newFiles.removeAll(oldFiles);
		assertFalse(newFiles.isEmpty());
		
		for(File toDelete : newFiles) {
			try {
				Files.delete(toDelete.toPath());
			} catch (IOException e) {
				fail("Could not clean up after gifToMP4Good test");
			}
		}
	}
	
}
