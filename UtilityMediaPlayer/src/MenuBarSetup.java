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

//Abstract Factory that allows
public interface MenuBarSetup {


	/**creates the fileMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the file menu section to
	 */
	public JMenuBar attachFileMenu(JMenuBar menuBar, MainFrame mainFrame);
	
	/**creates the viewMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the view menu section to
	 */
	public JMenuBar attachViewMenu(JMenuBar menuBar);
	
	/**creates the videoMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the video menu section to
	 */
	public JMenuBar attachVideoMenu(JMenuBar menuBar);
	
	public JMenuBar attachAudioMenu(JMenuBar menuBar);
	
	//Image MenuBar
	public JMenuBar attachImageMenu(JMenuBar menuBar);
	
	
	
	/**
	 * creates the helpMenu tab and attaches it to input
	 * @param menuBar the JMenuBar that the function will attach the help menu section to
	 */
	public JMenuBar attachHelpMenu(JMenuBar menuBar);
	
	/**
	 * attaches player buttons to lower panel
	 */
	
	
}
