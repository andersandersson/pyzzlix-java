package jsmug.audio;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;

public class PCMFloatChannelMixer implements PCMFloatChannel {
	private class VolumeFader {
		public long currentSample = 0;
		public long endSample = 0;
		public double fromVolume = 0.0;
		public double toVolume = 0.0;
		
		public VolumeFader(double fromVolume, double toVolume) {
			this.fromVolume = fromVolume;
			this.toVolume = toVolume;
		}
	}
	
	private List<PCMFloatChannel> inputChannels = null;
	private List<VolumeFader> inputVolumes = null;
	private int sampleRate = 0;
	private int channels = 0;
	
	private long sampleCount = 0;
	
	private boolean endOfStream = false;
	
	private FloatBuffer mixerBuffer;

	public PCMFloatChannelMixer(int sampleRate, int channels) {
		this.inputChannels = new LinkedList<PCMFloatChannel>();
		this.inputVolumes = new LinkedList<VolumeFader>();
		this.sampleRate = sampleRate;
		this.channels = channels;
		this.mixerBuffer = BufferUtils.createFloatBuffer(4096);
	}
	
	public boolean add(PCMFloatChannel channel, double volume) {
		this.inputVolumes.add(new VolumeFader(volume, volume));
		
		return this.inputChannels.add(channel);
	}
	
	public boolean remove(PCMFloatChannel channel) {
		return this.inputChannels.remove(channel);
	}

	public int getInputChannels() {
		return this.inputChannels.size();
	}
	
	public void fadeIn(int channel, double duration) {
		VolumeFader fader = this.inputVolumes.get(channel);
		
		fader.fromVolume = fader.toVolume;
		fader.toVolume = 1.0;
		fader.currentSample = 0;
		fader.endSample = (long)((double)this.getSampleRate()*duration);
	}
	
	public void fadeOut(int channel, double duration) {
		VolumeFader fader = this.inputVolumes.get(channel);
		
		fader.fromVolume = fader.toVolume;
		fader.toVolume = 0.0;
		fader.currentSample = 0;
		fader.endSample = (long)((double)this.getSampleRate()*duration);
	}
	
	public void fadeTo(int channel, double duration, double volume) {
		VolumeFader fader = this.inputVolumes.get(channel);
		
		fader.fromVolume = fader.toVolume;
		fader.toVolume = volume;
		fader.currentSample = 0;
		fader.endSample = (long)((double)this.getSampleRate()*duration);
	}
	
	public void setVolume(int channel, double volume) {
		this.fadeTo(channel, 0.0, volume);
		this.fadeTo(channel, 0.0, volume);
	}
	
	public double getVolume(int channel) {
		return this.inputVolumes.get(channel).toVolume;
	}
	
	@Override
	public boolean isOpen() {
		for(PCMFloatChannel channel : this.inputChannels) {
			if(channel.isOpen()) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int getSampleRate() {
		return this.sampleRate;
	}

	@Override
	public int getChannels() {
		return this.channels;
	}

	@Override
	public int getBits() {
		return 16;
	}

	@Override
	public void close() {
		for(PCMFloatChannel channel : this.inputChannels) {
			if(channel.isOpen()) {
				channel.close();
			}
		}
		
		this.inputChannels.clear();
	}

	@Override
	public long position() {
		return this.sampleCount;
	}

	@Override
	public PCMFloatChannel position(long position) {
		for(PCMFloatChannel channel : this.inputChannels) {
			if(channel.isOpen()) {
				channel.position(position);
			}
		}

		return this;
	}

	@Override
	public long read(FloatBuffer dst) {
		return this.read(new FloatBuffer[]{dst}, 0, 1);
	}

	@Override
	public long read(FloatBuffer dst, long position) {
		long oldPosition = this.sampleCount;
		
		this.position(this.sampleCount);
		
		long length = this.read(dst);
		
		this.position(oldPosition);
		
		return length;
	}

	@Override
	public long read(FloatBuffer[] dsts) {
		return this.read(dsts, 0, dsts.length);
	}

	
	@Override
	public long read(FloatBuffer[] dsts, int offset, int length) {
		int currentDst = offset;
		long sampleCountStart = this.sampleCount;
		
		if(this.inputChannels.size() == 0) {
			return 0;
		}
		
		while(!this.endOfStream && currentDst < length+offset) {
			if(dsts[currentDst].capacity() > this.mixerBuffer.capacity()) {
				this.mixerBuffer = BufferUtils.createFloatBuffer(dsts[currentDst].capacity());
			}
			
			this.mixerBuffer.limit(dsts[currentDst].capacity());
			
			for(int i=0; i<this.inputChannels.size(); i++) {
				this.mixerBuffer.position(0);
				
				// If any channel returns end of file, consider entire mixer end of file
				if(this.inputChannels.get(i).read(this.mixerBuffer) == -1) {
					dsts[currentDst].position(0);
					return -1;
				}
				
				VolumeFader fader = this.inputVolumes.get(i);
				double fromVolume = fader.toVolume;
				double toVolume = fader.toVolume;
				long volumeSamples = 0;
				
				if(fader.currentSample < fader.endSample) {
					volumeSamples = fader.endSample - fader.currentSample;
					fromVolume = (fader.toVolume - fader.fromVolume) * ((double)fader.currentSample / (double)fader.endSample) + fader.fromVolume;
					toVolume = fader.toVolume;
				}

				this.mixerBuffer.flip();
				
				if(i == 0) {
					this.sampleCount += this.mixerBuffer.limit();
					dsts[currentDst].limit(this.mixerBuffer.limit());
					PCMUtils.copyBuffer(this.mixerBuffer, dsts[currentDst], fromVolume, toVolume, volumeSamples, this.channels);
				} else {
					if(toVolume >= 0.01 || fromVolume >= 0.01) {
						PCMUtils.mixBuffers(this.mixerBuffer, dsts[currentDst], fromVolume, toVolume, volumeSamples, this.channels);
					}
				}
				
				if(fader.currentSample < fader.endSample) {
					fader.currentSample += this.mixerBuffer.limit();
				}
			}
			
			// Compress buffer to avoid distortion
			PCMUtils.compressBufferRMS(dsts[currentDst]);
			dsts[currentDst].position(dsts[currentDst].limit());
			
			currentDst++;
		}
		
		return this.sampleCount - sampleCountStart;
	}

	
	@Override
	public long size() {
		long size = 0;
		
		for(PCMFloatChannel channel : this.inputChannels) {
			if(channel.isOpen()) {
				long cSize = channel.size();
				
				if(size < cSize) {
					size = cSize;
				}
			}
		}

		return size;
	}
}
