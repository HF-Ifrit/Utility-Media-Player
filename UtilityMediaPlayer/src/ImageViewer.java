 import javafx.application.Application;
 import javafx.geometry.Rectangle2D;
 import javafx.scene.Group;
 import javafx.scene.Scene; 
 import javafx.scene.image.Image;
 import javafx.scene.image.ImageView;
 import javafx.scene.layout.HBox;
 import javafx.scene.paint.Color;
 import javafx.stage.Stage; 

 public class ImageViewer extends Application {
 
     @Override public void start(Stage stage) {
         Image image = new Image("gif.gif");
 
         ImageView i = new ImageView();
         i.setImage(image);
         
         Group root = new Group();
         Scene scene = new Scene(root);
         scene.setFill(Color.BLACK);
         HBox box = new HBox();
         box.getChildren().add(i);
         root.getChildren().add(box);
 
         stage.setTitle("ImageView");
         stage.setWidth(415);
         stage.setHeight(200);
         stage.setScene(scene); 
         stage.sizeToScene(); 
         stage.show(); 
     }
     
     enum ImageFormat {
    	 JPG,
    	 PNG,
    	 GIF
     }

     public static void main(String[] args) {
         Application.launch(args);
     }
     
     
     ImageViewer() {
    	 
     }
     
     boolean open(String filename) {
    	 
     }
     
     Scene getScene() {
    	 
     }
     
     boolean rotateImage(boolean clockwise) {
    	 
     }
     
     boolean mirrorImage() {
    	 
     }
     
     boolean gifToVideo()
 }