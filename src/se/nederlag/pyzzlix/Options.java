package se.nederlag.pyzzlix;

import java.util.HashMap;
import java.util.Map;

public class Options {
	private static Map<String,Object> options;
	
	public static Object get(String name) {
		if(Options.options == null) {
			Options.options = new HashMap<String, Object>();
		}

		return Options.options.get(name);
	}

	public static void set(String name, Object value) {
		if(Options.options == null) {
			Options.options = new HashMap<String, Object>();
		}
		
		Options.options.put(name, value);
	}
}
