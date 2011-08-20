package jsmug.audio;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

class OpenALSound implements Sound {
	public enum Command {PLAY, RESUME, PAUSE, STOP, NONE};
	
	boolean isPlaying = false;
	boolean isPaused = false;
	boolean isStopped = true;
	boolean isStream = true;
	boolean isLooping = false;
	boolean eos = true;
	
	int source = -1;
	int stopSource = -1;
	int buffer = -1;
	
	double volume = 1.0f;
	double pitch = 1.0f;

	int sampleRate = 0;
	int bits = 0;
	int channels = 0;
	
	double fadeFrom = 0.0;
	double fadeTo = 0.0;
	double fadeTimer = 0.0;
	double fadeDuration = 0.0;
	
	private Command nextCommand = Command.NONE;
	
	PCMFloatChannel input;
	FloatBuffer data = null;
	
	public OpenALSound(PCMFloatChannel input) {
		this.input = input;
		this.eos = false;
		this.isStream = true;
		
		this.sampleRate = input.getSampleRate();
		this.bits = input.getBits();
		this.channels = input.getChannels();

	}
	
	public OpenALSound(PCMFloatChannel input, int bufferSize) {
		this.input = input;
		this.eos = false;
		this.isStream = false;
		
		this.sampleRate = input.getSampleRate();
		this.bits = input.getBits();
		this.channels = input.getChannels();

		FloatBuffer tmp = BufferUtils.createFloatBuffer(bufferSize);
		FloatBuffer tmp2;
		
		// Allocate tmp buffer for about 10s of data
		while(input.read(tmp) > 0) {
			tmp2 = BufferUtils.createFloatBuffer(tmp.limit()+bufferSize);
			tmp.flip();
			tmp2.put(tmp);
			tmp = tmp2;
		}
		
		// Force stream mode if we don't fit into one buffer
		if(tmp.position() >= bufferSize) {
			this.isStream = true;
		}

		this.input = null;
		this.data = tmp;
		this.data.flip();
	}

	public long read(FloatBuffer dst) {
		long length = 0;
		
		if(this.data != null) {
			if(!this.data.hasRemaining()) {
				this.eos = true;
				return -1;
			}
			
			while(dst.hasRemaining() && this.data.hasRemaining()) {
				dst.put(this.data.get()*((float)this.volume));
				length += 1;
			}
			
			if(!this.data.hasRemaining()) {
				this.eos = true;
			}
		} else {
			length = this.input.read(dst);
			
			if(length == -1) {
				this.eos = true;
			}
		}
		
		return length;
	}

	public int getStopSource() {
		return this.stopSource;
	}

	public void setStopSource(int stopSource) {
		this.stopSource = stopSource;
	}

	public int getSource() {
		return this.source;
	}

	public void setSource(int source) {
		this.source = source;
	}
	
	public int getBuffer() {
		return this.buffer;
	}

	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void play() {
		this.nextCommand = Command.PLAY;
	}

	@Override
	public void pause() {
		this.nextCommand = Command.PAUSE;
	}

	@Override
	public void resume() {
		this.nextCommand = Command.RESUME;
	}

	@Override
	public void stop() {
		this.nextCommand = Command.STOP;
	}

	@Override
	public void setLooping(boolean looping) {
		this.isLooping = looping;
	}

	@Override
	public void setVolume(double volume) {
		this.volume = volume;
	}

	@Override
	public double getVolume() {
		return this.volume;
	}

	@Override
	public boolean isStream() {
		return this.isStream;
	}

	@Override
	public boolean isLooping() {
		return this.isLooping;
	}

	public boolean isPlaying() {
		return this.isPlaying;
	}
	
	@Override
	public boolean isPaused() {
		return this.isPaused;
	}
	
	@Override
	public boolean isStopped() {
		return this.isStopped;
	}
	
	public boolean eos() {
		return this.eos;
	}

	public void reset() {
		if(this.input != null) {
			this.input.position(0);
		} 
		
		if(this.data != null) {
			this.data.position(0);
		}

		this.eos = false;
	}

	@Override
	public boolean seek(int position) {
		if(this.data != null) {
			if(this.data.limit() >= position) {
				this.data.position(position);
				return true;
			} else {
				return false;
			}
		}

		if(this.input != null) {
			this.input.position(position);
			
			return this.input.position() == position;
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
		return this.bits;
	}

	@Override
	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	@Override
	public double getPitch() {
		return this.pitch;
	}

	@Override
	public void fadeIn(double duration) {
		this.play();
		this.volume = 0.0;
		this.fadeDuration = duration;
		this.fadeFrom = this.volume;
		this.fadeTo = 1.0;
		this.fadeTimer = 0.0;
	}

	@Override
	public void fadeOut(double duration) {
		this.fadeTo(duration, 0.0);
	}

	@Override
	public void fadeTo(double duration, double volume) {
		this.fadeDuration = duration;
		this.fadeFrom = this.volume;
		this.fadeTo = volume;
		this.fadeTimer = 0.0;
	}
	
	public void updateFade(double deltaTime) {
		if(this.fadeTimer < this.fadeDuration) {
			this.fadeTimer += deltaTime;
			this.volume = (this.fadeTo - this.fadeFrom)*this.fadeTimer/this.fadeDuration+this.fadeFrom;
		}
	}
	
	public Command popNextCommant() {
		Command command = this.nextCommand;
		this.nextCommand = Command.NONE;
		return command;
	}
	
	public void setIsPlaying(boolean isPlaying) {
		if(isPlaying) {
			this.isPaused = false;
			this.isStopped = false;
		}
		
		this.isPlaying = isPlaying;
	}

	public void setIsStopped(boolean isStopped) {
		if(isStopped) {
			this.isPaused = false;
			this.isPlaying = false;
		}
		
		this.isStopped = isStopped;
	}

	public void setIsPaused(boolean isPaused) {
		if(isPaused) {
			this.isPlaying = false;
			this.isStopped = false;
		}
		
		this.isPaused = isPaused;
	}
}
