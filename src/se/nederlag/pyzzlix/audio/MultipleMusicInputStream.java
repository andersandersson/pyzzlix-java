package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.audio.analysis.*;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;

public class MultipleMusicInputStream implements MusicInputStream {
	private MusicInputStream[] input;
	private final int lines;
	private final int channels;
	private final int sampleRate;
	private float volume;
	private ByteFader fader;
	private ByteMixer mixer;
	
	static private final int bufferSize = 4096*5;
	static private final boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);

	private byte[] buffer;
	
	public MultipleMusicInputStream(int lines, int channels, int sampleRate) {
		this.input = new MusicInputStream[lines];
		this.channels = channels;
		this.sampleRate = sampleRate;
		this.lines = lines;
		this.buffer = new byte[bufferSize];
		this.volume = 1.0f;

		this.fader = new ByteFader(channels, sampleRate);
		this.mixer = new ByteMixer(channels, sampleRate);
	}
	
	public void setStream(int idx, MusicInputStream stream) {
		this.input[idx] = stream;
	}
	
	public int getChannels() {
		return this.channels;
	}

	public int getSampleRate() {
		return this.sampleRate;
	}
	
	public MusicInputStream[] getStreams() {
		return this.input;
	}

	public int read(byte[] buffer) {
		int length = 0;
		Arrays.fill(buffer, (byte) 0);

		for(int i=0; i<this.lines; i++) {
			if(this.input[i] != null) {
				length = this.input[i].read(this.buffer);
				this.mixer.mixPlain(this.buffer, buffer);
			}
		}
		
		this.volume = this.fader.transform(buffer);
		
		return length;
	}

	public void reset() {
		long t = System.nanoTime();
		for(int i=0; i<this.lines; i++) {
			if(this.input[i] != null) {
				this.input[i].reset();
			}
		}
		t = System.nanoTime() - t;
	}


	@Override
	public float getVolume() {
		return this.volume;
	}

	@Override
	public void setVolume(float volume) {
		this.volume = volume;
		this.fader.reset(volume);
	}

	@Override
	public void setVolume(float volume, float fadeTime) {
		this.fader.fade(this.volume, volume, fadeTime);
	}
}

