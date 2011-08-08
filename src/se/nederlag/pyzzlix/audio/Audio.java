package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.openal.OpenALAudio;
import com.badlogic.gdx.files.FileHandle;

public class Audio {
	public static Sound loadAudioFile(String filename) {
		FileHandle handle = Gdx.files.internal(filename);
		
        return new OpenALSound(Gdx.audio.newSound(handle));
	}

	public static Music loadAudioStream(String filename) {
		FileHandle handle = Gdx.files.internal(filename);
		
		OggMusicInputStream stream = new OggMusicInputStream(handle);
		
		return new OpenALMusic((OpenALAudio) Gdx.audio, stream);
	}

	public static MultipleMusic loadMultipleAudioStream(String[] filenames) {
		OggMusicInputStream[] streams = new OggMusicInputStream[filenames.length];
		
		for(int i=0; i<filenames.length; i++) {
			FileHandle handle = Gdx.files.internal(filenames[i]);
			streams[i] = new OggMusicInputStream(handle);
		}
		
		MultipleMusicInputStream multipleStream = new MultipleMusicInputStream(streams.length, streams[0].getChannels(), streams[0].getSampleRate());
		
		for(int i=0; i<streams.length; i++) {
			multipleStream.setStream(i, streams[i]);
		}
		
		return new MultipleOpenALMusic((OpenALAudio) Gdx.audio, multipleStream);
    }
	
	public static void playMusic(Music music, float volume, float fadeTime) {
		music.setVolume(0.0f);
		music.setVolume(volume, fadeTime);
		music.play();
	}
	
	public static void playMusic(Music music) {
		music.setVolume(1.0f);
		music.play();
	}

	public static void stopMusic(Music music) {
		music.stop();
	}

	public static void playSound(Sound sound) {
		sound.play();
	}

	public static void stopSound(Sound sound) {
		sound.stop();
	}
	
	public static void setMusicVolume(float volume) {		
	}

	public static float getMusicVolume() {
		return 1.0f;
	}

	public static void setSoundVolume(float volume) {		
	}

	public static float getSoundVolume() {
		return 1.0f;
	}
}
