package se.nederlag.pyzzlix;

public abstract class Callback {
	public Object[] args;
	
	public Callback(Object... args) {
		this.args = args;
	}
	
	public abstract Object call(Object... params);
}
