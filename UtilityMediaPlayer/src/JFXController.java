import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//manages all actionEvents in MainFrame
public class JFXController {
	
	private final MainFrame stage;
	
	public JFXController(MainFrame stage){
		this.stage = stage;
	}

	//controller for Rewind Button
	public class backFile implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
