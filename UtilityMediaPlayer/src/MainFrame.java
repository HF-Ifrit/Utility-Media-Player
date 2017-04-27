import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.html.Option;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


//primary GUI window that will interact and control other modules
public class MainFrame extends JFrame {
	
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	private JFrame frame;
	private static final String AUDIO_PATH = "media libraries/audio/";
	private static final String VIDEO_PATH = "media libraries/video/";
	private static final String IMAGE_PATH = "media libraries/images/";
	
	final JFileChooser fileChooser;

	
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu, submenu;
	private JMenuItem menuItem;
	private JRadioButtonMenuItem rbMenuItem;
	private JCheckBoxMenuItem cbMenuItem;
	
	//controlled viewable items
	private Button playButton;
	private Slider volumeSlider;
	private Slider timeStampSlider;
	
	//JFX controllers
	private JFXController jfxControl;
	
	JTextArea output;
    JScrollPane scrollPane;
    JList<String> fileList;
    
     //reference to the file Lists' model for graphics
    private static DefaultListModel<String> fileListModel;
    //mappings of external file names to locations
    private Map<String, String> fileLocationMap;
    
    //PlayList we are on
    private Playlist playlist;
    
    
    //players/viewers
    private Player currentPlayer;
    private ImageViewer currentViewer;
    
    //current selected file of the controller
    private String currentFile;

    
    //previous file that was played
    private String previousFile;
    private Mode previousMode;
    private Component previousComponent;
    
    //old dimensions from fullscreen
    private Dimension oldDimensions;
    
    //player mode that is currently loaded
    private Mode mode;
    
    //display modes
    private boolean fullscreenMode;
    
