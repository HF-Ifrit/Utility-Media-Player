import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


//primary GUI window that will interact and control other modules
public class MainFrame extends JFrame {

	
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu, submenu;
	private JMenuItem menuItem;
	private JRadioButtonMenuItem rbMenuItem;
	private JCheckBoxMenuItem cbMenuItem;
	private Button playButton;
	private Slider volumeSlider;
	
	//JFX controllers
	private JFXController jfxControl;
	
	JTextArea output;
    JScrollPane scrollPane;
    JList<String> fileList;
    
    //players/viewers
    private Player currentPlayer;
    private ImageViewer currentImage;
    
    //player mode that is currently loaded
    private Mode mode;
    
    //enum to determine what mode the current controller is set to
    public enum Mode{
    	EMPTY,VIDEO,IMAGE,AUDIO
    }

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	private MainFrame() {
		
		currentPlayer = null;
		mode = Mode.EMPTY;
		jfxControl = new JFXController(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1040, 543);
		
        
      
		
	}
	
	//creates gui 
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame displayFrame = new JFrame("UMP Controller");
        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        MainFrame demo = new MainFrame();
        displayFrame.setJMenuBar(demo.createTextMenuBar());
        displayFrame.setContentPane(demo.createContentPane());
        demo.setFileList(createFileList());
        displayFrame.getContentPane().add(demo.fileList, BorderLayout.WEST);
        displayFrame.add(demo.createControlBar(), BorderLayout.SOUTH);
 
