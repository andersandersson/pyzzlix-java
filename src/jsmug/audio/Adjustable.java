package jsmug.audio;

public interface Adjustable {
	@Attribute(value="volume", access="set")
	public void setVolume(double volume);

	@Attribute(value="volume", access="get")
	public double getVolume();

	@Attribute(value="pitch", access="set")
	public void setPitch(double pitch);	

	@Attribute(value="pitch", access="get")
	public double getPitch();
	
	public void fadeIn(double duration);
	public void fadeOut(double duration);
	public void fadeTo(double duration, double volume);
}
