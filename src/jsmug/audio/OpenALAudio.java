package jsmug.audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCcontext;
import org.lwjgl.openal.ALCdevice;

public class OpenALAudio implements Audio {
	private ALCcontext context;
	private ALCdevice device;
	
	private FloatBuffer listenerPosition;
	private FloatBuffer listenerVelocity;
	private FloatBuffer listenerOrientation;
	
	private List<OpenALSound> sounds;
	
	private IntBuffer sources;
	private IntBuffer sourcesToPlay;
	private IntBuffer sourcesToPause;
	private IntBuffer sourcesToResume;
	private IntBuffer sourcesToStop;
	private IntBuffer sourcesInUse;
	
	private int soundBufferSize = 40960;
	private FloatBuffer soundFloatBuffer;
	private ByteBuffer soundByteBuffer;
	
	public OpenALAudio() {
		this.sounds = new LinkedList<OpenALSound>();
		this.listenerPosition = BufferUtils.createFloatBuffer(3);
		this.listenerVelocity = BufferUtils.createFloatBuffer(3);
		this.listenerOrientation = BufferUtils.createFloatBuffer(6);
		this.soundByteBuffer = BufferUtils.createByteBuffer(this.soundBufferSize*2);
		this.soundFloatBuffer = BufferUtils.createFloatBuffer(this.soundBufferSize);
		this.sources = BufferUtils.createIntBuffer(64);
	}
	
