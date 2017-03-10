import java.io.*;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MusicPlayer extends Application implements Player {
	
	boolean songLoaded;
	boolean isPaused;
	MediaPlayer player;
	Slider volume;
	
	/*The method that starts when the MusicPlayer is run. Will be delegated to a controller later in development;
	 * for now, this allows the MusicPlayer to function as a stand-alone application.(non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		player = null;
		songLoaded = false;
		volume = null;
		primaryStage.setTitle("Music Player");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(2,25,25,25));
		
		Button play = makeButton("Play/Pause", 1, 4, grid);
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (songLoaded) {
					alternatePlayback(player);
				}
				else {
					System.out.println("Song is playing.");
					open("media libraries/test.mp3");
					songLoaded = true;
				}
			}
		});
		
		volume = createSlider("Volume: ", 2, 2, grid);
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isValueChanging()) {
					volumeChange(player);
				}
			}
		});
		
		primaryStage.setScene(new Scene(grid, 300, 275));
		primaryStage.show();
	}
	@Override
	/*Opens the specified music file and loads it into the player
	 * @param fileName The name of the desired music file
	 */
	public void open(String fileName) {
		String uri = new File(fileName).toURI().toString();	
		Media media = new Media(uri);
		MediaPlayer musicPlayer = new MediaPlayer(media);
		this.player = musicPlayer;
		player.setVolume(1.0);
		musicPlayer.play();
		}
	
	/*Called when the Play/Pause button is pressed. Alternates the current state of the playing track
	 * from 'playing' to 'paused' and vice versa.
	 * @param musicPlayer The MediaPlayer associated with the current track
	 */
	private void alternatePlayback(MediaPlayer musicPlayer) {
		if (songLoaded) {
			if (isPaused) {
				musicPlayer.play();
				isPaused = false;
			}
			else {
				musicPlayer.pause();
				isPaused = true;
			}
		}
	}
	
	private void volumeChange(MediaPlayer musicPlayer) {
		player.setVolume(volume.getValue());
	}
	
	private void changePosition(MediaPlayer musicPlayer) {
		int y = 1;
	}
	
	private void skipPlayback(MediaPlayer musicPlayer) {
		int z = 1;
	}
	
	/*The following are methods that serve as the implementation of the Player interface. They call the appropriate methods in the MusicPlayer class. */
	
	public void alternatePlayback() {
		alternatePlayback(player);
	}
	
	public void volumeChange() {
		volumeChange(player);
	}
	
	public void changePosition() {
		changePosition(player);
	}
	
	public void skipPlayback() {
		skipPlayback(player);
	}

	
	
	/*Helper method that generates a Button and adds it to the grid. Used for setup.
	 * @param buttonName The text displayed on the button
	 * @param column The index of the column on the grid
	 * @param row The index of the row on the grid
	 * @param grid The GridPanel that the Button is being placed on
	 * @return A reference to the newly generated button
	 */
	private Button makeButton(String buttonName, int column, int row, GridPane grid) {
		HBox box = new HBox(10);
		Button button = new Button();
		button.setText(buttonName);
		box.getChildren().add(button);
		grid.add(box,  column, row);
		return button;
	}
	
	private Slider createSlider(String name,int column, int row, GridPane grid) {
		Slider volumeSlider = new Slider(0, 1, 1);
		Label volumeLabel = new Label(name);
		grid.add(volumeLabel, column, row - 1);
		volumeSlider.setPrefWidth(70);
		volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
		volumeSlider.setMinWidth(30);
		grid.add(volumeSlider, column, row);
		return volumeSlider;
	}
	
	public static void main(String[] args) {
		/*new javafx.embed.swing.JFXPanel();
		open("C:/Users/Danny/workspace/UtilityMediaPlayer/media libraries/test.mp3");
		System.out.println("Finished.");
		*/
		launch(args);
	}
}
