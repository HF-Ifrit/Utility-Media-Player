
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class HBoxBuilder extends HBox{
	private static double SPACING = 25;
	private static double LEFT_EDGE = 125;
	private static double FIT_VALUE = 40;
	
	protected DoubleProperty prefTileSize = new SimpleDoubleProperty(SPACING);
	protected MainFrame frame;
	
	final static Label volumeCaption = new Label("Volume:");
	
	private HBoxBuilder(MainFrame newFrame){
		this.frame = newFrame;
	}
	
    //helper method that adds the HBox Pane to the JFXPanel
    public static HBox newHBoxBar(MainFrame newFrame){
    	Button currentButton;
    	Image currentImage;
    	ImageView imageview;
    	JFXController jfxControl = newFrame.getJFXController();
    	
    	// create a color swatch.
    	final GridPane swatch = new GridPane();
    	
    	//initializes hbox
    	HBoxBuilder hbox = new HBoxBuilder(newFrame);
    	hbox.setHeight(70);
    	MainFrame frame = hbox.frame;
    	//helps control bounds of the window
        //hbox.setPadding(new Insets(15, 12, 15, 400));
        hbox.setSpacing(30);
        
        
        //adds back button
        currentImage = new Image(frame.getClass().getResourceAsStream("/internaldata/Rewind.png"));
    	currentButton = new Button();
    	imageview = new ImageView(currentImage);
    	imageview.setFitHeight(FIT_VALUE);
    	imageview.setFitWidth(FIT_VALUE);
    	currentButton.setGraphic(imageview);
    	currentButton.setOnAction(jfxControl.new backFile());
    	currentButton.setAccessibleRoleDescription("Rewind");
   

    	swatch.getChildren().add(currentButton);
    	
        
        //adds PlayButton image and button
    	Image playImage = new Image(frame.getClass().getResourceAsStream("/internaldata/Play.png"));
    	Button playButtonTemp = new Button();
    	imageview = new ImageView(playImage);
    	imageview.setFitHeight(FIT_VALUE);
    	imageview.setFitWidth(FIT_VALUE);
    	playButtonTemp.setGraphic(imageview);
    	playButtonTemp.setOnAction(jfxControl.new playButton());
    	playButtonTemp.setAccessibleRoleDescription("Play");
    	frame.setPlayButton(playButtonTemp);
    	
    	swatch.getChildren().add(playButtonTemp);
    	
    	
    	
    	//add forward button
    	currentImage = new Image(frame.getClass().getResourceAsStream("/internaldata/Forward.png"));
     	currentButton = new Button();
     	imageview = new ImageView(currentImage);
     	imageview.setFitHeight(FIT_VALUE);
     	imageview.setFitWidth(FIT_VALUE);
     	currentButton.setGraphic(imageview);
     	currentButton.setAccessibleRoleDescription("Forward");
     	currentButton.setOnAction(jfxControl.new forwardFile());

     	swatch.getChildren().add(currentButton);
     	
     	
     /**
     	Slider slider = new Slider(0, 100, 100);
		slider.setPrefWidth(300);
		slider.setMaxWidth(300);
		slider.setMinWidth(30);
		slider.setAccessibleRoleDescription("Spacing");
		
		
		slider.setVisible(false);
		swatch.getChildren().add(slider);
		*/
     	
		//add volume text
     	volumeCaption.setTextFill(Color.BLACK);
        GridPane.setConstraints(volumeCaption, 0, 0);
        swatch.getChildren().add(volumeCaption);
		
		//add volume control slider
		final Slider volume = new Slider(0, 100, 100);
		volume.setPrefWidth(200);
		volume.setMaxWidth(300);
		volume.setMinWidth(100);
		volume.setAccessibleRoleDescription("Volume");
		
		//responds to slider movements
		/*
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isValueChanging()) {
					currentPlayer.volumeChange((int) volume.getValue());
				}
			}
		});
		*/
		
		swatch.getChildren().add(volume);
    	
		hbox.getChildren().addAll(swatch);
		HBox.setHgrow(swatch, Priority.ALWAYS);
		
		//modifies button sizes and locations based on 
		hbox.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds, Bounds newBounds) {
				
				
				hbox.prefTileSize.set(Math.max(SPACING, hbox.getWidth()));
				double edge = SPACING + LEFT_EDGE;
				for (Node child : swatch.getChildrenUnmodifiable()) {
					Control tile = (Control) child;
					if(!(child instanceof Label)){
							
						
						final double margin = ( hbox.prefTileSize.get() - LEFT_EDGE)/ swatch.getChildrenUnmodifiable().size();
						tile.setPrefWidth(margin - SPACING);
						
						GridPane.setMargin(child, new Insets(15, edge, 15, edge));
						edge += margin;
						
						if(child instanceof Slider){
							((Slider) child).setMinWidth(margin - SPACING);
							
						}
					}
					else{
						GridPane.setMargin(child, new Insets(15, edge, 15, edge));
						edge += 50;
					}
				}
			}
		});
    	
    	return hbox;
    }
}
