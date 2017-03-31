import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

	private static final double CLOCKWISE = 90;
	private static final double COUNTERCLOCKWISE = 270;

	
	
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

	//TODO: implement this
	boolean gifToVideo(VideoPlayer.VideoFormat format) {
		return false;
	}

	boolean clear() {
		mainScene = null;
		currentIV = null;
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