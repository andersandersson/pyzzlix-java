package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.backends.openal.*;

/**
 * @author Anders Andersson
 */
public class OpenALMusicStream extends OpenALMusic {
	private MusicInputStream input;
	
	public OpenALMusicStream(OpenALAudio audio, MusicInputStream input) {
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
}
