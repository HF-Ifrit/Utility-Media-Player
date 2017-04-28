import java.awt.Component;

public interface Player 
{
	public boolean open(String fileName);
	public boolean openFullFilePath(String fileName);
	public boolean clear();
	public void volumeChange(double newVolume);
	public void alternatePlayback();
	public void skipPlayback();
	public void changePosition(long playbackPosition);
	public Component showView();
}
