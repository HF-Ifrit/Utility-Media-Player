import java.awt.Component;
import java.io.*;


import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicPlayer implements Player {
	
	boolean songLoaded;
	boolean isPaused;
	boolean playing;
	MainFrame frame;
	MediaPlayer player;
	Slider volume;
	Slider time;
	Duration duration;
	Text playTime;
	Text songTitle;
	Text albumTitle;
	Text artist;
	ImageView albumImage;
	Scene mainScene;
	JFXPanel mainFrame;
	public Button testButton;
	
	
	enum MusicFormat {
   	 MP3("libmp3lame"),
   	 WAV("lpcm");
   	 
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
	
//	/*The method that starts when the MusicPlayer is run. Used for testing purposes/if implementing as Application.
//	 * @see javafx.application.Application#start(javafx.stage.Stage)
//	 */
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Music Player");
		
		player = null;
		songLoaded = false;
		volume = null;
		duration = null;
		playTime = null;

		songTitle = null;
		albumImage = null;
		mainFrame = new JFXPanel();
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(2,25,25,25));
		
//		playTime = new Label();
//		playTime.setPrefWidth(130);
//		playTime.setMinWidth(50);
//		grid.add(playTime, 3, 5);
		
		mainScene = new Scene(grid, 300, 300);
		
		songTitle = makeLabel("Title: ", 0, 0, 0, grid);
		artist = makeLabel("Artist: ", 0, 1, 0, grid);
		albumTitle = makeLabel("Album: ", 0, 2, 0, grid);

		//Create the play/pause button and add its event handler.
		Button play = makeButton("Play/Pause", 0, 8, grid);
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (songLoaded) {
					alternatePlayback(player);
				}
				else {
					open("media libraries/audio/test.mp3");
					songLoaded = true;
					duration = player.getMedia().getDuration();
					
				}
			}
		});
		testButton = play;
		
		//Create the volume slider and add its event handler.
		volume = createSlider("Volume: ", 0, 4, grid);
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isValueChanging()) {
					volumeChange(volume.getValue());
				}
			}
		});
		
		//Create the time slider and add its event handler.
		time = createSlider("Time: ", 0, 6, grid);
		time.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable o) {
				if (time.isValueChanging()) {
					changePosition((long)time.getValue());
				}
			}
		});
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	/*Opens the specified music file and loads it into the player
	 * @param fileName The name of the desired music file
	 */
	private void openSong (String fileName) {
		if (fileName != null) {
			String uri = new File(fileName).toURI().toString();	
			if (uri.endsWith("mp3") || uri.endsWith("wav")) {
				Media media = new Media(uri);
				media.getMetadata().addListener( new MapChangeListener<String, Object>() {
					@Override
					public void onChanged(Change<? extends String, ? extends Object> change) {
						if (change.wasAdded()) {
							setMetadata(change.getKey(), change.getValueAdded());
						}
					}
				});
				MediaPlayer musicPlayer = new MediaPlayer(media);
				this.player = musicPlayer;
				musicPlayer.setOnReady(new Runnable() {
					public void run() {
						if (player != null) {
							duration = media.getDuration();
							updateValues();
							songLoaded = true;
							playing = true;
							player.setOnEndOfMedia(new Runnable() {
								public void run() {
									frame.advancePlaylist();
								}
							});
							musicPlayer.play();
	        				}
						}
				});
				}
				player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			    @Override
			        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
			            updateValues();
			    }
			});
		}
	}
		
	private void setMetadata(String key, Object value) {
		if (key.equals("title")) {
			songTitle.setText(value.toString());
		}
		if (key.equals("album")) {
			albumTitle.setText(albumTitle.getText() + value.toString());
		}
		if (key.equals("artist")) {
			artist.setText(artist.getText() + value.toString());
		}
		if (key.equals("image")) {
			albumImage.setImage((Image)value);
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
	
	/*This helper method keeps the current time of the song being played updated. */
	private void updateValues() {
		if (time != null && playTime != null && duration != null) {
			Platform.runLater(new Runnable() {
				public void run() {
					Duration currentTime = player.getCurrentTime();
					playTime.setText(formatTime(currentTime, duration));
					time.setDisable(duration.isUnknown());
					if (!time.isDisabled() && duration.greaterThan(Duration.ZERO) && !time.isValueChanging()) {
						time.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
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
			player.setVolume(newVolume / 100);
		}
		else if (newVolume < 0) {
			player.setVolume(player.getVolume() + newVolume);
		}
	}
	
	public void changePosition(long playbackPosition) {
		if (player.getMedia().getDuration() != null) {
			player.seek(duration.multiply(time.getValue() / 100));
		}
		updateValues();
	}
	
	
	public void skipPlayback() {
		if (player.getMedia().getDuration() != null) {
			player.seek(duration.divide(100));
		}
		updateValues();
	}
	
	public boolean clear() {
		if (this.songLoaded != true || this.player == null) {
			this.player = null;
			return false;
		}
		else {
			player.dispose();
			this.player = null;
			return true;
		}
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
	
	private Slider createSlider(String name, int column, int row, GridPane grid) {
		Slider slider = null;
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
			
		}
		else {
			slider = null;
		}
		grid.add(slider, column, row);
		return slider;
	}
	
	private Text makeLabel(String id, int column, int row, int size, GridPane grid) {
		Text text = new Text(id);
		text.setText(id);
		text.setFont(new Font(size));
		text.prefWidth(300);
		text.minWidth(50);
		grid.add(text, column, row);
		return text;
	}
	
	
	/* The commented out section is used if the MusicPlayer is not implemented as an Application. However, as of release time, it will be an Application. */
	public MusicPlayer(MainFrame frame) {
		this.frame = null;
		player = null;
		songLoaded = false;
		volume = null;
		duration = null;
		playTime = null;
		songTitle = null;
		albumImage = new ImageView();
		mainFrame = new JFXPanel();
		
		//Initialize the album art to the default value, if it is available in the file system.
   		String workingDir = System.getProperty("user.dir");
   		String fileSep = System.getProperty("file.separator");
   		String pathname = workingDir + fileSep + "src" + fileSep + "internaldata" + fileSep + "Blank_Album_Art.png";
		File f = new File(pathname);
		InputStream fileStream;
		try {
			fileStream = new FileInputStream(f);
			Image image = new Image(fileStream);
			if ( ! image.isError()) {
				ImageView iv = new ImageView();
				iv.setImage(image);
				this.albumImage = iv;
			}
		} catch (FileNotFoundException e1) {
			;
		}
			
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(2,25,25,25));
			
		mainScene = new Scene(grid, 400, 400);
			
		songTitle = makeLabel("", 1, 1, 20, grid);
		songTitle.setText("untitled");
		artist = makeLabel("Artist: ", 2, 1, 15, grid);
		albumTitle = makeLabel("Album: ", 2, 2, 15, grid);
			
		albumImage.setFitHeight(300.0);
		albumImage.setFitWidth(300.0);
		grid.add(albumImage, 1, 0);
			
		//Create the time slider and add its event handler.
		playTime = new Text();
		playTime.setFont(new Font(15));
		time = createSlider("Time: ", 2, 5, grid);
		time.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable o) {
				if (time.isValueChanging()) {
					changePosition((long)time.getValue());
				}
			}
		});
		grid.add(playTime, 1, 5);
		mainFrame.setScene(mainScene);
	}
	
	public static void main(String[] args) {
		Application.launch();
	}
}
