package se.nederlag.pyzzlix;

import com.badlogic.gdx.backends.lwjgl.LwjglApplet;

public class PyzzlixApplet extends LwjglApplet {
	private static final long serialVersionUID = 1L;

	public PyzzlixApplet()
	{
		super(new Pyzzlix(), false);
	}
}
