import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//manages all actionEvents in MainFrame
public class JFXController {
	
	private final MainFrame stage;
	
	public JFXController(MainFrame stage){
		this.stage = stage;
	}

	//controller for Rewind Button
	public class backFile implements EventHandler<javafx.event.ActionEvent>{

		@Override
		public void handle(javafx.event.ActionEvent event) {
			stage.backFile();
		}

	}
	
	//Controller for Play Button
	public class playButton implements EventHandler<javafx.event.ActionEvent>{

		@Override
		public void handle(javafx.event.ActionEvent event) {
			stage.play();
		}

	}
	
	//Controller for the volume slider
	public class volumeSlider implements InvalidationListener {
		
		@Override
		public void invalidated(Observable ov) {
			if (stage.getVolumeSlider().isValueChanging()) {
				stage.volumeChange();
			}
		}
	}
	
	//controller for Forward Button
	public class forwardFile implements EventHandler<javafx.event.ActionEvent>{

			@Override
			public void handle(javafx.event.ActionEvent event) {
				stage.forwardFile();

			}
	}
	
	
}
