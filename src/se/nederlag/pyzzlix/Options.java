package se.nederlag.pyzzlix;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 

import se.nederlag.pyzzlix.audio.Audio;

import com.badlogic.gdx.Gdx;

public class Options {
	private static Map<String,Object> options;
	
	public static Object get(String name) {
		if(Options.options == null) {
			Options.options = new HashMap<String, Object>();
		}

		Gdx.files.external(".pyzzlix").mkdirs();
		if(Gdx.files.external(".pyzzlix/options.txt").exists()) {
			String data = Gdx.files.external(".pyzzlix/options.txt").readString();
			String[] items = data.split("\n");
			
			Pattern p = Pattern.compile("class ([^:]*):([^:]*):([^:]*)");
			for(String row : items) {
				Matcher m = p.matcher(row);
				if(m.matches()) {
					if(m.group(1).equals("java.lang.Integer")) {
						Options.options.put(m.group(2), Integer.valueOf(m.group(3)));
					}
					else if(m.group(1).equals("java.lang.Boolean")) {
						Options.options.put(m.group(2), Boolean.valueOf(m.group(3)));
					}
					else if(m.group(1).equals("java.lang.Float")) {
						Options.options.put(m.group(2), Float.valueOf(m.group(3)));
					}
				}
			}
		}
		
		return Options.options.get(name);
	}

	public static void set(String name, Object value) {
		if(Options.options == null) {
			Options.options = new HashMap<String, Object>();
		}
		
		Options.options.put(name, value);
				
		Gdx.files.external(".pyzzlix").mkdirs();
		OutputStream out = Gdx.files.external(".pyzzlix/options.txt").write(false);
			
		try {
			for(Map.Entry<String,Object> entry : Options.options.entrySet()) {
				out.write(entry.getValue().getClass().toString().getBytes());
				out.write(":".getBytes());
				out.write(entry.getKey().toString().getBytes());
				out.write(":".getBytes());
				out.write(entry.getValue().toString().getBytes());
				out.write("\n".getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
