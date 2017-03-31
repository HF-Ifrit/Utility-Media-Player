
public interface Player 
{
	public boolean open(String fileName);
	public boolean clear();
	public void volumeChange(int newVolume);
	public void alternatePlayback();
	public void skipPlayback();
	public void changePosition(long playbackPosition);
}
