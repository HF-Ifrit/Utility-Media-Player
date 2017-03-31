import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.*;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

//primary GUI window that will interact and control other modules
public class MainFrame extends JFrame {

	private JList list;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu, submenu;
	private JMenuItem menuItem;
	private JRadioButtonMenuItem rbMenuItem;
	private JCheckBoxMenuItem cbMenuItem;
	private Button playButton;
	
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
        frame.getContentPane().add(createFileList(), BorderLayout.WEST);
        frame.add(demo.createControlBar(), BorderLayout.SOUTH);
 
        //Display the window.
        frame.setSize(1600, 900);
        frame.setVisible(true);
        
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
	
	//creates the sideView for 
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
	
	
	
		
	//creates view window
	public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(false);
 
        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);
       
 
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        contentPane.setVisible(true);
 
        return contentPane;
    }
	
	//creates content
	public void addJList(){
		
        //create list area
        list = createFileList();
        
        //add list to content pane
		getContentPane().add(list, BorderLayout.SOUTH);
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

    	hbox.getChildren().add(currentButton);
    	
        
        //adds PlayButton image and button
    	Image playImage = new Image(getClass().getResourceAsStream("/internaldata/Play.png"));
    	Button playButtonTemp = new Button();
    	playButton = playButtonTemp;
    	imageview = new ImageView(playImage);
    	imageview.setFitHeight(50);
    	imageview.setFitWidth(50);
    	playButtonTemp.setGraphic(imageview);
    	
    	hbox.getChildren().add(playButtonTemp);
    	
    	
    	
    	//add forward button
    	currentImage = new Image(getClass().getResourceAsStream("/internaldata/Forward.png"));
     	currentButton = new Button();
     	imageview = new ImageView(currentImage);
     	imageview.setFitHeight(50);
     	imageview.setFitWidth(50);
     	currentButton.setGraphic(imageview);

     	hbox.getChildren().add(currentButton);
     	
     	//add volume control button
     	volume = createSlider("Volume: ", null, 0, 4, grid);
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isValueChanging()) {
					volumeChange(player);
				}
			}
		});
    	
    	
    	return hbox;
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
			System.out.println("Opening file....");
			
		}
		
	}
	
	//controller for opening multiple files
	public class openMultipleFiles implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Opening multiple files!");
				
			}
			
	}
	
	//controller for opening multiple files
	public class openSaveFile implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			e.getSource();
			System.out.println("Saving file.....");

		}

	}
	
	/**
	 * 
	 * internal testing class
	 */
	public class testSuite{
		
	}
}
