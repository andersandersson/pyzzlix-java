package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.openal.OggInputStream;
import com.badlogic.gdx.files.FileHandle;

public class OggMusicInputStream implements MusicInputStream {
	private OggInputStream input;
	private FileHandle file;
	private float volume;
	
	public OggMusicInputStream(FileHandle file) {
		this.file = file;
		this.input = new OggInputStream(file.read());
		this.volume = 1.0f;
	}
	
	@Override
	public int getChannels() {
		return this.input.getChannels();
	}

	@Override
	public int getSampleRate() {
		return this.input.getSampleRate();
	}

	@Override
	public int read(byte[] buffer) {
		return this.input.read(buffer);
	}

	@Override
	public void reset() {
		this.input = new OggInputStream(file.read());
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getVolume() {
		return this.volume;
	}
}
