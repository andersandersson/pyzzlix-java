package se.nederlag.pyzzlix.audio;

public interface MusicInputStream extends Adjustable {
	public int getChannels ();
	public int getSampleRate ();
	public int read(byte[] buffer);
	public void reset();
}
