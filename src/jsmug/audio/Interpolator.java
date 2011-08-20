package jsmug.audio;

public interface Interpolator {
	public Object eval(Object from, Object to, double percentage);
}
