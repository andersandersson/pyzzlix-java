package se.nederlag.pyzzlix;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class PyzzlixDesktop {
	public static void main(String[] argv)
	{
	 	new LwjglApplication(new Pyzzlix(), "Pyzzlix", 1024, 768, false);
		//new LwjglApplication(new MusicTest(), "Pyzzlix", 800, 600, false);
	}
}
