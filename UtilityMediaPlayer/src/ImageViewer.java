import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
	
	//TODO: this needs to change
	boolean gifToVideo(VideoPlayer.VideoFormat format) {
		
		String filepath = currentFile.getAbsolutePath();
		
		//Intended format:
		//ffmpeg -i GIFPATH.GIF -movflags faststart -pix_fmt yuv420p -vf "scale=trunc(iw/2)*2:trunc(ih/2)*2" VIDEOPATH.MP4
		
		//export PATH=$PATH:/cygdrive/c/cygwin64/home/abelk_000/393/UtilityMediaPlayer/jars

		//proc = Runtime.getRuntime().exec(cmd, "PATH=$PATH:/android-sdk-linux_x86/platform-tools", fwrkDir);

		
		String ffmpegCommand = "ffmpeg -i "
				+ filepath
				+ " -movflags faststart -pix_fmt yuv420p -vf \"scale=trunc(iw/2)*2:trunc(ih/2)*2\" "
				+ outputPath;
		
		String[] envp = new String[1];
		envp[0] = "PATH=$PATH:" + workingDir + fileSep + "jars";
		
		//System.out.println(ffmpegCommand);
		
		System.out.println(envp[0]);
		
		try {
			Process proc = Runtime.getRuntime().exec("ffmpeg -h", envp);
			proc.waitFor();
			return true;
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
	
}