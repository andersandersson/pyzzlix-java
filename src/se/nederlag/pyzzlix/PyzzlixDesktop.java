package se.nederlag.pyzzlix;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.nederlag.pyzzlix.audio.FFT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class PyzzlixDesktop {
	public static void main(String[] argv)
	{
	 	new LwjglApplication(new Pyzzlix(Pyzzlix.RunMode.DESKTOP), "Pyzzlix", 1024, 768, false);
		//new LwjglApplication(new MusicTest(), "Pyzzlix", 800, 600, false);768
	}
}
