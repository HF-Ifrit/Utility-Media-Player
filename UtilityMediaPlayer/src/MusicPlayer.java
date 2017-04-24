import java.awt.Component;
import java.io.*;

import javax.swing.JFrame;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.MapChangeListener;
import javafx.embed.swing.JFXPanel;
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
	boolean playing;
	MediaPlayer player;
	Slider volume;
	Slider time;
	Duration duration;
	Label playTime;
	Label songTitle;
	Scene mainScene;
	JFrame mainFrame;
	
	
	enum MusicFormat {
   	 MP3("libmp3lame"),
   	 FLAC("flac");
   	 
   	 private MusicFormat(String encoder)
   	 {
   		this.codec = encoder; 
   	 }
   	 
   	 private final String codec;
   	 
   	 public String getCodec()
   	 {
   		 return codec;
   	 }
    }
	
	/*The method that starts when the MusicPlayer is run. Will be delegated to a controller later in development;
	 * for now, this allows the MusicPlayer to function as a stand-alone application.(non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Music Player");
		
		player = null;
		songLoaded = false;
		volume = null;
		duration = null;
		playTime = null;
		mainFrame = new JFrame();
		JFXPanel panel = new JFXPanel();
		mainFrame.add(panel);
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(2,25,25,25));
		
		playTime = new Label();
		playTime.setPrefWidth(130);
		playTime.setMinWidth(50);
		grid.add(playTime, 1, 5);
		
		songTitle = new Label();
		songTitle.setPrefWidth(200);
		songTitle.setMinWidth(50);
		grid.add(songTitle, 0, 0);
		
		mainScene = new Scene(grid, 300, 300);
		
		//Create the play/pause button and add its event handler.
		Button play = makeButton("Play/Pause", 0, 8, grid);
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (songLoaded) {
					alternatePlayback(player);
				}
				else {
					open("media libraries/test.mp3");
					songLoaded = true;
					duration = player.getMedia().getDuration();
					
				}
			}
		});
		
		//Create the volume slider and add its event handler.
		volume = createSlider("Volume: ", null, 0, 4, grid);
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isValueChanging()) {
					volumeChange(player);
				}
			}
		});
		
		//Create the time slider and add its event handler.
		playTime = new Label("Time :");
		time = createSlider("Time: ", playTime, 0, 6, grid);
		time.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable o) {
				if (time.isValueChanging()) {
					changePosition(player);
				}
			}
		});
		panel.setScene(mainScene);
		panel.setVisible(true);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	/*Opens the specified music file and loads it into the player
	 * @param fileName The name of the desired music file
	 */
	private void openSong (String fileName) {
		if (fileName != null) {
			String uri = new File(fileName).toURI().toString();	
			if (uri.endsWith("mp3")) {
				Media media = new Media(uri);
				MediaPlayer musicPlayer = new MediaPlayer(media);
				this.player = musicPlayer;
				musicPlayer.setOnReady(new Runnable() {
					public void run() {
						media.getMetadata().addListener((MapChangeListener<String, Object>) change -> {
							if (change.wasAdded()) {
								setMetadata(change.getKey(), change.getValueAdded());
							}
						});
						player.setVolume(1.0);
	        			duration = media.getDuration();
	        			updateValues();
	        			songLoaded = true;
	        			playing = true;
	        			}
				});
			}
		}
	}
		
	
	//TODO Add album, artist, image?
	private void setMetadata(String key, Object value) {
		if (key.equals("title")) {
			songTitle.setText(value.toString());
		}
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
	
	/* Changes the volume of the audio track currently playing.
	 * @param musicPlayer The MediaPlayer associated with this current track.
	 */
	private void volumeChange(MediaPlayer musicPlayer) {
		player.setVolume(volume.getValue() / 100.0);
	}
	
	/* Provides playback seeking for the current track
	 * @param musicPlayer The MediaPlayer associated with this current track.
	 */
	private void changePosition(MediaPlayer musicPlayer) {
		if (player.getMedia().getDuration() != null) {
			player.seek(duration.multiply(time.getValue() / 100));
		}
		updateValues();
	}
	
	/*This helper method keeps the current time of the song being played updated. */
	protected void updateValues() {
		if (time != null && volume != null && playTime != null) {
			Platform.runLater(new Runnable() {
				public void run() {
					Duration currentTime = player.getCurrentTime();
					playTime.setText(formatTime(currentTime, duration));
					time.setDisable(duration.isUnknown());
					if (!time.isDisabled() && duration.greaterThan(Duration.ZERO) && !time.isValueChanging()) {
						double dur = currentTime.toMillis();
						double ation;
						if (dur <= 0) {
							ation = 0.0;
						}
						else {
							ation = currentTime.divide(dur).toMillis();
						}
						time.setValue(ation * 100.0);
					}
				}
			});
		}
	}
	
	/*This helper method displays the current time elapsed in the track. */
	private static String formatTime(Duration elapsed, Duration duration) {
		int intElapsed = (int) Math.floor(elapsed.toSeconds());
		int elapsedHours = intElapsed / (60 * 60);
		if (elapsedHours > 0) {
			intElapsed -= elapsedHours * 60 * 60;
		}
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

		if (duration.greaterThan(Duration.ZERO)) {
			int intDuration = (int) Math.floor(duration.toSeconds());
			int durationHours = intDuration / (60 * 60);
			if (durationHours > 0) {
				intDuration -= durationHours * 60 * 60;
			}
			int durationMinutes = intDuration / 60;
			int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
			if (durationHours > 0) {
				return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds,
						durationHours, durationMinutes, durationSeconds);
			} else {
				return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes,
						durationSeconds);
			}
		} else {
			if (elapsedHours > 0) {
				return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
			} else {
				return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
			}
		}
	}

	//TODO
	private void skipPlayback(MediaPlayer musicPlayer) {
		int z = 1;
	}
	
	
	/*The following are methods that serve as the implementation of the Player interface. They call the appropriate methods in the MusicPlayer class. */
	
	public void alternatePlayback() {
		alternatePlayback(player);
	}
	
	public boolean open(String fileName) {
		openSong(fileName);
		return songLoaded;
	}
	
	public void volumeChange(double newVolume) {
		if (newVolume >= 0) {
			player.setVolume(newVolume);
		}
		else if (newVolume < 0) {
			player.setVolume(player.getVolume() + newVolume);
		}
	}
	
	public void changePosition(long playbackPosition) {
		changePosition(player);
	}
	
	public void skipPlayback() {
		skipPlayback(player);
	}
	
	public boolean clear() {
		player.dispose();
		this.player = null;
		return true;
	}
	
	public Component showView() {
		return mainFrame;
	}
	
	
	/*
	Helper method that generates a Button and adds it to the grid. Used for setup.
	 * @param buttonName The text displayed on the button
	 * @param column The index of the column on the grid
	 * @param row The index of the row on the grid
	 * @param grid The GridPanel that the Button is being placed on
	 * @return A reference to the newly generated button */
	 
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
			slider = new Slider(0, 100, 100);
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
	
	
	
	public MusicPlayer() {
		player = null;
		songLoaded = false;
		volume = null;
		duration = null;
		playTime = null;
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(2,25,25,25));
		
		time = new Slider();
		HBox.setHgrow(time,Priority.ALWAYS);
		time.setMinWidth(50);
		time.setMaxWidth(Double.MAX_VALUE);
		grid.add(time, 0, 6);
		
		
		playTime = new Label();
		playTime.setPrefWidth(130);
		playTime.setMinWidth(50);
		grid.add(playTime, 1, 5);
		
		songTitle = new Label();
		songTitle.setPrefWidth(200);
		songTitle.setMinWidth(50);
		grid.add(songTitle, 0, 0);
		
		mainScene = new Scene(grid, 300, 300);
	}
	
	public static void main(String[] args) {
		Application.launch();
	}
}
