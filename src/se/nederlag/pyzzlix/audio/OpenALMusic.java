package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.backends.openal.OpenALAudio;

/**
 * @author Anders Andersson
 */
public class OpenALMusic extends com.badlogic.gdx.backends.openal.OpenALMusic implements Music {
	private MusicInputStream input;
	
	public OpenALMusic(OpenALAudio audio, MusicInputStream input) {
		super(audio, null);
		
		this.input = input;
		
		setup(input.getChannels(), input.getSampleRate());
	}

	@Override
	protected int read(byte[] buffer) {
		return this.input.read(buffer);
	}

	@Override
	protected void reset() {
		this.input.reset();
	}

	@Override
	public float getVolume() {
		return this.input.getVolume();
	}

	@Override
	public void setVolume(float volume) {
		this.input.setVolume(volume);
	}

	@Override
	public void setVolume(float volume, float fadeTime) {
		this.input.setVolume(volume, fadeTime);
	}
}
