import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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
    
    //previous file that was played
    private String previousFile;
    private Mode previousMode;
    
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
		previousFile = "";
		mode = Mode.EMPTY;
		jfxControl = new JFXController(this);
		currentImage = new ImageViewer();
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
        displayFrame.setMinimumSize(new Dimension(600, 400));
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
    	
    	fxPanel.setScene(new Scene(HBoxBuilder.newHBoxBar(this)));
    	
    	return fxPanel;
    }
    
    /**
     * Getters and Setters
     */
    
    /**
     * returns JFXController for the MainFrame
     */
    public JFXController getJFXController(){
    	return this.jfxControl;
    }
    
    /**
     * returns the source PlayButton for MainFrame
     */
    public Button getPlayButton(){
    	return this.playButton;
    }
    
    /**
     * sets the playButton of the MainFrame
     */
    public void setPlayButton(Button playButton){
    	this.playButton = playButton;
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
	
	//plays current file at file selection index
	public void play(){
		String filename = "";
		Mode tempmode = Mode.EMPTY;
		int selectedindex = fileList.getSelectedIndex();
		if(selectedindex < 0){
			mode = Mode.EMPTY;
			return;
		}
		else{
			 filename = fileList.getModel().getElementAt(selectedindex);
			tempmode = parseFileType(filename);
		}
		/**
		 * TODO
		 * call respective player depending on mode
		 */
		//creates a new player for new file
		if(filename != previousFile){
			mode = tempmode;
			if(currentPlayer != null){
				currentPlayer.clear();
			}
			createViews(filename);
		
			
		}
		//runs play action on currentFile
		else{
			playbackExecute();
		}
		
	}
	
	//helper method to streamline closing video/music player windows
	private void closePlayers(){
		if(currentPlayer != null){
			currentPlayer.clear();
		}
	}
	
	//helper method to streamline play/pause of current mode
	private void playbackExecute(){
		if((mode == Mode.AUDIO) || (mode == Mode.VIDEO)){
			currentPlayer.alternatePlayback();
		}
	}
	
	//helper method to streamline creation of generic Players
	private void setupPlayers(String filename){
		currentPlayer.open(filename);
		currentPlayer.volumeChange(this.volumeSlider.getValue());
		Component tempPlayer = currentPlayer.showView();
		
		this.getContentPane().add(tempPlayer, BorderLayout.CENTER);
		tempPlayer.setVisible(true);
	}
	
	//helper method to streamline creation of new video/music players
	private void createViews(String filename){
		if(mode == Mode.AUDIO){
			//TODO testing checks
			filename = "media libraries/test.mp3";
			currentPlayer = new MusicPlayer();
			setupPlayers(filename);
			
			
		}
		if(mode == Mode.VIDEO){
			//TODO testing checks
			filename = "media libraries/video/singing_dove.mp4";
			currentPlayer = new VideoPlayer();
			setupPlayers(filename);
		}
		if(mode == Mode.IMAGE){
			//TODO testing checks
			filename = "media libraries/images/image.png";
			currentImage.open(filename);
			closePlayers();
			JFXPanel panel = new JFXPanel();
			panel.setScene(currentImage.getScene());
			this.getContentPane().add(panel, BorderLayout.CENTER);
			
		}
		this.paint(this.getGraphics());
		
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
	
	//sets the volumeSlider reference to selected reference
	public void setVolumeSlider(Slider volume){
		this.volumeSlider = volume;
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
	 * TODO
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
			 return HBoxBuilder.newHBoxBar(mainFrame);
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
