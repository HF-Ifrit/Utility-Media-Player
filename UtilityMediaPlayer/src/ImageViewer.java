import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ImageViewer {

	private Scene mainScene;
	private ImageView currentIV;
	private boolean openImage = false;
	private File currentFile;
	private boolean test = false;

	private static final double CLOCKWISE = 90;
	private static final double COUNTERCLOCKWISE = 270;
	private static final int GIF_TO_VIDEO_ARGUMENTS = 10;
	
	String workingDir = System.getProperty("user.dir");
	String fileSep = System.getProperty("file.separator");
	String outputPath = workingDir + fileSep + "output";
	
	ImageViewer(boolean testing) {
		if(testing) {
			this.test = true;
		}
	}
	
	public ImageViewer() {
		
	}
	
	enum ImageFormat {
		JPG, PNG, GIF
	}

	boolean open(String pathname) {
		if(pathname == null) {
			return false;
		}
		
		File f = new File(pathname);
		InputStream fileStream;
		try {
			fileStream = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			return false;
		}
		
		Image image = new Image(fileStream);

		if (image.isError()) {
			return false;
		}

		ImageView iv = new ImageView();
		iv.setImage(image);

		Group root = new Group();
		Scene scene = new Scene(root);
		scene.setFill(Color.BLACK);
		HBox box = new HBox();
		box.getChildren().add(iv);
		root.getChildren().add(box);

		openImage = true;
		currentIV = iv;
		mainScene = scene;
		currentFile = f;

		return true;
	}

	Scene getScene() {
		return this.mainScene;
	}

	boolean rotateImage(boolean clockwise) {

		if (openImage == false) {
			return false;
		}

		double rotation = currentIV.getRotate();

		if (clockwise) {
			rotation += CLOCKWISE;
		}

		else {
			rotation += COUNTERCLOCKWISE;
		}

		// Adjust result to be in the range [0, 360).
		rotation = rotation % 360.0;

		currentIV.setRotate(rotation);

		Group root = new Group();
		Scene scene = new Scene(root);
		scene.setFill(Color.WHITE);
		HBox box = new HBox();
		box.getChildren().add(currentIV);
		root.getChildren().add(box);

		mainScene = scene;

		return true;
	}

	boolean mirrorImage() {

		if (openImage == false) {
			return false;
		}

		// Keep the current X scaling, but reverse it with respect to the axis
		double newScale = currentIV.getScaleX() * -1;

		currentIV.setScaleX(newScale);

		Group root = new Group();
		Scene scene = new Scene(root);
		scene.setFill(Color.BLACK);
		HBox box = new HBox();
		box.getChildren().add(currentIV);
		root.getChildren().add(box);

		mainScene = scene;

		return true;
	}
	
	//Flip the image across the horizontal axis.
	boolean mirrorImageVertically() {
		if(openImage == false) {
			return false;
		}
		
		else {
			//Rotating an image 180 degrees, then mirroring it across the vertical axis,
			//is equivalent to mirroring it across the horizontal axis.
			rotateImage(true);
			rotateImage(true);
			mirrorImage();
			return true;
		}
	}
	
	//Opens a new JOptionPane that displays basic image properties, iff an image is currently open.
	void imageProperties() {
		if(openImage == false) {
			return;
		}
		
		else {
			String currentPath = currentFile.getAbsolutePath();
			String height = "" + currentIV.getImage().getHeight();
			String width = "" + currentIV.getImage().getWidth();
			String properties = "Path: " + currentPath + "\n"
					+ "Height (px): " + height + "\n"
					+ "Width (px): " + width;
	        
	        Thread t1 = new Thread(new Runnable() {
	        	public void run() {
	    	        JOptionPane.showMessageDialog(null, properties,"Image Properties", JOptionPane.INFORMATION_MESSAGE);
	    	        if(test) {
	                    JOptionPane.getRootFrame().dispose();

	    	        }
	        	}
	        });
	        t1.start();
		}
	}
	
	boolean gifToVideo() {
		
		if(openImage == false) {
			return false;
		}
		
		String fileName = currentFile.getName();
		if( ! fileName.endsWith(".gif")) {
			return false;
		}
		
		String filepath = currentFile.getAbsolutePath();
		String ffmpegPath = workingDir + fileSep + "jars" + fileSep + "ffmpeg.exe";
		
		//Intended format:
		//"FFMPEGPATH.EXE -i GIFPATH.GIF -movflags faststart -pix_fmt yuv420p -vf "scale=trunc(iw/2)*2:trunc(ih/2)*2" VIDEOPATH.MP4"
		
		String[] ffmpegCommand = new String[GIF_TO_VIDEO_ARGUMENTS];
		
		ffmpegCommand[0] = (ffmpegPath);
		ffmpegCommand[1] = ("-i");
		ffmpegCommand[2] = (filepath);
		ffmpegCommand[3] = ("-movflags");
		ffmpegCommand[4] = ("faststart");
		ffmpegCommand[5] = ("-pix_fmt");
		ffmpegCommand[6] = ("yuv420p");
		ffmpegCommand[7] = ("-vf");
		ffmpegCommand[8] = ("\"scale=trunc(iw/2)*2:trunc(ih/2)*2\"");
		ffmpegCommand[9] = (outputPath + fileSep + "new_video_" + System.currentTimeMillis() + ".mp4");
		
		//StreamGobbler code is, as mentioned below, taken from https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
		try {
			Process proc = Runtime.getRuntime().exec((String[]) ffmpegCommand);
			
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
            
            //exec returns 0 on successful call
            return (exitVal == 0);
		} catch (IOException | InterruptedException e1) {
			System.out.println(e1.getMessage());
			return false;
		}
	}

	boolean clear() {
		mainScene = null;
		currentIV = null;
		currentFile = null;
		openImage = false;
		return true;
	}
	
	
	//TODO: manual testing package; requires ImageViewer to extend Application
/*	String workingDir = System.getProperty("user.dir");
	String fileSep = System.getProperty("file.separator");
	
	String gifPath = workingDir + fileSep + "media libraries" + fileSep + "images" + fileSep + "gif.gif";
	
	@Override
	public void start(Stage stage) {
		
		open(gifPath);
		
		rotateImage(false);
		
		stage.setTitle("ImageView");
		stage.setWidth(600);
		stage.setHeight(800);
		stage.setScene(mainScene); 
		stage.sizeToScene(); 
		stage.show(); 
	}
	
	public static void main(String[] args) {
	Application.launch();
}*/
	
	//This class taken from https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
	class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
	    
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	    }
	    
	    public void run()
	    {
	        try
	        {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            while ( (line = br.readLine()) != null)
	            	;
	                //System.out.println(type + ">" + line);    
	            } catch (IOException ioe)
	              {
	                ioe.printStackTrace();  
	              }
	    }
	}
	
}