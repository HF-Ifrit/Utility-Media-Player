import java.io.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicPlayer extends Application implements Player {
	
	boolean songLoaded;
	boolean isPaused;
	MediaPlayer player;
	Slider volume;
	Slider time;
	Duration duration;
	Label playTime;
	
	/*The method that starts when the MusicPlayer is run. Will be delegated to a controller later in development;
	 * for now, this allows the MusicPlayer to function as a stand-alone application.(non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		player = null;
		songLoaded = false;
		volume = null;
		duration = null;
		playTime = null;
		primaryStage.setTitle("Music Player");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(2,25,25,25));
		
		//Create the play/pause button and add its event handler.
		Button play = makeButton("Play/Pause", 2, 2, grid);
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
					duration = player.getMedia().getDuration();
					
				}
			}
		});
		
		//Create the volume slider and add its event handler.
		volume = createSlider("Volume: ", null, 1, 4, grid);
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isValueChanging()) {
					volumeChange(player);
				}
			}
		});
		
		time = new Slider();
		HBox.setHgrow(time,Priority.ALWAYS);
		time.setMinWidth(50);
		time.setMaxWidth(Double.MAX_VALUE);
		grid.add(time, 1, 7);
		
		
		playTime = new Label();
		playTime.setPrefWidth(130);
		playTime.setMinWidth(50);
		grid.add(playTime, 1, 5);
		
		//All of the stuff commented out here relates to the (currently non-functioning) playback slider. Will
		//come back to at a later date.
//		
//		ChangeListener listener = new ChangeListener() {
//			 public void changed(ObservableValue o, Object oldValue, Object newValue) {
//	                updateValues();
//			 }
//		};
//		 player.currentTimeProperty().addListener(listener);
		
		//Create the time slider and add its event handler.
//		playTime = new Label("Time :");
//		time = createSlider("Time: ", playTime, 1, 6, grid);
//		InvalidationListener listener = new InvalidationListener() {
//			public void invalidated(Observable ov) {
//				updateValues();
//			}
//		};
//		player.currentTimeProperty().addListener(listener); 
		
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
		updateValues();
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
		if (player.getMedia().getDuration() != null) {
			player.seek(duration.multiply(time.getValue() / 100.0));
		}
		updateValues();
	}
	
	//TODO
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
	
	private Slider createSlider(String name, Label preLabel, int column, int row, GridPane grid) {
		Label label = null;
		Slider slider = null;
		if (preLabel == null) {
			label = new Label(name);
		}
		else {
			label = preLabel;
		}
		if (name.equals("Volume: ")) { 
			slider = new Slider(0, 1, 1);
			slider.setPrefWidth(70);
			slider.setMaxWidth(Region.USE_PREF_SIZE);
			slider.setMinWidth(30);
		}
		else if (name.equals("Time: ")) {
			slider = new Slider();
			HBox.setHgrow(slider, Priority.ALWAYS);
			slider.setMinWidth(50);
			slider.setMaxWidth(Double.MAX_VALUE);
			slider.valueProperty().addListener(new InvalidationListener() {
			    public void invalidated(Observable ov) {
			       if (time.isValueChanging()) {
			    	   changePosition(player);
			       }
			    }
			});
		}
		else {
			slider = null;
		}
		grid.add(label, column, row - 1);
		grid.add(slider, column, row);
		return slider;
	}
	
	/*This helper method keeps the current time of the song being played updated. */
	protected void updateValues() {
		if (time != null && volume != null && playTime != null) {
			Platform.runLater(new Runnable() {
				@SuppressWarnings("deprecation")
				public void run() {
					Duration currentTime = player.getCurrentTime();
					playTime.setText(formatTime(currentTime, duration));
					time.setDisable(duration.isUnknown());
					if (!time.isDisabled() && duration.greaterThan(Duration.ZERO) && !time.isValueChanging()) {
						time.setValue(currentTime.divide(duration).toMillis() * 100.0);
					}
				}
			});
		}
	}
	
	private static String formatTime(Duration elapsed, Duration duration) {
		   int intElapsed = (int)Math.floor(elapsed.toSeconds());
		   int elapsedHours = intElapsed / (60 * 60);
		   if (elapsedHours > 0) {
		       intElapsed -= elapsedHours * 60 * 60;
		   }
		   int elapsedMinutes = intElapsed / 60;
		   int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 
		                           - elapsedMinutes * 60;
		 
		   if (duration.greaterThan(Duration.ZERO)) {
		      int intDuration = (int)Math.floor(duration.toSeconds());
		      int durationHours = intDuration / (60 * 60);
		      if (durationHours > 0) {
		         intDuration -= durationHours * 60 * 60;
		      }
		      int durationMinutes = intDuration / 60;
		      int durationSeconds = intDuration - durationHours * 60 * 60 - 
		          durationMinutes * 60;
		      if (durationHours > 0) {
		         return String.format("%d:%02d:%02d/%d:%02d:%02d", 
		            elapsedHours, elapsedMinutes, elapsedSeconds,
		            durationHours, durationMinutes, durationSeconds);
		      } else {
		          return String.format("%02d:%02d/%02d:%02d",
		            elapsedMinutes, elapsedSeconds,durationMinutes, 
		                durationSeconds);
		      }
		      } else {
		          if (elapsedHours > 0) {
		             return String.format("%d:%02d:%02d", elapsedHours, 
		                    elapsedMinutes, elapsedSeconds);
		            } else {
		                return String.format("%02d:%02d",elapsedMinutes, 
		                    elapsedSeconds);
		            }
		        }
		    }
	
	public static void main(String[] args) {
		/*new javafx.embed.swing.JFXPanel();
		open("C:/Users/Danny/workspace/UtilityMediaPlayer/media libraries/test.mp3");
		System.out.println("Finished.");
		*/
		launch(args);
	}
}
