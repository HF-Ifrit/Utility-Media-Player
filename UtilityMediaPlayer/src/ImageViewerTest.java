import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ImageViewerTest {
	
	ImageViewer testViewer;
	
	String workingDir = System.getProperty("user.dir");
	String fileSep = System.getProperty("file.separator");
	
	String video1 = "media libraries/video/kaius_presentation.mp4";
	String video2 = "media libraries/video/WIN_20170227_19_51_17_Pro.mp4";
	
	String imagePath;
	String gifPath;
	
	@Before
	public void Before() 
	{
		testViewer = new ImageViewer();
		
		imagePath = workingDir + fileSep + "media libraries" + fileSep + "images" + fileSep + "image.png";
		gifPath = workingDir + fileSep + "media libraries" + fileSep + "images" + fileSep + "gif.gif";
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
