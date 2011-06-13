package se.nederlag.pyzzlix;

public abstract class SpriteCallback {
	private Object[] args = null;
	
	public SpriteCallback(Object... args)
	{
		this.args = args;
	}
	
	public SpriteCallback()
	{
	}

	public Object getArgs() {
		return args;
	}

	public void setArgs(Object... args) {
		this.args = args;
	}

	public Object getArg(int idx) {
		return args[idx];
	}

	public abstract void callback(Sprite sprite, double currenttime);
}
