package se.nederlag.pyzzlix;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import com.badlogic.gdx.backends.lwjgl.LwjglApplet;

public class PyzzlixApplet extends LwjglApplet {
	private static final long serialVersionUID = 1L;

	public PyzzlixApplet()
	{
		super(new Pyzzlix(Pyzzlix.RunMode.APPLET), false);
		try {
			Display.setParent(null);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}