package se.nederlag.pyzzlix.audio;

public interface Playable {
	public void play();
	public void pause();
	public void stop();
	public boolean isPlaying();
}