        //Display the window.
        displayFrame.setSize(1600, 900);
        displayFrame.setVisible(true);
    }
	
	//creates the menu bar with all options on it
	private JMenuBar createTextMenuBar(){

		JMenuBar menuBar;
		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu as File tab
		menuBar = MenuBarSetup.attachFileMenu(menuBar, this);
		
		//build the second menu as a View tab
		menuBar = MenuBarSetup.attachViewMenu(menuBar);
		
		//build the third menu as a Video tab
		menuBar = MenuBarSetup.attachVideoMenu(menuBar);
		
		//build fourth menu as Audio tab
		menuBar = MenuBarSetup.attachAudioMenu(menuBar);
		
		//build fifth menu as Image tab
		menuBar = MenuBarSetup.attachImageMenu(menuBar);
		
		//build the last menu as help tab
		menuBar =  MenuBarSetup.attachHelpMenu(menuBar);
		
		return menuBar;
	}
	
	//creates the sideView for file of lists
	private static JList<String> createFileList(){
		JList<String> list;
		list = new JList<String>();
		list.setModel(new AbstractListModel<String>() {
			String[] values = new String[] {
					"Video.mp4", "Audio.mp3", "Media.gif", "Image.png"};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		list.setFont(new Font("Tahoma", Font.PLAIN, 16));
		list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		return list;
	}
	
	//sets the MainFrame's fileList variable
	public void setFileList(JList<String> fileList){
		this.fileList = fileList;
	}
	
	
	
		
	//creates view window
	public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(false);
 
        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);
        scrollPane.setVisible(true);
       
 
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        contentPane.setVisible(true);
 
        return contentPane;
    }
	

	
	 /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainFrame.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	
    /**
     * creates the button controller for the frame
     * 
     * @return JFXPanel holding all buttons for the lower Controller Panel
     */
    private JFXPanel createControlBar(){
    	/**
    	 * creates player interface panel
    	 */
    	JFXPanel fxPanel = new JFXPanel();
    	GridPane grid = new GridPane();
    	
    	fxPanel.setScene(new Scene(newHBoxBar()));
    	
    	return fxPanel;
    }
    
    //helper method that adds the HBox Pane to the JFXPanel
    private HBox newHBoxBar(){
    	Button currentButton;
    	Image currentImage;
    	ImageView imageview;
    	
    	//initializes hbox
    	HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 400));
        hbox.setSpacing(30);
        
        
        //adds back button
        currentImage = new Image(getClass().getResourceAsStream("/internaldata/Rewind.png"));
    	currentButton = new Button();
    	imageview = new ImageView(currentImage);
    	imageview.setFitHeight(50);
    	imageview.setFitWidth(50);
    	currentButton.setGraphic(imageview);
    	currentButton.setOnAction(jfxControl.new backFile());
    	currentButton.setAccessibleRoleDescription("Rewind");
   

    	hbox.getChildren().add(currentButton);
    	
        
        //adds PlayButton image and button
    	Image playImage = new Image(getClass().getResourceAsStream("/internaldata/Play.png"));
    	Button playButtonTemp = new Button();
    	imageview = new ImageView(playImage);
    	imageview.setFitHeight(50);
    	imageview.setFitWidth(50);
    	playButtonTemp.setGraphic(imageview);
    	playButtonTemp.setOnAction(jfxControl.new playButton());
    	playButtonTemp.setAccessibleRoleDescription("Play");
    	this.playButton = playButtonTemp;
    	
    	hbox.getChildren().add(playButtonTemp);
    	
    	
    	
    	//add forward button
    	currentImage = new Image(getClass().getResourceAsStream("/internaldata/Forward.png"));
     	currentButton = new Button();
     	imageview = new ImageView(currentImage);
     	imageview.setFitHeight(50);
     	imageview.setFitWidth(50);
     	currentButton.setGraphic(imageview);
     	currentButton.setAccessibleRoleDescription("Forward");
     	currentButton.setOnAction(this.jfxControl.new forwardFile());

     	hbox.getChildren().add(currentButton);
     	
     	
     
     	Slider slider = new Slider(0, 100, 100);
		slider.setPrefWidth(300);
		slider.setMaxWidth(300);
		slider.setMinWidth(30);
		slider.setAccessibleRoleDescription("Spacing");
		
		
		slider.setVisible(false);
		hbox.getChildren().add(slider);
     	
		
		
		//add volume control slider
		final Slider volume = new Slider(0, 100, 100);
		volume.setPrefWidth(200);
		volume.setMaxWidth(300);
		volume.setMinWidth(30);
		volume.setAccessibleRoleDescription("Volume");
		
		//responds to slider movements
		/*
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isValueChanging()) {
					currentPlayer.volumeChange((int) volume.getValue());
				}
			}
		});
		*/
		
		hbox.getChildren().add(volume);
    	
    	
    	return hbox;
    }
    
    /**
     * operations called by actionListeneers
     */
	//move file selection unit forward one index
	public void backFile(){
		int setIndex = fileList.getModel().getSize() - 1;
		if(fileList.getSelectedIndex() > 0)
			fileList.setSelectedIndex(fileList.getSelectedIndex() - 1);
		else
			fileList.setSelectedIndex(setIndex);
	}
	
	//plays current file at file seleciton index
	public void play(){
		int selectedindex = fileList.getSelectedIndex();
		if(selectedindex == -1){
			mode = Mode.EMPTY;
		}
		else{
			String filename = fileList.getModel().getElementAt(selectedindex);
			mode = parseFileType(filename);
		}
		/**
		 * TODO
		 * call respective player depending on mode
		 */
		
	}
	
	
	//moves the file selection unit back one index
	public void forwardFile(){
		int setIndex = 0;
		if(fileList.getSelectedIndex() < fileList.getModel().getSize() - 1)
			fileList.setSelectedIndex(fileList.getSelectedIndex() + 1);
		else
			fileList.setSelectedIndex(setIndex);
	}
	
	
	//temporary parse file system for supported formats
	/**
	 * TODO
	 * will be implemented in a separate file management class
	 */
	public Mode parseFileType(String file){
		
		//checks if ending filetype is video format
		if(file.substring(file.lastIndexOf('.')).equals(".gif") || file.substring(file.lastIndexOf('.')).equals(".mp4")){
			return Mode.VIDEO;
		}
		//check if ending filetype is audio format
		else if(file.substring(file.lastIndexOf('.')).equals(".mp3")){
			return Mode.AUDIO;
		}
		//check if ending filetype is image format
		else if(file.substring(file.lastIndexOf('.')).equals(".png") || file.substring(file.lastIndexOf('.')).equals(".jpg")){
			return Mode.IMAGE;
		}
		//otherwise file can't be opened
		else
			return Mode.EMPTY;
	}
	
	/**
	 *TODO 
	 *integrate actions with other components
	 *
	 */
	
	//controller for opening a file
	public class openFile implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Opening file...");
			
		}
		
	}
	
	//controller for opening multiple files
	public class openMultipleFiles implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Opening multiple files...");
				
			}
			
	}
	
	//controller for opening multiple files
	public class openSaveFile implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			e.getSource();
			System.out.println("Saving file...");

		}

	}

	



	
	/**
	 * Testing variables
	 * 
	 */
	
	
	/**
	 * 
	 * internal testing class
	 * provides access to private variables and methods only for testing purposes only
	 * remove during release
	 */
	public static class TestSuite{
		/**
		 * reference to a controlled testing instance of a MainFrame
		 */
		public MainFrame mainFrame;
		
		
		//creates a new MainFrame within the TestSuite
		public void newMainFrame(){
			mainFrame = new MainFrame();
		}
		
		//returns current instance of the MainFrame
		public MainFrame getMainFrame(){
			return mainFrame;
		}
		
		
		//returns frame created by createAndShowGUI for testing purposes
		public JFrame createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("UMP Controller");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	        //Create and set up the content pane.
	        MainFrame demo = new MainFrame();
	        mainFrame = demo;
	        frame.setJMenuBar(demo.createTextMenuBar());
	        frame.setContentPane(demo.createContentPane());
	        demo.setFileList(MainFrame.createFileList());
	        frame.getContentPane().add(demo.fileList, BorderLayout.WEST);
	        frame.add(demo.createControlBar(), BorderLayout.SOUTH);
	 
	        //Display the window.
	        frame.setSize(1600, 800);
	        frame.setVisible(true);
	        
	        return frame;
	    }
		
		//returns JList from createFileList
		public static JList<String> createFileList(){
			 
	        
	        //add list to content pane
			return MainFrame.createFileList();
		}
		
		//returns an image icon from the createImageIcon 
		 public static ImageIcon createImageIcon(String path) {
		        return MainFrame.createImageIcon(path);
		 }
		 
		 //returns JFXPanel Control Bar
		 public JFXPanel createControlBar(){
		    	return mainFrame.createControlBar();
		 }
		 
		 //returns HBox of mainFrame
		 public HBox newHBoxBar(){
			 return mainFrame.newHBoxBar();
		 }
		 
		 //returns the fileList of mainFrame
		 public JList<String> getFileList(){
			 return mainFrame.fileList;
		 }
		 
		 //returns the current Mode of the controller
		 public Mode getMode(){
			 return mainFrame.mode;
		 }
		 
		 //runs backFile procedure
		 public void backFile(){
			 mainFrame.backFile();
		 }
		 
		 //runs the playFile procedure
		 public void play(){
			 mainFrame.play();
		 }
		 
		 //runs forwardFile procedure
		 public void forwardFile(){
			 mainFrame.forwardFile();
		 }
		 
		
		//resets all stored values in the TestSuite
		public void reset(){
			mainFrame = null;
		}
	}
}
