package se.nederlag.pyzzlix.audio;

import java.nio.ByteOrder;

public class ByteMixer {

	private int channels;
	private int sampleRate;

	static private final boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);

	public ByteMixer(int channels, int sampleRate) {
		this.channels = channels;
		this.sampleRate = sampleRate;
	}
	
	public void mixPlain(byte[] src, byte[] dst) {
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
}
