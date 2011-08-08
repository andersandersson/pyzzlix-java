package se.nederlag.pyzzlix.audio;

public class OpenALSound implements Sound {
	private com.badlogic.gdx.audio.Sound sound; 
	private float volume = 1.0f;
	
	public OpenALSound(com.badlogic.gdx.audio.Sound sound)
	{
		this.sound = sound;
	}
	
	public void play() {
		this.sound.play(this.volume);
	}

	public void play(float volume) {
		this.sound.play(volume);
	}

	public void stop() {
		this.sound.stop();
	}	

	public void dispose() {
		this.sound.dispose();	
	}
	
	public float getVolume()
	{
		return this.volume;
	}
	
	public void setVolume(float volume)
	{
		this.volume = volume;
	}

	public void pause() {
	}

	public boolean isPlaying() {
		return false;
	}

	@Override
	public void setVolume(float volume, float fadeTime) {
		this.volume = volume;
	}
}
