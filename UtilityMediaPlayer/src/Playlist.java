import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Playlist {
	private ArrayList<URI> tracks;
	private MainFrame frame;
	
	/*If this file is valid, we add its URI to the internal list of URIs. */
	public void addTrack(String fileName) {
		if (fileName != null) {
			URI uri = new File(fileName).toURI();
			String name = new File(fileName).toURI().toString();
			if (name.endsWith("mp3") || name.endsWith("mp4") || name.endsWith("webm")) {
				tracks.add(uri);
			}
		}
	}
	
	public void savePlaylist(String fileName) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter( new File(fileName)));
			for (URI track : tracks) {
				writer.write(track.toString());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error saving playlist.");
		}
		
		
	}
	
	public Playlist loadPlaylist(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			String currentLine = null;
			while ((currentLine = reader.readLine()) != null) {
				if (! (currentLine = currentLine.trim()).equals("")) {
					try {
						tracks.add(new URI(currentLine));
					} catch (URISyntaxException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Error loading playlist.");
					}
				}
			}
			return this;
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error loading playlist.");
		}
		return null;
	}
	
	
	public ArrayList<URI> getTracks() {
		return tracks;
	}
	
	public Playlist(ArrayList<URI> uriList, MainFrame frame) {
		this.tracks = uriList;
		this.frame = frame;
	}
	
	public Playlist(MainFrame frame) {
		this.frame = frame;
	}
	
}
