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
	private String name;
	
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
		String path = "playlists";
		File playlists = new File(path);
		try {
			if (!playlists.exists()) {
				playlists.mkdirs();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter( new File("playlists" + System.getProperty("file.separator") + fileName + ".txt")));
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
			BufferedReader reader = new BufferedReader(new FileReader(new File("playlists" + System.getProperty("file.separator") +
					fileName)));
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
			this.name = fileName;
			reader.close();
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
	
	public String getName() {
		return name;
	}
	
	public Playlist(ArrayList<URI> uriList, MainFrame frame, String name) {
		this.tracks = uriList;
		this.frame = frame;
		this.name = name;
	}
	
	public Playlist(MainFrame frame, String name) {
		this.frame = frame;
		this.tracks = new ArrayList<>();
		this.name = name;
	}
	
	public Playlist(MainFrame frame) {
		this.frame = frame;
		this.tracks = new ArrayList<>();
	}
	
}
