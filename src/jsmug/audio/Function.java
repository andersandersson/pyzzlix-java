package jsmug.audio;

public interface Function<T,U> {
	public T eval(U x);
}