    //the specific MenuBarBuilder to the OS or type
    
    
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
	private MainFrame(JFrame frame) {
		this.frame = frame;
		currentPlayer = null;
		previousFile = "";
		mode = Mode.EMPTY;
		jfxControl = new JFXController(this);
		currentViewer = new ImageViewer();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1040, 543);
		fileChooser = new JFileChooser();
		fileLocationMap = new HashMap<String, String>();
        oldDimensions = new Dimension(1040,543);
        fullscreenMode = false;
	}
	
	
	//creates gui 
	private static void createAndShowGUI() {

		
		try {
		       UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
			 }
		catch (Exception ex) {
				ex.printStackTrace();
			 }
		
		
        //Create and set up the window.
        JFrame displayFrame = new JFrame();
        displayFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        MainFrame demo = new MainFrame(displayFrame);
        displayFrame.setJMenuBar(demo.createTextMenuBar());
        displayFrame.setContentPane(demo.createContentPane());
        demo.setFileList(createFileList(demo));
        demo.scrollPane = new JScrollPane();
        demo.scrollPane.setPreferredSize(new Dimension(200,demo.fileList.getHeight() ));
        demo.scrollPane.setViewportView(demo.fileList);
        displayFrame.getContentPane().add(demo.scrollPane, BorderLayout.WEST);
        //displayFrame.add(demo.createTimeControl(), BorderLayout.SOUTH);
        displayFrame.add(demo.createControlBar(), BorderLayout.SOUTH);
       


        /*
        StackPane stack = new StackPane();
        Scene scene = new Scene(stack,300,300);
        Text hello = new Text("Hello");
        
        scene.setFill(Color.BLACK);
        hello.setFill(Color.WHEAT);
        hello.setEffect(new Reflection());
        JFXPanel panel = new JFXPanel();
        panel.setScene(scene);
        stack.getChildren().add(hello);
        displayFrame.getContentPane().add(panel, BorderLayout.EAST);
        */
        
        
        //Display the window.
        displayFrame.setSize(1600, 900);
        displayFrame.setVisible(true);
        displayFrame.setMinimumSize(new Dimension(600, 400));

    }
	
	//creates the menu bar with all options on it
	private JMenuBar createTextMenuBar(){

		JMenuBar menuBar;
		MenuBarSetup menuBarSetup = getMenuBarVersion();
		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu as File tab
		menuBar = menuBarSetup.attachFileMenu(menuBar, this);
		
		//build the second menu as a View tab
		menuBar = menuBarSetup.attachViewMenu(menuBar, this);
		
		//build the third menu as a Video tab
		menuBar = menuBarSetup.attachVideoMenu(menuBar, this);
		
		//build fourth menu as Audio tab
		//menuBar = menuBarSetup.attachAudioMenu(menuBar, this);
		
		//build fifth menu as Image tab
		menuBar = menuBarSetup.attachImageMenu(menuBar, this);
		
		//build the last menu as help tab
		menuBar =  menuBarSetup.attachHelpMenu(menuBar, this);
		
		return menuBar;
	}
	
	//determines which OS/Menu Bar style to create of an abstarct MenuBarSetup
	private MenuBarSetup getMenuBarVersion(){
		if(OsUtils.isWindows()){
			return new WindowsMenuBarSetup();
		}
		else if(OsUtils.isUnix()){
			return new GenericMenuBarSetup();
		}
		else{
			return new GenericMenuBarSetup();
		}
	}
	
	
	private static void removeImproperFileTypes(ArrayList<String> fileList) {
		ArrayList<String> improper = new ArrayList<String>();
		for(String file : fileList) {
			if(file.endsWith(".gif")) {
				continue;
			}
			else if(file.endsWith(".jpg")) {
				continue;
			}
			else if(file.endsWith(".png")) {
				continue;
			}
			else if(file.endsWith(".mp3")) {
				continue;
			}
			else if(file.endsWith(".wav")) {
				continue;
			}
			else if(file.endsWith(".webm")) {
				continue;
			}
			else if(file.endsWith(".mp4")) {
				continue;
			}
			
			else improper.add(file);
		}
		
		fileList.removeAll(improper);
	}
	
	//creates the sideView for file of lists
	private static JList<String> createFileList(MainFrame mainFrame){
		JList<String> list;
		list = new JList<String>();
		ArrayList<String> audio = MainFrame.getFolderContents(MainFrame.AUDIO_PATH);
		ArrayList<String> video = MainFrame.getFolderContents(MainFrame.VIDEO_PATH);
		ArrayList<String> images = MainFrame.getFolderContents(MainFrame.IMAGE_PATH);
		ArrayList<String> fileList = new ArrayList<String>();
		fileList.addAll(audio);
		fileList.addAll(video);
		fileList.addAll(images);
		removeImproperFileTypes(fileList);
		Collections.sort(fileList);
		fileListModel = new DefaultListModel<String>();
		for(String fileName : fileList){
			fileListModel.addElement(fileName);
		}
		list.setModel(fileListModel);
		
		
		/*
		list.setModel(new DefaultListModel<String>()
		{
			String[] values = new String[] {
					"Video.mp4", "Audio.mp3", "Media.gif", "Image.png"};
			
			ArrayList<String> fileNames = fileList;
			
			public ArrayList<String> getFileNames() 
			{
				return fileNames;
			}
			
			public int getSize() 
			{
				return fileNames.size();
			}
			
			public String getElementAt(int index) {
				return getFileNames().get(index);
			}
			
			public void addElement(String newFile){
				getFileNames().add(newFile);
			}
		});
		*/
		
		//listener for double clicks
		list.addMouseListener(new MouseAdapter(){
		    @Override
		    public void mouseClicked(MouseEvent e){
		        if(e.getClickCount()==2){
		           mainFrame.play();
		        }
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
//        output = new JTextArea(5, 30);
//        output.setEditable(false);
//        scrollPane = new JScrollPane(output);
//        scrollPane.setVisible(true);
       
 
        //Add the text area to the content pane.
       // contentPane.add(scrollPane, BorderLayout.CENTER);
        
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
    	
    	fxPanel.setScene(new Scene(HBoxBuilder.newHBoxBar(this)));
    	
    	return fxPanel;
    }
    
    /**
     * creates a time controller for the frame
     * @return JFXPanel holding the timeStamp slider
     */
    private JFXPanel createTimeControl(){
    	/**
    	 * creates player interface panel
    	 */
    	JFXPanel fxPanel = new JFXPanel();
    	
    	fxPanel.setScene(new Scene(HBoxBuilder.newTimeStampTrackerBar(this)));
    	
    	return fxPanel;
    }
    
    /**
     * combine both bars
     */
    private JFXPanel createBothControls(){
    	JFXPanel fxPanel = new JFXPanel();
    	
    	
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
     * TODO
     * operations called by actionListeneers
     */
    
	//move file selection unit forward one index
	void backFile(){
		int setIndex = fileList.getModel().getSize() - 1;
		if(fileList.getSelectedIndex() > 0)
			fileList.setSelectedIndex(fileList.getSelectedIndex() - 1);
		else
			fileList.setSelectedIndex(setIndex);
	}
	
	//sets window to fullscreen
	private void fullscreen(){
		if(!fullscreenMode){
			oldDimensions = getFrame().getSize();
			device.setFullScreenWindow(frame);
			fullscreenMode = true;
		}
		else{
			device.setFullScreenWindow(null);
			getFrame().setSize(oldDimensions);
			fullscreenMode = false;
		}
	}
	
	//hides fileitems
	private void hideItems(){
		if(scrollPane.isVisible() ){
			scrollPane.setVisible(false);
		}
		else 
			scrollPane.setVisible(true);
	}
	
	
	
	//creates a image properties pop-up
	private void imageProperties(){
		if(currentViewer != null){
			currentViewer.imageProperties();
		}
	}
	
	
	//call to rotate image
	private void rotate(boolean clockwise){
		if(currentViewer != null)
			rotateImage(clockwise);
	}
	
	//call to flip image
	private void flip(boolean horizontal){
		if(currentViewer != null){
			if(horizontal)
				mirrorImage();
			else
				mirrorImageVertically();
		}
	}
	
	//changes volume to slider value
	void volumeChange() {
		if ((mode == Mode.AUDIO) || (mode == Mode.VIDEO)) {
			currentPlayer.volumeChange(volumeSlider.getValue());
		}
	}
	
	//changes time to slider value
	void timeStampChange(){
		if ((mode == Mode.AUDIO) || (mode == Mode.VIDEO)) {
			//TODO
		}
	}
	
	private boolean saveInLibrary(File toSave) {
		if(toSave == null) return false;
		
		try {
			if(toSave.getName().endsWith(".gif") || toSave.getName().endsWith(".png") || toSave.getName().endsWith(".jpg")) {
				String pathname = IMAGE_PATH + toSave.getName();
				
				Files.copy(Paths.get(toSave.getAbsolutePath()), Paths.get(pathname), StandardCopyOption.REPLACE_EXISTING);
				return true;
			}
			
			else if (toSave.getName().endsWith(".mp4") || toSave.getName().endsWith(".webm")) {
				String pathname = VIDEO_PATH + toSave.getName();
				
				Files.copy(Paths.get(toSave.getAbsolutePath()), Paths.get(pathname), StandardCopyOption.REPLACE_EXISTING);
				return true;
			}
			
			else if (toSave.getName().endsWith(".mp3") || toSave.getName().endsWith(".wav")) {
				String pathname = AUDIO_PATH + toSave.getName();
				
				Files.copy(Paths.get(toSave.getAbsolutePath()), Paths.get(pathname), StandardCopyOption.REPLACE_EXISTING);
				return true;
			}
			
		} catch (IOException e) {
			return false;
		}
		
		return false;
	}
	
	//adds current file to playlist
	private void addToPlaylist(){
		if(currentFile != null){
			if(playlist == null){
				playlist = new Playlist(this, "untitled");
			}
			playlist.addTrack(currentFile);
		}
	}
	
	//save playlist
	private void savePlaylist(String filename){
		if(playlist != null && filename != null){
			playlist.savePlaylist(filename);
		}
	}
	
	//save playlist
	private void openPlaylist(String filename){
			if(filename != null){
				if(playlist == null){
					JOptionPane.showMessageDialog(frame, "No playlist selected.");
				}
				playlist.loadPlaylist(filename);
			}
		}
	
	private boolean rotateImage(boolean clockwise) {
		boolean success = currentViewer.rotateImage(clockwise);
		
		if( ! success) {
			return false;
		}
		
		else setupViewer();
		return true;
	}
	
	public boolean mirrorImage() {
		boolean success = currentViewer.mirrorImage();
		
		if( ! success) {
			return false;
		}
		else 
			setupViewer();
		
		return true;
	}
	
	public boolean mirrorImageVertically() {
		boolean success = currentViewer.mirrorImageVertically();
		
		if( ! success) {
			return false;
		}
		
		else setupViewer();
		return true;
	}
	
	public void orderFileListByType() {
		Enumeration<String> fileListEnum = fileListModel.elements();
		
		ArrayList<String> videos = new ArrayList<String>();
		ArrayList<String> audio = new ArrayList<String>();
		ArrayList<String> images = new ArrayList<String>();
		
		while(fileListEnum.hasMoreElements()) {
			String next = fileListEnum.nextElement();
			if(isVideo(next)) {
				videos.add(next);
			}
			else if(isAudio(next)) {
				audio.add(next);
			}
			else if(isImage(next)) {
				images.add(next);
			}
		}
		
		fileListModel.clear();
		
		for(String s : videos) {
			fileListModel.addElement(s);
		}
		for(String s : audio) {
			fileListModel.addElement(s);
		}
		for(String s : images) {
			fileListModel.addElement(s);
		}
	}
	
	public void orderFileListByName() {
		Enumeration<String> fileListEnum = fileListModel.elements();
		ArrayList<String> toOrder = new ArrayList<String>();
		
		while(fileListEnum.hasMoreElements()) {
			String next = fileListEnum.nextElement();
			toOrder.add(next);
		}
		
		//Sort by the natural ordering; for strings, this is lexicographical order.
		toOrder.sort(null);
		
		fileListModel.clear();
		for(String s : toOrder) {
			fileListModel.addElement(s);
		}
	}
	
	private boolean isVideo(String filename) {
		return (filename.endsWith(".mp4") || filename.endsWith(".webm"));
	}
	
	private boolean isAudio(String filename) {
		return (filename.endsWith(".mp3") || filename.endsWith(".wav"));
	}
	
	private boolean isImage(String filename) {
		return (filename.endsWith(".gif") || filename.endsWith(".png") || filename.endsWith(".jpg"));
	}
	
	
	//helper method to streamline closing video/music player windows
	private void updateComponent(Component newComponent){
		if(previousComponent != null){
			getFrame().remove(previousComponent);
		}
		previousComponent = newComponent;
	}
	
	//helper method to streamline play/pause of current mode
	private void playbackExecute(){
		if((mode == Mode.AUDIO) || (mode == Mode.VIDEO)){
			currentPlayer.alternatePlayback();
		}
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
				currentPlayer = null;
			}
			createViews(filename);
			
			
		}
		//runs play action on currentFile
		else{
			playbackExecute();
		}	
	}
	
	//helper method to streamline creation of generic Players
	private void setupPlayers(String filename){
		Component tempPlayer = currentPlayer.showView();
		getFrame().add(tempPlayer, BorderLayout.CENTER);
		getFrame().setVisible(true);
		
		
		currentPlayer.open(filename);
		currentPlayer.volumeChange(this.volumeSlider.getValue());
		updateComponent(tempPlayer);
		getFrame().setVisible(true);
		getFrame().validate();		
		getFrame().repaint();
	}
	
	//helper method to creation for new scene
	private void setupViewer(){
		JFXPanel panel = new JFXPanel();
		panel.setScene(currentViewer.getScene());
		
		JFXPanel fixedPanel = new JFXPanel();
		fixedPanel.setLayout(new GridBagLayout());
		fixedPanel.add(panel);
		JScrollPane scroll = new JScrollPane(fixedPanel);
		scroll.setPreferredSize(getFrame().getSize());
		
		getFrame().add(scroll, BorderLayout.CENTER);
		
		updateComponent(panel);
		updateComponent(fixedPanel);
		updateComponent(scroll);
		getFrame().setVisible(true);
		getFrame().validate();		
		getFrame().repaint();
	}
	
	
	//helper method to streamline creation of new video/music players
	private void createViews(String filename){
		this.previousFile = filename;
		if(mode == Mode.AUDIO){
			String tempFilename = "media libraries/audio/" + filename;
			filename = verifyFilePath(filename, tempFilename);
			currentFile = filename;
			currentPlayer = new MusicPlayer();
			setupPlayers(filename);
		}
		if(mode == Mode.VIDEO){
			String tempFilename = "media libraries/video/" + filename;
			filename = verifyFilePath(filename, tempFilename);
			currentPlayer = new VideoPlayer();
			currentFile = filename;
			setupPlayers(filename);
		}
		if(mode == Mode.IMAGE){
			String tempFilename = "media libraries/images/" + filename;
			filename = verifyFilePath(filename, tempFilename);
			currentFile =  filename;
			currentViewer.open(filename);
			setupViewer();
		}
		this.paint(this.getGraphics());  
	}
	
	
	
	/**
	 * helper method that convert the filename to the absolute path if the filename is found to be a external file
	 * @param filename
	 * @param internalFilePath
	 * @return gives the absolute file path for external files, otherwise switches to internalFilePath given
	 */
	private String verifyFilePath(String filename, String internalFilePath){
		if(fileLocationMap.get(filename) != null){
			return fileLocationMap.get(filename);
		}
		else 
			return internalFilePath;
	}
	
	
	//moves the file selection unit back one index
	public void forwardFile(){
		int setIndex = 0;
		if(fileList.getSelectedIndex() < fileList.getModel().getSize() - 1)
			fileList.setSelectedIndex(fileList.getSelectedIndex() + 1);
		else
			fileList.setSelectedIndex(setIndex);
	}
	
	//pop up help info
	private void openHelpMenu(){
		String about = "New media can be imported directly by inserting it in the \"media libraries\" folder." + "\n"
				+ "External media can be opened using File >> Open File. " + "\n"
				+ "Double-click media to play it.";
		JOptionPane.showMessageDialog(null, about,"About", JOptionPane.INFORMATION_MESSAGE);
	}
	
	//pop up about info
	private void openAboutInfo(){
		String about = "Utility Media Player" + "\n"
				+ "Prerelease Version";
		JOptionPane.showMessageDialog(null, about,"About", JOptionPane.INFORMATION_MESSAGE);
		}
	
	
	//temporary parse file system for supported formats
	/**
	 * TODO
	 * will be implemented in a separate file management class
	 */
	public Mode parseFileType(String file){
		
		//checks if ending filetype is video format
		if(file.substring(file.lastIndexOf('.')).equals("." + VideoPlayer.VideoFormat.MP4.toString().toLowerCase()) 
				|| file.substring(file.lastIndexOf('.')).equals("." + VideoPlayer.VideoFormat.WEBM.toString().toLowerCase()))
		{
			return Mode.VIDEO;
		}
		//check if ending filetype is audio format
		else if(file.substring(file.lastIndexOf('.')).equals("." + MusicPlayer.MusicFormat.MP3.toString().toLowerCase())
				|| file.substring(file.lastIndexOf('.')).equals("." + MusicPlayer.MusicFormat.WAV.toString().toLowerCase())){
			return Mode.AUDIO;
		}
		//check if ending filetype is image format
		else if(file.substring(file.lastIndexOf('.')).equals("." + ImageViewer.ImageFormat.GIF.toString().toLowerCase()) 
				|| file.substring(file.lastIndexOf('.')).equals("." + ImageViewer.ImageFormat.JPG.toString().toLowerCase())
				|| file.substring(file.lastIndexOf('.')).equals("." + ImageViewer.ImageFormat.PNG.toString().toLowerCase()))
		{
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
	
	public Slider getVolumeSlider() {
		return volumeSlider;
	}
	
	//sets the timestampSlider reference to the selected reference
	public void setTimeStampSlider(Slider timeStamp){
		this.timeStampSlider = timeStamp;
	}
	
	public Slider getTimeStampSlider(){
		return timeStampSlider;
	}
	
	
	
	
	
	/**
	 *TODO 
	 *integrate actions with other components
	 *controllers for listeners to attach to the buttons
	 *
	 */
	
	//controller for opening a file
	public class openFile implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			int returnVal = fileChooser.showOpenDialog(contentPane);
			 if (returnVal == JFileChooser.APPROVE_OPTION) {
		           File file = fileChooser.getSelectedFile();
		           String filename = file.getName();
		           fileListModel.addElement(filename);
		           fileList.setSelectedValue(filename, true);
		           String path = file.getAbsolutePath();
		           fileLocationMap.put(filename, path);
		           play();
			 }
		}
	}
	
	//controller for adding items to playlist
	public class addToPlaylist implements ActionListener{
		MainFrame frame = this.frame;
		@Override
		
		public void actionPerformed(ActionEvent e) 
		{
			if (playlist == null) {
				String fileName = JOptionPane.showInputDialog("Please name your playlist: ");
				playlist = new Playlist(frame, fileName);
				savePlaylist(fileName);
			}
			addToPlaylist();
		}
	}
	
	//controller for saving the playlist
	public class savePlaylist implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			//TODO give savePlaylist a file name
			String fileName = JOptionPane.showInputDialog("Please name your playlist: ");
			savePlaylist(fileName);
		}
	}
	
	//controller for opening the playlist
	public class openPlaylist implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			//TODO give openPlaylist a file name
			int returnVal = fileChooser.showOpenDialog(contentPane);
			 if (returnVal == JFileChooser.APPROVE_OPTION) {
		           File file = fileChooser.getSelectedFile();
		           String fileName = file.getName();
		           openPlaylist(fileName);
		           fileListModel.addElement(fileName);
		           fileList.setSelectedValue(fileName, true);
		           String path = file.getAbsolutePath();
		           fileLocationMap.put(fileName, path);
			 }
		}
	}
		
	
	//controller for play menu option
	public class play implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			play();
		}
		
	}
	
	//sets window to fullscreen mode
	public class fullscreen implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			fullscreen();
		}
	}
	
	//sets window to fullscreen mode
	public class hideItems implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			hideItems();
		}
	}	
	
	//controller for video player screen capture
	public class capture implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(mode.equals(MainFrame.Mode.VIDEO))
			{
				VideoPlayer vPlayer = (VideoPlayer)currentPlayer;
				vPlayer.captureScreen(ImageViewer.ImageFormat.PNG);
			}
		}
	}
	
	//controller for video player audio track extraction
	public class extractAudio implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(mode.equals(MainFrame.Mode.VIDEO))
			{
				VideoPlayer vPlayer = (VideoPlayer)currentPlayer;
				MusicPlayer.MusicFormat[] values = MusicPlayer.MusicFormat.values();
				
				//create options for the joptionpane from our list of available music formats
				String[] options = new String[values.length + 1];
				for(int i = 0; i < values.length; i++)
					options[i] = values[i].toString();
				
				options[options.length - 1] = "Cancel"; //adding cancel to the list of options
				
				//based on which index is clicked from the options, use the corresponding format index for extraction, unless it's the cancel button
				int formatIndex = JOptionPane.showOptionDialog(getFrame(), 
						"What format would you like to save the audio track as?", 
						"Save audio track as...", 
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				
				if(formatIndex != options.length-1)
					vPlayer.extractAudio(values[formatIndex]);
			}
		}
	}
	
	//controller for videoing gifs
	public class gifToVideo implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentViewer == null) {
				JOptionPane.showMessageDialog(null, "Gif-to-Video conversion failed; an image must be open first.","Video Conversion Warning", JOptionPane.WARNING_MESSAGE);
			}
			
			else {
				boolean success = currentViewer.gifToVideo();
				if( ! success) {
					JOptionPane.showMessageDialog(null, "Gif-to-Video conversion failed; ensure your image is of the proper type and accessible.","Video Conversion Warning", JOptionPane.WARNING_MESSAGE);
				}
				
				else {
					JOptionPane.showMessageDialog(null, "Gif-to-Video finished; your video is in the Output folder.","Video Conversion", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
	
	//controller for clipping videos
	public class clipVideo implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(mode.equals(MainFrame.Mode.VIDEO))
			{
				String start = JOptionPane.showInputDialog(getFrame(),
						"Input the start time for the video clip in the following format:", 
						"HH:MM:SS");
				
				String end = JOptionPane.showInputDialog(getFrame(),
						"Input the end time for the video clip in the following format:",
						"HH:MM:SS");
				
				if(!start.contains(":") || !end.contains(":"))
				{
					JOptionPane.showMessageDialog(getFrame(), "Invalid time format");
				}
				else
				{
					int startHour = Integer.parseInt(start.substring(0,2));
					int startMinutes = Integer.parseInt(start.substring(3,5));
					int startSeconds = Integer.parseInt(start.substring(6,8));
					
					int startTime = (startHour * 3600) + (startMinutes * 60) + startSeconds;
					
					int endHour = Integer.parseInt(end.substring(0,2));
					int endMinutes = Integer.parseInt(start.substring(3,5));
					int endSeconds = Integer.parseInt(start.substring(6,8));
					
					int endTime = (endHour * 3600) + (endMinutes * 60) + endSeconds;
					
					VideoPlayer vPlayer = (VideoPlayer)currentPlayer;
					vPlayer.clipVideo(startTime, endTime);
					
							
				}
					
			}

				
					
			
		}
	}

	//controller for image viewer Properties
	public class imageProperties implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentViewer != null)
				currentViewer.imageProperties();
		}
	}
	
	//controller to rotate the image
	public class rotate implements ActionListener{
		private boolean clockwise;
		
		rotate(boolean clockwise){
			this.clockwise = clockwise;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			rotate(clockwise);
			
		}
	}
	
	//controller to flip the image
	public class flip implements ActionListener{
		//whether to flip horizontally or not
		private boolean direction;
		
		flip(boolean direction){
			this.direction = direction;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			flip(direction);
				
			
		}
	}
	
	//creates a new pop-up window with help/instructions info
	public class openHelpMenu implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			openHelpMenu();

		}
	}
	
	//creates a new pop-up window with about info for version, authors, license, etc.
	public class openAboutInfo implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			openAboutInfo();

		}
	}
	
	
	
	
	public JFrame getFrame()
	{
		return frame;
	}

	public static ArrayList<String> getFolderContents(String folderPath)
	{
		File f = new File(folderPath);
		File[] files = f.listFiles();
		ArrayList<String> fileNames = new ArrayList<String>(files.length);
		for(int i = 0; i < files.length; i++)
			fileNames.add(files[i].getName());
		
		return fileNames;
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
			mainFrame = new MainFrame(new JFrame());
		}
		
		//returns current instance of the MainFrame
		public MainFrame getMainFrame(){
			return mainFrame;
		}
		
		
		//returns frame created by createAndShowGUI for testing purposes
		//creates gui 
		public static MainFrame createAndShowGUI() {

			
			try {
			       UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
				 }
			catch (Exception ex) {
					ex.printStackTrace();
				 }
			
			
	        //Create and set up the window.
	        JFrame displayFrame = new JFrame();
	        displayFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

	        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	        //Create and set up the content pane.
	        MainFrame demo = new MainFrame(displayFrame);
	        displayFrame.setJMenuBar(demo.createTextMenuBar());
	        displayFrame.setContentPane(demo.createContentPane());
	        demo.setFileList(MainFrame.createFileList(demo));
	        demo.scrollPane = new JScrollPane();
	        demo.scrollPane.setPreferredSize(new Dimension(200,demo.fileList.getHeight() ));
	        demo.scrollPane.setViewportView(demo.fileList);
	        displayFrame.getContentPane().add(demo.scrollPane, BorderLayout.WEST);
	        //displayFrame.add(demo.createTimeControl(), BorderLayout.SOUTH);
	        displayFrame.add(demo.createControlBar(), BorderLayout.SOUTH);
	       
	        
	        
	        //Display the window.
	        displayFrame.setSize(1600, 900);
	        displayFrame.setVisible(true);
	        displayFrame.setMinimumSize(new Dimension(600, 400));
	        
	        return demo;
	    }
		
		//returns JList from createFileList
		public JList<String> createFileList(){
	        //add list to content pane
			return MainFrame.createFileList(mainFrame);
		}
		
		//returns JList from createFileList
		public JList<String> getFileList(MainFrame mainFrame){
	        //add list to content pane
			return mainFrame.fileList;
		}
		
		//sets fileList to dummy contents
		public void setFileListDefault(){
			String[] values = new String[] {
					"Video.mp4", "Audio.mp3", "Media.gif", "Image.png"};
			ArrayList<String> fileNames = new ArrayList<String>();
			
			for(String value : values){
				fileNames.add(value);
			}
			
			mainFrame.fileList.setModel(new DefaultListModel<String>()
			{
				
				
				public ArrayList<String> getFileNames() 
				{
					return fileNames;
				}
				
				public int getSize() 
				{
					return fileNames.size();
				}
				
				public String getElementAt(int index) {
					return getFileNames().get(index);
				}
				
				public void addElement(String newFile){
					getFileNames().add(newFile);
				}
			});
		}
		
		public void setFileList(JList<String> fileList)
		{
			
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
		 
		 //returns the previous file played by the UMP
		 public String getPreviousFile(){
			 return mainFrame.previousFile;
		 }
		 
		 //returns the currentPlayer of the mainFrame
		 public Player getCurrentPlayer(){
			 return mainFrame.currentPlayer;
		 }
		 
		 
		 //returns the current Viewer of the mainFrame
		 public ImageViewer getCurrentViewer(){
			 return mainFrame.currentViewer;
		 }
		 
		 //sets the volumeSlider of the mainFrame to value
		 public void setVolume(double value){
			 mainFrame.volumeSlider.setValue(value);
		 }
		 
			 
		 
		
		//resets all stored values in the TestSuite
		public void reset(){
			mainFrame = null;
		}
	}
}
