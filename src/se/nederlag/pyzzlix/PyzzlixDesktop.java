package se.nederlag.pyzzlix;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class PyzzlixDesktop {
	public static void main(String[] argv)
	{
	 	new LwjglApplication(new Pyzzlix(Pyzzlix.RunMode.DESKTOP), "Pyzzlix", 800, 600, false);
		//new LwjglApplication(new MusicTest(), "Pyzzlix", 800, 600, false);
	}
}
