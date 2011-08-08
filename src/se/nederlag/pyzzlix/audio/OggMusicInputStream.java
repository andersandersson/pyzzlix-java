package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.openal.OggInputStream;
import com.badlogic.gdx.files.FileHandle;

public class OggMusicInputStream implements MusicInputStream {
	private OggInputStream input;
	private FileHandle file;
	private float volume;
	private ByteFader fader;
	
	public OggMusicInputStream(FileHandle file) {
		this.file = file;
		this.input = new OggInputStream(file.read());
		this.volume = 1.0f;
		this.fader = new ByteFader(this.input.getChannels(), this.input.getSampleRate());
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
		int length = this.input.read(buffer);
		
		this.volume = this.fader.transform(buffer);
		
		return length;
	}

	@Override
	public void reset() {
		this.input = new OggInputStream(file.read());
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		this.fader.reset(volume);
	}

	public float getVolume() {
		return this.volume;
	}

	@Override
	public void setVolume(float volume, float fadeTime) {
		this.fader.fade(this.volume, volume, fadeTime);
	}
}
