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
public class MenuBarSetup {
	
	/**
	 * TODO
	 * 1. create parameter to each attach function to take in a variable array of actionListeners and attach them in order to all the menu items
	 * 2. create a validation check method for attaching actionlisteners to menu items
	 */

	/**creates the fileMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the file menu section to
	 */
	public static JMenuBar attachFileMenu(JMenuBar menuBar, MainFrame mainframe){
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
        /**
         * TODO demo
         * change listener to not be coupled
         */
        menuItem.addActionListener(mainframe.new openFile());
        menu.add(menuItem);
        
        //OpenMultipleFiles
        menuItem = new JMenuItem("Open Multiple Files",
                KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_M, ActionEvent.CTRL_MASK ));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens multiple files from explorer");
        /**
         * TODO demo
         * change listener to not be coupled
         */
        menuItem.addActionListener(mainframe.new openMultipleFiles());
        menu.add(menuItem);
        
        //SaveFile
        menuItem = new JMenuItem("Save File",
                KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens a file explorer to save loaded file into the local space");
        /**
         * TODO demo
         * change listener to not be coupled
         */
        menuItem.addActionListener(mainframe.new openSaveFile());
        menu.add(menuItem);
        
      //ConvertFile
        menuItem = new JMenuItem("Convert File",
                KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Allows a current loaded file to be converted to another format");
        menu.add(menuItem);
        
        //AddToPlaylist
        menuItem = new JMenuItem("Add To Playlist",
                KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Adds current loaded files to playlist");
        menu.add(menuItem);
        
        
        //SavePlayList
        menuItem = new JMenuItem("Save Playlist",
                KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Saves playlist in a temporary cache that can be used later");
        menu.add(menuItem);
        
        //SavePlaylistToFile
        menuItem = new JMenuItem("Save Playlist To File",
                KeyEvent.VK_F);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Permanently saves file to a file location");
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
	public static JMenuBar attachViewMenu(JMenuBar menuBar){
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
                KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens a file explorer to find file location");
        menu.add(menuItem);
        
      //SwapScreens
        menuItem = new JMenuItem("Swap Screens",
                KeyEvent.VK_W);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Swaps Left Window location with Right Window location");
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
        menu.add(cbMenuItem);
        
        //ShowFiles
        cbMenuItem = new JCheckBoxMenuItem("File Items");
        cbMenuItem.setMnemonic(KeyEvent.VK_I);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        cbMenuItem.getAccessibleContext().setAccessibleDescription(
        		"Displays the available files in the left sidebar");
        menu.add(cbMenuItem);
        
        //ShowSatuses
        cbMenuItem = new JCheckBoxMenuItem("Statuses");
        cbMenuItem.setMnemonic(KeyEvent.VK_U);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_U, ActionEvent.CTRL_MASK));
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
	public static JMenuBar attachVideoMenu(JMenuBar menuBar){
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
        menu.add(menuItem);
        
        //Convert to GIF
        menuItem = new JMenuItem("Convert to GIF");
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Converts set portion of video from moment into a GIF");
        menu.add(menuItem);
        
        //Video display options
        menu.addSeparator();
        
        /**
         * Zoom 
         */
        submenu = new JMenu("Zoom");
        submenu.setMnemonic(KeyEvent.VK_Z);

        //Zoom rates

        ButtonGroup group = new ButtonGroup();
        
        rbMenuItem = new JRadioButtonMenuItem("10%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
 
        rbMenuItem = new JRadioButtonMenuItem("25%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("50%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("75%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("100%");
        rbMenuItem.setSelected(true);
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("125%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("150%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("200%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("300%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("400%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("500%");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        
        
        //add Zoom to main view
        menu.add(submenu);
        
        
        /**
         * Aspect Ratio
         */
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
        
        /**
         * Crop Ratios
         */
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
        menu.add(submenu);
        
        
		
		return menuBar;
	}
	
	public static JMenuBar attachAudioMenu(JMenuBar menuBar){
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
	
	public static JMenuBar attachImageMenu(JMenuBar menuBar){
		//temporary loading spaces on creation
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JRadioButtonMenuItem rbMenuItem;
		
		
		
		//Audio Tab
        menu = new JMenu("Image");
        menu.setMnemonic(KeyEvent.VK_I);
        menu.getAccessibleContext().setAccessibleDescription(
                "GUI Image options");
        menuBar.add(menu);
        
      //Mute
        cbMenuItem = new JCheckBoxMenuItem("Duplicate");
        cbMenuItem.setMnemonic(KeyEvent.VK_M);
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_8, ActionEvent.ALT_MASK));
        cbMenuItem.getAccessibleContext().setAccessibleDescription(
        		"Mutes audio player");
        menu.add(cbMenuItem);
        
        
        //Colormode submenu
        submenu = new JMenu("Mode");
        submenu.setMnemonic(KeyEvent.VK_M);
        
        ButtonGroup group = new ButtonGroup();
        
        rbMenuItem = new JRadioButtonMenuItem("RGB");
        rbMenuItem.setSelected(true);
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("Grayscale");
        group.add(rbMenuItem);
        submenu.add(rbMenuItem);
        
        menu.add(submenu);
        
        
        //Transform submenu
        submenu = new JMenu("Transform");
        submenu.setMnemonic(KeyEvent.VK_T);
        
        menuItem = new JMenuItem("Flip Horizontally");
        submenu.add(menuItem);
        
        menuItem = new JMenuItem("Flip Vertically");
        submenu.add(menuItem);
        
        submenu.addSeparator();
        
        menuItem = new JMenuItem("Rotate Clockwise 90°");
        submenu.add(menuItem);
        
        menuItem = new JMenuItem("Rotate Counter-Clockwise 90°");
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
        menu.add(menuItem);
        
        
        
       
        menu.add(menuItem);
		
		return menuBar;
	}
	
	
	
	/**creates the helpMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the help menu section to
	 */
	public static JMenuBar attachHelpMenu(JMenuBar menuBar){
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
        menu.add(menuItem);
        
        menu.addSeparator();
        
        //About
        menuItem = new JMenuItem("About",
                KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_F1, ActionEvent.SHIFT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens window with information about product and creators");
        menu.add(menuItem);
        
        menu.addSeparator();
		
		return menuBar;
	}
}
