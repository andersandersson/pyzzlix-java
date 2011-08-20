package jsmug.audio;

public interface Sound extends PCMInfo, Adjustable {
	public void play();
	public void pause();
	public void resume();
	public void stop();

	public void setLooping(boolean looping);
	
	public boolean isStream();
	public boolean isLooping();
	public boolean isPlaying();
	public boolean isPaused();
	public boolean isStopped();
	
	public boolean seek(int position);	
}