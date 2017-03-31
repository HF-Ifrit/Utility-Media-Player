
public interface Player 
{
	public void open(String fileName);
	public void volumeChange(double newVolume);
	public void alternatePlayback();
	public void skipPlayback();
	public void changePosition(long playbackPosition);
}
