package jsmug.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public final class PCMUtils {
	static private final boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);

	private PCMUtils() {		
	}
	
	public static void convertFloatToByte(FloatBuffer src, ByteBuffer dst) {
		float next;
		
		while(src.hasRemaining()) {
			next = src.get();
			
			int val = (int)(next*32768);

			if (val > 32767) {
				val = 32767;
			}
			if (val < -32768) {
				val = -32768;
			}

			if (PCMUtils.bigEndian) {
				dst.put((byte)(val >>> 8));
				dst.put((byte)(val));
			} else {
				dst.put((byte)(val));
				dst.put((byte)(val >>> 8));
			}
		}
	}

	
	// Copy src to dst with respect to volume fade
	public static void copyBuffer(FloatBuffer src, FloatBuffer dst, double startVolume, double endVolume, long volumeSamples, int channels) {
		int limit = src.limit();
		double srcValue, volume;
		
		for(int i=0; i<limit; i+=channels) {
			for(int j=0; j<channels; j++) {
				if((i/channels) < volumeSamples) {
					volume = startVolume + (endVolume - startVolume)*((double) (i/channels))/((double) volumeSamples);
					srcValue = (src.get(i+j)*volume);
				} else {
					srcValue = (src.get(i+j)*endVolume);
				}
				
				dst.put(i+j, (float) srcValue);
			}
		}			
	}
	
	
	// Mix two buffers
	public static void mixBuffers(FloatBuffer src, FloatBuffer dst, double startVolume, double endVolume, long volumeSamples, int channels) {
		int limit = src.limit();
		double srcValue, dstValue, volume;
		
		for(int i=0; i<limit; i+=channels) {
			for(int j=0; j<channels; j++) {
				srcValue = src.get(i+j);
				dstValue = dst.get(i+j);

				if((i/2) < volumeSamples) {
					volume = startVolume + (endVolume - startVolume)*((double) (i/channels))/((double) volumeSamples);
					dstValue += (srcValue*volume);
				} else {
					dstValue += (srcValue*endVolume);
				}
				
				dst.put(i+j, (float) dstValue);
			}
		}		
	}
	
	// Compress a buffer by dividing with its RMS (root mean square = sqrt(x1*x1+...+xn*cn)
	public static void compressBufferRMS(FloatBuffer dst) {
		int limit = dst.limit();
		double RMS = 0.0, value;
		
		for(int i=0; i<limit; i++) {
			value = dst.get(i);
			RMS += value*value;
		}
		
		RMS = Math.sqrt(RMS/((double) limit));
		
		if(RMS > 1.0) {
			for(int i=0; i<limit; i++) {
				dst.put(i, dst.get(i)/((float)RMS));
			}
		}
	}
}
