package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.backends.openal.OpenALAudio;

public class MultipleOpenALMusic extends OpenALMusic implements MultipleMusic {
	private MultipleMusicInputStream input;
	
	public MultipleOpenALMusic(OpenALAudio audio, MultipleMusicInputStream input) {
		super(audio, input);
		
		this.input = input;
	}

	public Adjustable[] getParts() {
		return this.input.getStreams();
	}
}
