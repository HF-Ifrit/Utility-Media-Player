import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void testCreateImageIcon() {
		fail("Not yet implemented");
	}

}
