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

	/**creates the fileMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the file menu section to
	 */
	public static JMenuBar attachFileMenu(JMenuBar menuBar){
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
        menu.add(menuItem);
        
        //OpenMultipleFiles
        menuItem = new JMenuItem("Open Multiple Files",
                KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_M, ActionEvent.CTRL_MASK ));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens multiple files from explorer");
        menu.add(menuItem);
        
        //SaveFile
        menuItem = new JMenuItem("Save File",
                KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Opens a file explorer to save loaded file into the local space");
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
        menu.setMnemonic(KeyEvent.VK_W);
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
                KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_C, ActionEvent.CTRL_MASK));
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
        menu.setMnemonic(KeyEvent.VK_V);
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
