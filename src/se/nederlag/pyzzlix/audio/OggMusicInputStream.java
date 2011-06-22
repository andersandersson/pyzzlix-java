package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.openal.OggInputStream;
import com.badlogic.gdx.files.FileHandle;

public class OggMusicInputStream implements MusicInputStream {
	private OggInputStream input;
	private FileHandle file;
	
	public OggMusicInputStream(FileHandle file) {
		this.file = file;
		this.input = new OggInputStream(file.read());
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
		Gdx.app.log("AUDIO", "Reset music stream");
		this.input = new OggInputStream(file.read());
	}
}