	public boolean init() {
		try {
			this.context = AL.getContext();
			
			if(this.context == null) {
				AL.create();
				this.context = AL.getContext();
			}
			
			this.device = AL.getDevice();
			this.resetListener();

			AL10.alGenSources(this.sources);
			
			if(AL10.alGetError() != 0) {
				return false;
			}
			
			this.sourcesToPlay = BufferUtils.createIntBuffer(this.sources.capacity());
			this.sourcesToPause = BufferUtils.createIntBuffer(this.sources.capacity());
			this.sourcesToResume = BufferUtils.createIntBuffer(this.sources.capacity());
			this.sourcesToStop = BufferUtils.createIntBuffer(this.sources.capacity());
			
			this.sourcesInUse = BufferUtils.createIntBuffer(this.sources.capacity());
			for(int i=0; i<this.sourcesInUse.capacity(); i++) {
				this.sourcesInUse.put(i, -1);
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public void finish() {
		AL.destroy();
	}
	
	public String getDefaultDeviceName() {
		if(ALC10.alcIsExtensionPresent(null, "ALC_ENUMERATION_EXT")) {
			return ALC10.alcGetString(null, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		}
		
		return null;
	}
	
	public String[] getDeviceNames() {
		if(ALC10.alcIsExtensionPresent(null, "ALC_ENUMERATION_EXT")) {
			String devicesString = ALC10.alcGetString(null, ALC11.ALC_ALL_DEVICES_SPECIFIER);
			return devicesString.split("\0\0");
		}
		
		return null;
	}
	
	public Sound newSound(String filename) {
		OggFloatChannel channel = null;
		try {
			channel = new OggFloatChannel(Files.internal(filename).getReadChannel());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this.newSound(channel);
	}
	
	public Sound newSound(PCMFloatChannel channel) {
		OpenALSound sound = new OpenALSound(channel, this.soundBufferSize);
		this.sounds.add(sound);
		return sound;
	}
	
	
	public Sound newSoundStream(String filename) {
		OggFloatChannel channel = null;
		try {
			channel = new OggFloatChannel(Files.internal(filename).getReadChannel());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this.newSoundStream(channel);
	}
	
	
	public Sound newSoundStream(PCMFloatChannel channel) {
		OpenALSound sound = new OpenALSound(channel);
		this.sounds.add(sound);
		return sound;
	}
	
	
	public void update(double deltaTime) {
		for(OpenALSound s : this.sounds) {
			this.updateSound(s, deltaTime);
		}
		
		if(this.sourcesToPlay.position() > 0) {
			this.sourcesToPlay.flip();
			AL10.alSourcePlay(this.sourcesToPlay);
			this.sourcesToPlay.clear();
		}

		if(this.sourcesToPause.position() > 0) {
			this.sourcesToPause.flip();
			AL10.alSourcePause(this.sourcesToPause);
			this.sourcesToPause.clear();
		}

		if(this.sourcesToResume.position() > 0) {
			this.sourcesToResume.flip();
			
			while(this.sourcesToStop.hasRemaining()) {
				int source = this.sourcesToStop.get();
				AL10.alSourcei(source, AL10.AL_SOURCE_STATE, AL10.AL_PLAYING);
				this.freeSource(source);
			}

			this.sourcesToResume.clear();
		}

		if(this.sourcesToStop.position() > 0) {			
			this.sourcesToStop.flip();
			AL10.alSourceStop(this.sourcesToStop);
			this.sourcesToStop.position(0);
			
			while(this.sourcesToStop.hasRemaining()) {
				int source = this.sourcesToStop.get();
				AL10.alSourcei(source, AL10.AL_BUFFER, 0);
				this.freeSource(source);
			}
			
			this.sourcesToStop.clear();
		}
	}
	

	private void resetListener() {
		this.listenerPosition.put(new float[]{0.0f, 0.0f, 0.0f}).rewind();
		this.listenerVelocity.put(new float[]{0.0f, 0.0f, 0.0f}).rewind();
		this.listenerOrientation.put(new float[]{0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f}).rewind();
		
		AL10.alListener(AL10.AL_POSITION, this.listenerPosition);
		AL10.alListener(AL10.AL_VELOCITY, this.listenerVelocity);
		AL10.alListener(AL10.AL_ORIENTATION, this.listenerOrientation);
	}
	
	private int claimSource() {
		this.sources.position(0);
		for(int i=0; i<this.sources.limit(); i++) {
			int source = this.sources.get(i);
			if(this.sourcesInUse.get(i) == -1) {
				int state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
				
				if (state != AL10.AL_PLAYING && state != AL10.AL_PAUSED) {
					AL10.alSourceStop(source);
					AL10.alSourcei(source, AL10.AL_BUFFER, 0);
					
					this.sources.put(i, -1);
					this.sourcesInUse.put(i, source);
					
	                return source;
				}
			}
		}

		return -1;
	}
	
	private void freeSource(int source) {		
		for(int i=0; i<this.sourcesInUse.limit(); i++) {
			if(this.sourcesInUse.get(i) == source) {
				this.sources.put(i, source);
				this.sourcesInUse.put(i, -1);
				break;
			}
		}
	}

	private int createBuffer() {
		int id = AL10.alGenBuffers();

		return id;
	}
	
	
	private int fillBuffer(int buffer, OpenALSound sound) {
		this.soundFloatBuffer.clear();

		// If we reach end of stream and is looping, reset stream and play on
		if(sound.read(this.soundFloatBuffer) == -1) {
			if(sound.isLooping()) {
				sound.reset();
				this.soundFloatBuffer.clear();
				sound.read(this.soundFloatBuffer);
				System.out.println("Looping");
			} else {
				// No more data to fill buffer with
				return -1;
			}
		}
		this.soundFloatBuffer.flip();
		this.soundByteBuffer.clear();				
		PCMUtils.convertFloatToByte(this.soundFloatBuffer, this.soundByteBuffer);		
		this.soundByteBuffer.flip();
		
		int format = 0;
		if(sound.getChannels() == 1 && sound.getBits() == 8) {
			format = AL10.AL_FORMAT_MONO8;
		} else if(sound.getChannels() == 1 && sound.getBits() == 16) {
			format = AL10.AL_FORMAT_MONO16;
		} else if(sound.getChannels() == 2 && sound.getBits() == 8) {
			format = AL10.AL_FORMAT_STEREO8;
		} else if(sound.getChannels() == 2 && sound.getBits() == 16) {
			format = AL10.AL_FORMAT_STEREO16;
		} else {
			format = -1;
		}

		int length = this.soundByteBuffer.limit();
		
		AL10.alBufferData(buffer, format, this.soundByteBuffer, sound.getSampleRate());
		
		return length;
	}
	
	
	private void playSound(OpenALSound sound) {
		int source = sound.getSource();
		if(source != -1) {
			AL10.alSourcef(source, AL10.AL_PITCH, (float)sound.getPitch());
			AL10.alSourcef(source, AL10.AL_GAIN, (float)sound.getVolume());
			AL10.alSource(source, AL10.AL_POSITION, this.listenerPosition);
			AL10.alSource(source, AL10.AL_VELOCITY, this.listenerPosition);
			this.sourcesToPlay.put(source);
		}
	}
	
	private void resumeSound(OpenALSound sound) {
		int source = sound.getSource();
		if(source != -1) {
			AL10.alSourcef(source, AL10.AL_PITCH, (float)sound.getPitch());
			AL10.alSourcef(source, AL10.AL_GAIN, (float)sound.getVolume());
			AL10.alSource(source, AL10.AL_POSITION, this.listenerPosition);
			AL10.alSource(source, AL10.AL_VELOCITY, this.listenerPosition);
			this.sourcesToResume.put(source);
		}
	}
	
	private void pauseSound(OpenALSound sound) {
		int source = sound.getSource();
		if(source != -1) {
			this.sourcesToPause.put(source);
		}
	}
	
	private void stopSound(OpenALSound sound) {
		int source = sound.getSource();
		
		if(source != -1) {
			this.sourcesToStop.put(source);
			sound.setSource(-1);
		}
	}
	
	private void updateSound(OpenALSound sound, double deltaTime) {
		int source = sound.getSource();

		OpenALSound.Command nextCommand = sound.popNextCommant(); 
		
		if(nextCommand == OpenALSound.Command.PLAY) {
			this.stopSound(sound);
			
			sound.setIsPlaying(true);
			
			source = this.claimSource();
			sound.setSource(source);
			sound.reset();
			
			this.playSound(sound);
		}
		else if(nextCommand == OpenALSound.Command.PAUSE) {
			sound.setIsPaused(true);
			
			this.pauseSound(sound);
		}
		else if(nextCommand == OpenALSound.Command.RESUME) {
			sound.setIsPlaying(true);

			if(source == -1) {
				source = this.claimSource();
				sound.setSource(source);
			}
			
			this.resumeSound(sound);
		}
		else if(nextCommand == OpenALSound.Command.STOP) {
			sound.setIsStopped(true);
			sound.reset();
			
			this.stopSound(sound);
		}
		
		if(source != -1) {
			if(sound.isPlaying() && sound.isStream()) {
				int state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
				int queued = AL10.alGetSourcei(source, AL10.AL_BUFFERS_QUEUED);
				int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
				
				// If the sound has stopped, the stream reached its end and it is
				// not looping, stop it and reset stream/data
				if(state == AL10.AL_STOPPED && sound.eos() && !sound.isLooping()) {
					this.stopSound(sound);
				}
				// Otherwise fill up buffers
				else {			
					// Make sure we have 3 buffers on a stream
					if(queued < 3) {
						for(int i=0; i<3-queued; i++) {
							int buffer = this.createBuffer();
							int length = this.fillBuffer(buffer, sound);
							
							// Only queue if buffer got positive length
							if(length > 0) {
								AL10.alSourceQueueBuffers(source, buffer);
							}
						}
					}
					
					// Refill processed buffers
					while(processed > 0) {
						int buffer = AL10.alSourceUnqueueBuffers(source);
						int length = this.fillBuffer(buffer, sound);
		
						// Only queue if buffer got positive length
						if(length > 0) {
							AL10.alSourceQueueBuffers(source, buffer);
						}
						
						processed--;
					}
					
					// Make sure source is playing
					if(state != AL10.AL_PLAYING) {
						this.playSound(sound);
					}
				}
			} 
			else if(sound.isPlaying() && !sound.isStream()) {
				int state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
				int buffer = AL10.alGetSourcei(source, AL10.AL_BUFFER);
				
				// If the buffer is out and stream is at end, stop sound
				if(state == AL10.AL_STOPPED && !sound.isLooping()) {
					this.stopSound(sound);
				} 
				else { 
					// Make sure the sound has a buffer
					if(buffer == -1) {
						buffer = sound.getBuffer();
						
						// If the sound has no buffer, create it
						if(buffer == -1) {
							buffer = this.createBuffer();
							sound.setBuffer(buffer);
							this.fillBuffer(buffer, sound);
						}
						
						AL10.alSourcei(source, AL10.AL_BUFFER, buffer);			
					}
				}
			}
	
			// Update volume for this sound
			if(sound.isPlaying()) {
				sound.updateFade(deltaTime);
				AL10.alSourcef(source, AL10.AL_GAIN, (float)sound.getVolume());
				AL10.alSourcef(source, AL10.AL_PITCH, (float)sound.getPitch());
			}
			
			// Update looping for this sound
			if(!sound.isStream() && sound.isLooping()) {
				AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
			} else {
				AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
			}
		}
	}
}
