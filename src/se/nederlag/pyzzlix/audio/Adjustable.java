package se.nederlag.pyzzlix.audio;

public interface Adjustable {
	public void setVolume(float volume);
	public void setVolume(float volume, float fadeTime);
	public float getVolume();
}
