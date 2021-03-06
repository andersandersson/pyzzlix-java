package se.nederlag.pyzzlix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jsmug.audio.Sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resources {
	public static List<TextureRegion> loadImageSheet(Texture texture, int width, int height) {
		return Resources.loadImageSheet(texture, width, height, 0, 0, texture.getWidth(), texture.getHeight());
	}

	public static List<TextureRegion> loadImageSheet(Texture texture, int width, int height, int srcx, int srcy, int srcw, int srch) {
		List<TextureRegion> list = new ArrayList<TextureRegion>();
		int num_frames_w = srcw / width;
		int num_frames_h = srch / height;				
	
		for(int y = 0; y < num_frames_h; y++) 
		{	
			for(int x = 0; x < num_frames_w; x++) 
			{
				TextureRegion region = new TextureRegion(texture, x*width+srcx, y*height+srcy, width, height);
				list.add(region);
			}
		}

		return list;
	}
	
	public static Sound getMusic(String name) {
		return Pyzzlix.audio.newSoundStream("data/"+name+".ogg");
	}

	public static Sound getSound(String name) {
		return Pyzzlix.audio.newSound("data/"+name+".ogg");
	}
}
