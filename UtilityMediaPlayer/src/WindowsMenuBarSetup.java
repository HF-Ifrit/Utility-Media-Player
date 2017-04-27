import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

//class that allows menuBars tabs to be attached to a given menuBar
public class WindowsMenuBarSetup implements MenuBarSetup{
	
	/**
	 * 
	 * TODO
	 * 1. create parameter to each attach function to take in a variable array of actionListeners and attach them in order to all the menu items
	 * 2. create a validation check method for attaching actionlisteners to menu items
	 */

	/**creates the fileMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the file menu section to
	 */
	public JMenuBar attachFileMenu(JMenuBar menuBar, MainFrame mainFrame){
		//temporary loading spaces on creation
		JMenu menu, submenu;
		JMenuItem menuItem;
		
		
		//File Tab
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
                "File management tab");
        menuBar.add(menu);
 
        //OpenFile
        menuItem = new JMenuItem("Open File",
                KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens a file explorer to find file location");

        menuItem.addActionListener(mainFrame.new openFile());
        menu.add(menuItem);
        
        //OpenFile
        menuItem = new JMenuItem("Import File to Library",
                KeyEvent.VK_I);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Save the current file to the UMP library.");

        menuItem.addActionListener(mainFrame.new saveFile());
        menu.add(menuItem);
        
        //Play option
        menuItem = new JMenuItem("Play",
                KeyEvent.VK_1);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens a file explorer to find file location");

        menuItem.addActionListener(mainFrame.new play());
        menu.add(menuItem);
        
        
        
        //AddToPlaylist
        menuItem = new JMenuItem("Add To Playlist",
                KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Adds current loaded files to playlist");
        menuItem.addActionListener(mainFrame.new addToPlaylist());
        menu.add(menuItem);
        
        
        //SavePlaylistToFile
        menuItem = new JMenuItem("Save Playlist",
                KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Permanently saves file to a file location");
        menuItem.addActionListener(mainFrame.new savePlaylist());
        menu.add(menuItem);
        
        //Order files by type
        menuItem = new JMenuItem("Order by Type",
                KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Rearranges the file list in order of media types.");
        menuItem.addActionListener(mainFrame.new orderFileListByType());
        menu.add(menuItem);
        
        //Order files by name
        menuItem = new JMenuItem("Order by Name",
                KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Rearranges the file list in alphabetical order.");
        menuItem.addActionListener(mainFrame.new orderFileListByName());
        menu.add(menuItem);
        
        //
        
        /**
         * TODO 
         * IMAGE BASE
         * 
        menuItem = new JMenuItem("Both text and icon",
                new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_B);
        menu.add(menuItem);
 
        menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menu.add(menuItem);
        */
        
        return menuBar;
	}
	
	/**creates the viewMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the view menu section to
	 */
	public JMenuBar attachViewMenu(JMenuBar menuBar, MainFrame mainFrame){
		//temporary loading spaces on creation
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		
		//View Tab
        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                "GUI View options");
        menuBar.add(menu);
 
        //ViewPlaylist
        menuItem = new JMenuItem("Open Playlist",
                KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens a file explorer to find file location");
        menuItem.addActionListener(mainFrame.new openPlaylist());
        menu.add(menuItem);
        
        
        //View Options directly added into the GUI
        menu.addSeparator();
        
        
        //ShowFullScreen
        cbMenuItem = new JCheckBoxMenuItem("Fullscreen");
        cbMenuItem.setMnemonic(KeyEvent.VK_F);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        cbMenuItem.getAccessibleContext().setAccessibleDescription(
        		"Puts GUI into a fullscreen mode");
        cbMenuItem.addActionListener(mainFrame.new fullscreen());
        menu.add(cbMenuItem);
        
        //ShowFiles
        cbMenuItem = new JCheckBoxMenuItem("Hide Items");
        cbMenuItem.setMnemonic(KeyEvent.VK_I);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        cbMenuItem.getAccessibleContext().setAccessibleDescription(
        		"Displays the available files in the left sidebar");
        cbMenuItem.addActionListener(mainFrame.new hideItems());
        menu.add(cbMenuItem);
        

        //ShowPlaylists Bar
        cbMenuItem = new JCheckBoxMenuItem("Playlists");
        cbMenuItem.setMnemonic(KeyEvent.VK_L);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        menu.add(cbMenuItem);
        
       
        
        
        
		return menuBar;
	}
	
	/**creates the videoMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the video menu section to
	 */
	public JMenuBar attachVideoMenu(JMenuBar menuBar, MainFrame mainFrame){
		//temporary loading spaces on creation
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JRadioButtonMenuItem rbMenuItem;
		
		//Video Tab
        menu = new JMenu("Video");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription(
                "GUI View options");
        menuBar.add(menu);
        
        //ShowFullScreen
        cbMenuItem = new JCheckBoxMenuItem("Fullscreen");
        cbMenuItem.setMnemonic(KeyEvent.VK_F);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_1, ActionEvent.ALT_MASK));
        cbMenuItem.getAccessibleContext().setAccessibleDescription(
        		"Puts video window into its own Fullscreen mode");
        menu.add(cbMenuItem);
        
        menu.addSeparator();
        
        //Take Snap Shot
        menuItem = new JMenuItem("Snap Shot");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Saves an image of video screen at moment");
        menuItem.addActionListener(mainFrame.new capture());
        menu.add(menuItem);
        
        //Convert to GIF
        menuItem = new JMenuItem("Convert to GIF...");
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Converts set portion of video from moment into a GIF");
        menuItem.addActionListener(mainFrame.new gifClip());
        menu.add(menuItem);
        
        //Extract audio track
        menuItem = new JMenuItem("Extract Audio Track...");
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Saves the audio track of the currently playing video to the output folder");
        menuItem.addActionListener(mainFrame.new extractAudio());
        menu.add(menuItem);
        
        //Clip video
        menuItem = new JMenuItem("Clip Video...");
        menuItem.getAccessibleContext().setAccessibleDescription("Create a clip of the currently playing video");
        menuItem.addActionListener(mainFrame.new clipVideo());
        menu.add(menuItem);
        //Video display options
        //menu.addSeparator();
        
        
        
        
/*        *//**
         * Aspect Ratio
         *//*
        submenu = new JMenu("Aspect Ratio");
        submenu.setMnemonic(KeyEvent.VK_R);

        //Aspects

        ButtonGroup group2 = new ButtonGroup();
        
        rbMenuItem = new JRadioButtonMenuItem("Default");
        rbMenuItem.setSelected(true);
        group2.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("16:9");
        group2.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("4:3");
        group2.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("16:10");
        group2.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("1:1");
        group2.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        //add Aspect Ratios to main view
        menu.add(submenu);
        
        *//**
         * Crop Ratios
         *//*
        submenu = new JMenu("Crop");
        submenu.setMnemonic(KeyEvent.VK_C);

        //Crop 

        ButtonGroup group3 = new ButtonGroup();
        
        rbMenuItem = new JRadioButtonMenuItem("Default");
        rbMenuItem.setSelected(true);
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("16:9");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("4:3");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("16:10");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("5:4");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("1:1");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        //add Crop Ratios to main view
        menu.add(submenu);*/
        
        
		
		return menuBar;
	}
	
	public JMenuBar attachAudioMenu(JMenuBar menuBar, MainFrame mainFrame){
		//temporary loading spaces on creation
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JRadioButtonMenuItem rbMenuItem;
		
		
		
		//Audio Tab
        menu = new JMenu("Audio");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "GUI Audio options");
        menuBar.add(menu);
        
      //Mute
        cbMenuItem = new JCheckBoxMenuItem("Mute");
        cbMenuItem.setMnemonic(KeyEvent.VK_M);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_4, ActionEvent.ALT_MASK));
        cbMenuItem.getAccessibleContext().setAccessibleDescription(
        		"Mutes audio player");
        menu.add(cbMenuItem);
        
        
        //Increase Volume
        menuItem = new JMenuItem("Increase Volume");
        menuItem.setMnemonic(KeyEvent.VK_I);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_5, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Raises volume level of audio player");
        menu.add(menuItem);
        
