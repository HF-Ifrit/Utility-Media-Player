import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.stage.Stage;

public class UMPApplication extends Application{

	public static void main(String[] args) {
		JFrame f = new JFrame("test1");
		/*
		String filename = "media libraries/test.mp3";
		Player currentPlayer = new MusicPlayer();
		currentPlayer.open(filename);
		Component tempPlayer = currentPlayer.showView();
		tempPlayer.setVisible(true);
		f.getContentPane().add(tempPlayer, BorderLayout.CENTER);
		*/
		
		String filename = "media libraries/video/singing_dove.mp4";
		Player currentPlayer = new VideoPlayer();
		
		
		f.setSize(400, 300);
		f.setVisible(true);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}
