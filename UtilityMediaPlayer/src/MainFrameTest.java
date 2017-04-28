import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.image.ImageObserver;

import javax.swing.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MainFrameTest{
	
	private MainFrame main;
	//reference to Main Frame test suite
	private static MainFrame.TestSuite tester = new MainFrame.TestSuite();
	
	//initialization of variables before each test
	@Before
	public void setUp() throws Exception {
		tester.newMainFrame();
		main = tester.getMainFrame();
		
	}
	
	//cleanup after each test
	@After
	public void tearDown() throws Exception {
		tester.getMainFrame().setVisible(false);
		tester.reset();



	}


	//test that main method runs without errors
	@Test
	public void testMain() {
		try{
			assertEquals("MainFrame does not have function to exit on close.",  JFrame.EXIT_ON_CLOSE, main.getDefaultCloseOperation());
			MainFrame.main(null);
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}
	
	//test create and show GUI to make sure that a full GUI is created and all components are there
	@Test
	public void testCreateAndShowGUI(){
		try{
			MainFrame instanceFrame = tester.createAndShowGUI();
			tester.mainFrame = instanceFrame;
			JFrame frame = tester.getMainFrame().getFrame();
			
			assertEquals("createAndShowGUI does not have function to exit on close.",  JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
			assertTrue("createAndShowGUI frame is not visible", frame.isVisible());
			assertTrue("createandShowGUI does not have right width", frame.getWidth() >= 1400);
			assertTrue("createandShowGUI does not have right height", frame.getHeight() >= 700);
			frame.getAccessibleContext();
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}

	//test that a content pane can be created
	@Test
	public void testCreateContentPane() {
		try{
			Container jpanel = main.createContentPane();
			assertFalse("Content Pane is opaque", jpanel.isOpaque());
			assertTrue("Content Pane is not visible", jpanel.isVisible());
			
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}

	//test that a fileList is properly created
	@Test
	public void testcreateFileList() {
		try{
			MainFrame instanceFrame = tester.createAndShowGUI();
			tester.mainFrame = instanceFrame;
			tester.setFileListDefault();
			JList<String> filelist = tester.getFileList(instanceFrame);
			
			assertTrue("Not correct number of elements being shown",filelist.getModel().getSize() == 4);
			assertTrue("List not in single selection mode" ,filelist.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION);
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}

	//test that an imageIcon is properly created by createImageIcon
	@Test
	public void testCreateImageIcon() {
		try{
			ImageIcon imageIcon = tester.createImageIcon("/internaldata/Forward.png");
			Image imageLoaded = new Image(getClass().getResourceAsStream("/internaldata/Forward.png"));
			java.awt.Image imageLoaded2 = new ImageIcon("/internaldata/Forward.png").getImage();
			assertTrue("no image loaded" , imageIcon.getImage() != null);
			assertTrue("image height not consistent", imageLoaded.getHeight() == imageIcon.getImage().getHeight(null));
			assertTrue("image width not consistent", imageLoaded.getWidth() == imageIcon.getImage().getWidth(null));
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}
	
	//test that createImageIcon returns null
	@Test 
	public void testCreateImageIconNull(){
		try{
			ImageIcon imageIcon = tester.createImageIcon("/internaldata/middle.gif");
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}
	
	//test that createControlBar creates a jfxpanel with correct elements
	@Test
	public void testCreateControlBar(){
		try{
			JFXPanel fxPanel = tester.createControlBar();
			assertEquals("controlBar not supposed to have components", 0, fxPanel.getComponentCount());
			Parent root = fxPanel.getScene().getRoot();
			assertTrue("root of ControlBar is not the set HBoxBar", root instanceof HBox);
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}
	
	//test that creating a new HBox from HBoxBar has all components
	@Test
	public void testNewHBoxBar(){
		try{
			HBox hbox = tester.newHBoxBar();
			System.out.println(hbox.getChildren());
			GridPane pane = (GridPane) hbox.getChildren().get(0);
			
			
			assertTrue(pane.getChildren().get(0) instanceof Button);
			assertEquals("incorrect first button", "Rewind", pane.getChildren().get(0).getAccessibleRoleDescription());
			assertTrue(pane.getChildren().get(1) instanceof Button);
			assertEquals("incorrect second button", "Play", pane.getChildren().get(1).getAccessibleRoleDescription());
			assertTrue(pane.getChildren().get(2) instanceof Button);
			assertEquals("incorrect second button", "Forward", pane.getChildren().get(2).getAccessibleRoleDescription());
			assertTrue(pane.getChildren().get(3) instanceof Label);
			assertEquals("incorrect second button", "Volume Text", pane.getChildren().get(3).getAccessibleRoleDescription());
			assertTrue(pane.getChildren().get(4) instanceof Slider);
			assertEquals("incorrect second button", "Volume", pane.getChildren().get(4).getAccessibleRoleDescription());
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}
	
	//test backFile
	@Test
	public void testBackFile(){
			MainFrame instanceFrame = tester.createAndShowGUI();
			tester.mainFrame = instanceFrame;
			JList<String> filelist = tester.getFileList(instanceFrame);
			
			int listSize = filelist.getModel().getSize();
			int endindex = listSize - 1;
			
			assertEquals("initial file list does not contain correct selection index" , -1, filelist.getSelectedIndex());
			tester.backFile();
			assertEquals("intial file list does not loop to back" , endindex ,filelist.getSelectedIndex());
			tester.backFile();
			assertEquals("file list does decrement to end - 1" , endindex -1 ,filelist.getSelectedIndex());
			tester.backFile();
			assertEquals("file list does decrement to end - 2" , endindex - 2 ,filelist.getSelectedIndex());
			tester.backFile();
			assertEquals("file list does decrement to end - 3" , endindex - 3 ,filelist.getSelectedIndex());
	}
	
	//test forwardFile operations
	@Test
	public void testForwardfile(){
		try{
			MainFrame instanceFrame = tester.createAndShowGUI();
			tester.mainFrame = instanceFrame;
			JList<String> filelist = tester.getFileList(instanceFrame);
			int listSize = filelist.getModel().getSize();
			int endindex = listSize - 1;
			int startindex = 0;
			assertEquals("initial file list does not contain correct selection index" , -1, filelist.getSelectedIndex());
			tester.forwardFile();
			assertEquals("initial file list does not loop to front" , startindex ,filelist.getSelectedIndex());
			
			filelist.setSelectedIndex(endindex);
			tester.forwardFile();
			assertEquals("file list does not loop to front from end index" , startindex ,filelist.getSelectedIndex());
			tester.forwardFile();
			assertEquals("file list does increment to end + 1" , startindex + 1 ,filelist.getSelectedIndex());
			tester.forwardFile();
			assertEquals(" file list does increment to end +2" , startindex + 2 ,filelist.getSelectedIndex());
			tester.forwardFile();
			assertEquals("file list does increment to end + 3" , startindex + 3 ,filelist.getSelectedIndex());
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}
	
	//test play operations
	@Test
	public void testPlay(){
		try{
			MainFrame instanceFrame = tester.createAndShowGUI();
			tester.mainFrame = instanceFrame;
			JList<String> filelist = tester.getFileList(instanceFrame);
			
			
			assertEquals("file does not start in empty mode", MainFrame.Mode.EMPTY, tester.getMode());
			assertTrue("player is incorrectly initialized to an object", tester.getCurrentPlayer() == null);
			tester.play();
			assertEquals("file does not start in empty mode", MainFrame.Mode.EMPTY, tester.getMode());
			assertTrue("player is incorrectly initialized to an object", tester.getCurrentPlayer() == null);
			
			
			
			//item 1
			filelist.setSelectedIndex(0);
			tester.play();
			assertEquals("file does not play Video files of mp3", MainFrame.Mode.AUDIO, tester.getMode());
			assertTrue("player doesn't create the musicPlayer on play", tester.getCurrentPlayer() instanceof MusicPlayer);
			
			tester.play();
			assertEquals("file does not keep same mode on play/pause", MainFrame.Mode.AUDIO, tester.getMode());
			assertTrue("player doesn't keep a musicPlayer on play/pause", tester.getCurrentPlayer() instanceof MusicPlayer);
			assertFalse("player does not pause music player", ((MusicPlayer)tester.getCurrentPlayer()).playing  );

			//item 2
			filelist.setSelectedIndex(1);
			tester.play();
			assertEquals("file does not play gif files", MainFrame.Mode.IMAGE, tester.getMode());
			assertTrue("player doesn't create the imageViewer on play", tester.getCurrentViewer() instanceof ImageViewer);
			tester.play();
			assertEquals("file does not keep same mode on play/pause", MainFrame.Mode.IMAGE, tester.getMode());
			assertTrue("player doesn't keep the imageViewer on play", tester.getCurrentViewer() instanceof ImageViewer);
			
			//item 3
			filelist.setSelectedIndex(3);
			tester.play();
			assertEquals("file does not play Video files", MainFrame.Mode.VIDEO, tester.getMode());
			assertTrue("player doesn't create the videoPlayer on play", tester.getCurrentPlayer() instanceof VideoPlayer);
			tester.play();
			assertEquals("file does not keep same mode on play/pause", MainFrame.Mode.VIDEO, tester.getMode());
			assertTrue("player doesn't keep a videoPlayer on play/pause", tester.getCurrentPlayer() instanceof VideoPlayer);
			assertTrue("player does not keep video on play", ((VideoPlayer)tester.getCurrentPlayer()).hasVideo()  );
			
			//item 4

		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
		
	}
	
	//test the volume operations
	@Test
	public void testVolumeControl(){
		
			MainFrame instanceFrame = tester.createAndShowGUI();
			tester.mainFrame = instanceFrame;
			JList<String> filelist = tester.getFileList(instanceFrame);
			Slider volumeSlider = tester.getVolumeSlider();
			assertTrue("player doesn't initialize the volume at right setting" , volumeSlider.getValue() >= 99);
			
			filelist.setSelectedIndex(0);
			tester.play();
			tester.setVolume(50);
			assertTrue("volume slider does not stay changed", 50 == volumeSlider.getValue());
			
			
			try{
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}

}
