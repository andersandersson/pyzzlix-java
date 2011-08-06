package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.files.FileHandle;

public class Mixer 
{	
	public class Sound
	{
		private com.badlogic.gdx.audio.Sound sound; 
		private float volume = 1.0f;
		
		public Sound(com.badlogic.gdx.audio.Sound sound)
		{
			this.sound = sound;
		}
		
		public void play() {
			this.sound.play();
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
	}

	public class Music
	{
		private com.badlogic.gdx.audio.Music music;
		private float volume = 0.0f;

		public Music(com.badlogic.gdx.audio.Music music)
		{
			this.music = music;
		}

		public void play() {
			this.music.play();
		}

		public void pause() {
			this.music.pause();
		}

		public void stop() {
			this.music.stop();
		}

		public boolean isPlaying() {
			return this.music.isPlaying();
		}

		public void setLooping(boolean isLooping) {
			this.music.setLooping(isLooping);
		}

		public boolean isLooping() {
			return this.music.isLooping();
		}

		public void setVolume(float volume) {
			this.music.setVolume(volume);
			this.volume = volume;
		}
		
		public float getVolume() {
			return this.volume;
		}

		public float getPosition() {
			return this.music.getPosition();
		}

		public void dispose() {
			this.music.dispose();
		} 
	
	}	
	
	private class VolumeChange
	{
		public double fromVolume;
		public double fromTime;
		public double toVolume;
		public double toTime;
		public Music music;
		
		public VolumeChange(Music music, double fromVolume, double fromTime, double toVolume, double toTime)
		{
			this.fromVolume = fromVolume;
			this.toVolume = toVolume;
			this.fromTime = fromTime;
			this.toTime = toTime;
			this.music = music;
			
		}
	}	
	
	private static Mixer instance = null;
	private Audio audio = null;
	private double currentTime;
	
	private List<VolumeChange> volumeChanges = new LinkedList<VolumeChange>();
	private List<VolumeChange> deleteSchedule = new LinkedList<VolumeChange>();

	private List<Mixer.Sound> soundsToPlay = new LinkedList<Mixer.Sound>();
	private List<Mixer.Music> musicToPlay = new LinkedList<Mixer.Music>();
	
	public static Mixer getInstance()
	{
		if (instance == null)
			instance = new Mixer();
		return instance;
	}
	
	public Mixer()
	{
		this.audio = Gdx.app.getAudio();
	}
	
	public void update(double deltaTime)
	{
		this.currentTime += deltaTime;
		
		for (Sound s : this.soundsToPlay)
		{
			s.play(s.getVolume());
		}
	
		for (Music m : this.musicToPlay)
		{
			m.play();
		}
		
		this.soundsToPlay.clear();
		this.musicToPlay.clear();
		
		for (VolumeChange vc : this.volumeChanges)
		{
			if (this.currentTime >= vc.toTime)
			{
				vc.music.setVolume((float)vc.toVolume);
				this.deleteSchedule.add(vc);
			}
			else
			{
				double factor = (vc.toTime - this.currentTime) / (vc.toTime - vc.fromTime);
				vc.music.setVolume((float)(vc.toVolume - factor * (vc.toVolume - vc.fromVolume)));
			}
		}
		
		for (VolumeChange vc : this.deleteSchedule)
		{
			this.volumeChanges.remove(vc);
		}
	}
	
	public Sound loadAudioFile(String filename)
	{
		FileHandle handle = Gdx.files.internal(filename);
		
        return new Sound(this.audio.newSound(handle));
	}
	
	public Music loadAudioStream(String filename)
	{
		FileHandle handle = Gdx.files.internal(filename);
		 
        return new Music(this.audio.newMusic(handle));
	}
	
    public void playMusic(Music music, double volume, double fadeinTime, boolean looping)
    {
    	if(music == null) {
    		return;
    	}
    	
    	if (fadeinTime > 0.0)
    	{
    		music.setVolume(0.0f);
    		this.volumeChanges.add(new VolumeChange(music, 
    												0.0, 
    												this.currentTime,
    												volume,
    												this.currentTime  + fadeinTime));
    	}
    	else
    	{
    		music.setVolume((float) volume);		
    	}
    
    	music.setLooping(looping);
    	//this.musicToPlay.add(music);
    	music.play();
    }
        
    public void playSound(Sound sound, double volume)
    {        
    	if(sound == null) {
    		return;
    	}
    	
    	if(false) {
	    	sound.setVolume((float) volume);
	    	this.soundsToPlay.add(sound);
    	}
    }
    
    public void setMusicVolume(Music music, double volume, double fadeTime)
    {
    	if (fadeTime > 0.0)
    	{
    		this.volumeChanges.add(new VolumeChange(music, 
    												music.getVolume(), 
    												this.currentTime,
    												volume,
    												this.currentTime  + fadeTime));
    	}
    	else
    	{
    		music.setVolume((float) volume);		
    	}
    
    }
        
	public void stopMusic(Music music)
	{
		music.stop();
	}
	
	public void pauseMusic(Music music)
	{
		music.pause();
	}      
	
	public void stopSound(Sound sound)
	{
		sound.stop();
	}       

}      
