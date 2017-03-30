
public interface Player 
{
	public void open(String fileName);
	public void volumeChange(int newVolume);
	public void alternatePlayback();
	public void skipPlayback();
	public void changePosition(long playbackPosition);
}
