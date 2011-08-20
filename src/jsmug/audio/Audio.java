package jsmug.audio;

public interface Audio {
	public boolean init();
	public void finish();
	public String getDefaultDeviceName();
	public String[] getDeviceNames();
	public Sound newSound(String filename);
	public Sound newSound(PCMFloatChannel input);
	public Sound newSoundStream(String filename);
	public Sound newSoundStream(PCMFloatChannel input);
	public void update(double deltaTime);
}