        //Decrease Volume
        menuItem = new JMenuItem("Decrease Volume");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_6, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Lowers volume level of audio player");
        menu.add(menuItem);
        
        menu.addSeparator();
        
        //AudioClip
        menuItem = new JMenuItem("Create Audio Clip");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_7, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Save a portion of the Audio Clip from a given point to a set endpoint");
        menu.add(menuItem);
        
        menu.addSeparator();
        
        
        /**
         * Stereo output
         */
        submenu = new JMenu("Stereo Mode");
        submenu.setMnemonic(KeyEvent.VK_S);

        //Stereo

        ButtonGroup group3 = new ButtonGroup();
        
        rbMenuItem = new JRadioButtonMenuItem("Stereo");
        rbMenuItem.setSelected(true);
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("Left");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("Right");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("Reverse Stereo");
        group3.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        //add to menu
        menu.add(submenu);
		
		return menuBar;
	}
	
	
	//Image MenuBar
	public JMenuBar attachImageMenu(JMenuBar menuBar, MainFrame mainFrame){
		//temporary loading spaces on creation
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JRadioButtonMenuItem rbMenuItem;
		
		
		
		//Image Tab
        menu = new JMenu("Image");
        menu.setMnemonic(KeyEvent.VK_I);
        menu.getAccessibleContext().setAccessibleDescription(
                "GUI Image options");
        menuBar.add(menu);
        
        //Transform submenu
        submenu = new JMenu("Transform");
        submenu.setMnemonic(KeyEvent.VK_T);
        
        menuItem = new JMenuItem("Flip Horizontally");
        menuItem.addActionListener(mainFrame.new flip(true));
        submenu.add(menuItem);
        
        menuItem = new JMenuItem("Flip Vertically");
        menuItem.addActionListener(mainFrame.new flip(false));
        submenu.add(menuItem);
        
        submenu.addSeparator();
        
        menuItem = new JMenuItem("Rotate Clockwise 90°");
        menuItem.addActionListener(mainFrame.new rotate(true));
        submenu.add(menuItem);
        
        menuItem = new JMenuItem("Rotate Counter-Clockwise 90°");
        menuItem.addActionListener(mainFrame.new rotate(false));
        
        submenu.add(menuItem);
        
        
        menu.add(submenu);
        
        menu.addSeparator();
        
        
        
      	//Image Properties
        menuItem = new JMenuItem("Image Properties",
                KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens window with help document/info");
        menuItem.addActionListener(mainFrame.new imageProperties());
        menu.add(menuItem);
        
        menu.addSeparator();
        
      	//Gif to Video
        menuItem = new JMenuItem("Convert Gif to Video",
                KeyEvent.VK_V);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"If a .gif file is open, convert it to a new .mp4.");
        menuItem.addActionListener(mainFrame.new gifToVideo());
        menu.add(menuItem);
        
        
       
        menu.add(menuItem);
		
		return menuBar;
	}
	
	
	
	/**
	 * creates the helpMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the help menu section to
	 */
	public JMenuBar attachHelpMenu(JMenuBar menuBar, MainFrame mainFrame){
		//temporary loading spaces on creation
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		
		//Video Tab
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription(
                "GUI View options");
        menuBar.add(menu);
        
        //Help
        menuItem = new JMenuItem("Help",
                KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_F1, 0));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens window with help document/info");
        menuItem.addActionListener(mainFrame.new openHelpMenu());
        menu.add(menuItem);
        
        menu.addSeparator();
        
        //About
        menuItem = new JMenuItem("About",
                KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_F1, ActionEvent.SHIFT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens window with information about product and creators");
        menuItem.addActionListener(mainFrame.new openAboutInfo());
        menu.add(menuItem);

		return menuBar;
	}


	
	/**
	 * attaches player buttons to lower panel
	 */
	
	
}
