package se.nederlag.pyzzlix.audio;

public interface Music extends Playable, Adjustable {
	public void setLooping(boolean isLooping);
	public boolean isLooping();
	public float getPosition();
	public void dispose();
}
