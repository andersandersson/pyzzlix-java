package se.nederlag.pyzzlix;

import java.util.ArrayList;
import java.util.List;

import se.nederlag.pyzzlix.audio.Audio;
import se.nederlag.pyzzlix.audio.Music;
import se.nederlag.pyzzlix.audio.OpenALMusic;

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
	
	public static Music getMusic(String name) {
		return Audio.loadAudioStream("data/"+name+".ogg");
	}

	public static Mixer.Sound getSound(String name) {
		return Mixer.getInstance().loadAudioFile("data/"+name+".ogg");
	}
}
