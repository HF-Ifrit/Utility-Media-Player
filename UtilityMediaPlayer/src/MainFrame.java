import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

//primary GUI window that will interact and control other modules
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu, submenu;
	private JMenuItem menuItem;
	private JRadioButtonMenuItem rbMenuItem;
	private JCheckBoxMenuItem cbMenuItem;
	
	JTextArea output;
    JScrollPane scrollPane;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1040, 543);
		
		
		
		//File OptionsMenu
		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
	}
	
	private JMenuBar createTextMenuBar(){

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu as File tab
		menuBar = MenuBarSetup.attachFileMenu(menuBar);
		
		//build the second menu as a View tab
		menuBar = MenuBarSetup.attachViewMenu(menuBar);
		
		//build the third menu as a Video tab
		menuBar = MenuBarSetup.attachVideoMenu(menuBar);
		
		//build the last menu as help tab
		menuBar =  MenuBarSetup.attachHelpMenu(menuBar);
		
		return menuBar;
	}
	
	
		
	//creates view window
	public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
 
        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);
 
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
 
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
	
	//creates gui 
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MenuLookDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        MainFrame demo = new MainFrame();
        frame.setJMenuBar(demo.createTextMenuBar());
        frame.setContentPane(demo.createContentPane());
 
        //Display the window.
        frame.setSize(1600, 900);
        frame.setVisible(true);
    }
}
