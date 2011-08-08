package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.backends.openal.OpenALAudio;

public class MultipleOpenALMusicStream extends OpenALMusicStream implements MultipleMusic {
	private MultipleMusicInputStream input;
	
	public MultipleOpenALMusicStream(OpenALAudio audio, MultipleMusicInputStream input) {
		super(audio, input);
		
		this.input = input;
	}

	public Adjustable[] getParts() {
		return this.input.getStreams();
	}
}
