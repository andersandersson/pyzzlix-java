package se.nederlag.pyzzlix.audio;

import java.nio.ByteOrder;

public class ByteFader {
	private int channels;
	private int sampleRate;
	
	private float fromVolume;
	private float toVolume;
	private int samples;
	private int position;
	private boolean fading;

	static private final boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
	
	public ByteFader(int channels, int sampleRate) {
		this.channels = channels;
		this.sampleRate = sampleRate;
		this.fromVolume = 0.0f;
		this.toVolume = 1.0f;
		this.fading = false;
	}
	
	public void fade(float fromVolume, float toVolume, float fadeTime) {
		this.fromVolume = fromVolume;
		this.toVolume = toVolume;
		this.samples = (int) (fadeTime*this.sampleRate);
		this.position = 0;
		this.fading = true;
	}
	
	public void reset(float volume) {
		this.fromVolume = volume;
		this.toVolume = volume;
		this.samples = 0;
		this.position = 0;
		this.fading = false;
	}

	public float skip(int length) {
		float volume = this.toVolume;
		
		if(this.fading) {
			this.position += length;
			
			if(this.position < this.samples) {
				volume = this.fromVolume + (this.toVolume - this.fromVolume) * ((float)this.position / (float)this.samples);
			} else {
				volume = this.toVolume;
				this.fading = false;
			}
		}
		
		return volume;
	}
	
	public float transform(byte[] in) {
		float volume = 0.0f;
		int val;
		
		for(int i=0; i<in.length; i += 2*this.channels) {
			if(this.position < this.samples) {
				volume = this.fromVolume + (this.toVolume - this.fromVolume) * ((float)this.position / (float)this.samples);
			} else {
				volume = this.toVolume;
				this.fading = false;
			}

			for(int j=0; j<this.channels; j++) {
				if (bigEndian) {
					val = ((int)in[i+2*j] << 8)|(int)(in[i+2*j+1]&0x000000000000000000000FF);
				} else {
					val = ((int)in[i+2*j+1] << 8)|(int)(in[i+2*j]&0x000000000000000000000FF);
				}

				val = (int)(val * volume);
				
				if (val > 32767) {
					val = 32767;
				}
				if (val < -32768) {
					val = -32768;
				}
				
				if (bigEndian) {
					in[i+2*j+1] = (byte)(val >>> 8);
					in[i+2*j] = (byte)(val);
				} else {
					in[i+2*j] = (byte)(val);
					in[i+2*j+1] = (byte)(val >>> 8);
				}
			}

			this.position += 1;				
		}
		
		return volume;
	}
	
	public boolean isFading() {
		return this.fading;
	}
	
	public boolean idle() {
		if(!this.fading) {
			return (this.toVolume < 0.01f) || (this.toVolume > 0.99f); 
		} else {
			return false;
		}
	}
}
