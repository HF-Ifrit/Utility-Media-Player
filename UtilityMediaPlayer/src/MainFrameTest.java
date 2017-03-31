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
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

public class MainFrameTest {
	
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
			JFrame frame = tester.createAndShowGUI();
			assertEquals("createAndShowGUI does not have function to exit on close.",  JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
			assertTrue("createAndShowGUI frame is not visible", frame.isVisible());
			assertEquals("createandShowGUI does not have right width", 1600 , frame.getWidth());
			assertEquals("createandShowGUI does not have right height", 800 , frame.getHeight());
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
			assertEquals("Content Pane does not have a component",1, jpanel.getComponentCount());
			assertTrue("Content Pane is not visible", jpanel.isVisible());
			JScrollPane scrollPane = (JScrollPane) jpanel.getComponent(0);
			assertTrue("Scroll Pane is not visible", scrollPane.isVisible());
			assertEquals("Scroll Pane does not have a component",3, scrollPane.getComponentCount());
			
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}

	//test that a fileList is properly created
	@Test
	public void testcreateFileList() {
		try{
			JList<String> jList = tester.createFileList();
			assertTrue("Not correct number of elements being shown",jList.getModel().getSize() == 4);
			assertTrue("List not in single selection mode" ,jList.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION);
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
			//assertTrue("image is not requested Image", imageLoaded2.equals(imageIcon.getImage()));
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
			assertTrue(hbox.getChildren().get(0) instanceof Button);
			assertEquals("incorrect first button", "Rewind", hbox.getChildren().get(0).getAccessibleRoleDescription());
			assertTrue(hbox.getChildren().get(1) instanceof Button);
			assertEquals("incorrect second button", "Play", hbox.getChildren().get(1).getAccessibleRoleDescription());
			assertTrue(hbox.getChildren().get(2) instanceof Button);
			assertEquals("incorrect second button", "Forward", hbox.getChildren().get(2).getAccessibleRoleDescription());
			assertTrue(hbox.getChildren().get(3) instanceof Slider);
			assertEquals("incorrect second button", "Spacing", hbox.getChildren().get(3).getAccessibleRoleDescription());
			assertTrue(hbox.getChildren().get(4) instanceof Slider);
			assertEquals("incorrect second button", "Volume", hbox.getChildren().get(4).getAccessibleRoleDescription());
		}
		catch(Exception e){
			fail("Unexpected exception/error: " + e.toString());
		}
	}

}
