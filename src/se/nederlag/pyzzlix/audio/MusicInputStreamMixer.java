package se.nederlag.pyzzlix.audio;

import com.badlogic.gdx.audio.analysis.*;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;

public class MusicInputStreamMixer implements MusicInputStream {
	private MusicInputStream[] input;
	private final int lines;
	private final int channels;
	private final int sampleRate;
	private float volume;
	
	static private final int bufferSize = 4096;
	static private final boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);

	private byte[] buffer;
	
	public MusicInputStreamMixer(int lines, int channels, int sampleRate) {
		this.input = new MusicInputStream[lines];
		this.channels = channels;
		this.sampleRate = sampleRate;
		this.lines = lines;
		this.buffer = new byte[bufferSize];
		this.volume = 1.0f;
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

	public int read(byte[] buffer) {
		int length = 0;
		Arrays.fill(buffer, (byte) 0);

		for(int i=0; i<this.lines; i++) {
			if(this.input[i] != null) {
				length = this.input[i].read(this.buffer);
				this.mixBuffers(this.buffer, buffer, this.input[i].getVolume());
			}
		}
		
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
		Gdx.app.log("SOUND ", "Reset time: "+(double)(t*1e-9));
	}
	
	private void setBufferVolume(byte[] src, float volume) {
		int length = src.length;
		int val;
		
		for(int i=0; i<length; i += 2*this.channels) {
			for(int j=0; j<this.channels; j += 1) {
				if (bigEndian) {
					val = ((int)src[i+2*j] << 8)|(int)(src[i+2*j+1]&0x000000000000000000000FF);
				} else {
					val = ((int)src[i+2*j+1] << 8)|(int)(src[i+2*j]&0x000000000000000000000FF);
				}

				val = (int)(val * volume);
				
				if (val > 32767) {
					val = 32767;
				}
				if (val < -32768) {
					val = -32768;
				}
				
				if (bigEndian) {
					src[i+2*j+1] = (byte)(val >>> 8);
					src[i+2*j] = (byte)(val);
				} else {
					src[i+2*j] = (byte)(val);
					src[i+2*j+1] = (byte)(val >>> 8);
				}
			}
		}	}
	
	private void mixBuffers(byte[] src, byte[] dst, float volume) {
		int length = src.length;
		int l_val, r_val, val;
		
		for(int i=0; i<length; i += 2*this.channels) {
			for(int j=0; j<this.channels; j += 1) {
				if (bigEndian) {
					l_val = ((int)src[i+2*j] << 8)|(int)(src[i+2*j+1]&0x000000000000000000000FF);
					r_val = ((int)dst[i+2*j] << 8)|(int)(dst[i+2*j+1]&0x000000000000000000000FF);
				} else {
					l_val = ((int)src[i+2*j+1] << 8)|(int)(src[i+2*j]&0x000000000000000000000FF);
					r_val = ((int)dst[i+2*j+1] << 8)|(int)(dst[i+2*j]&0x000000000000000000000FF);
				}

				l_val = (int)(l_val * volume);
				
				l_val = l_val + 32768;
				r_val = r_val + 32768;
				
				if(l_val < 32768 && r_val < 32768) {
					val = (l_val*r_val)/32768;
				} else {
					val = 2*(l_val+r_val) - (l_val*r_val)/32768 - 65536;
				}
				
				val = val - 32768;
				
				if (val > 32767) {
					val = 32767;
				}
				if (val < -32768) {
					val = -32768;
				}
				
				if (bigEndian) {
					dst[i+2*j+1] = (byte)(val >>> 8);
					dst[i+2*j] = (byte)(val);
				} else {
					dst[i+2*j] = (byte)(val);
					dst[i+2*j+1] = (byte)(val >>> 8);
				}
			}
		}
	}

	@Override
	public float getVolume() {
		return this.volume;
	}

	@Override
	public void setVolume(float volume) {
		this.volume = volume;
	}
}

