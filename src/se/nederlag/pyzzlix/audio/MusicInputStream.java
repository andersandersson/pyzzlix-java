package se.nederlag.pyzzlix.audio;

public interface MusicInputStream {
	public int getChannels ();
	public int getSampleRate ();
	public int read(byte[] buffer);
	public void reset();
}
