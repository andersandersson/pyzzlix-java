package se.nederlag.pyzzlix;

public abstract class MenuCallback {
	private Object[] args = null;
	
	public MenuCallback(Object... args)
	{
		this.args = args;
	}
	
	public MenuCallback()
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

	public abstract void call();
}
