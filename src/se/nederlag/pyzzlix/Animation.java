package se.nederlag.pyzzlix;

import java.util.List;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	double frameTimer = 0.0;
	List<Double> frameLengths = new LinkedList<Double>();
	List<TextureRegion> frames= new LinkedList<TextureRegion>();
	int frameCount = 0;
	String mode = "loop";
	boolean reverse = false;
	int direction = 1;
	int frame;
	Sprite sprite;
	
	public Animation(Sprite sprite, int width, int height, int srcx, int srcy, int srcw, int srch, double currenttime, double framelength, String mode, boolean reverse)
	{
		if(mode == "loop" || mode == "pingpong" || mode == "pingpongloop" || mode == "normal")
		{
			this.mode = mode;
		} else {
			this.mode = "normal";
		}
		
		this.reverse = reverse;
		this.sprite = sprite;
		
		int num_frames_w = srcw / width;
		int num_frames_h = srch / height;				
		
		for(int y = 0; y < num_frames_h; y++) 
		{
			for(int x = 0; x < num_frames_w; x++) 
			{
				TextureRegion region = new TextureRegion(sprite, x*width+srcx, y*height+srcy, width, height);
				this.frames.add(region);
				this.frameLengths.add(framelength);
			}
		}
		
		reset(currenttime);
	}
	
	public void reset(double currenttime)
	{
		frameTimer = currenttime;
		frameCount = frames.size();
		
		if(reverse) {
			direction = -1;
			frame = frameCount - 1;
		} else {
			direction = 1;
			frame = 0;
		}
		
		if(frameCount > 0)
		{
			sprite.setRegion(frames.get(frame));
		}
	}
	
	public void updateFrame(double currenttime)
	{
		if(frameCount > 0)
		{
			while(frameTimer + frameLengths.get(frame) <= currenttime)
			{
				frameTimer += frameLengths.get(frame);
				frame += direction;
				
				if(frame >= frameCount)
				{
					if(mode == "loop")
					{
						frame = 0;
					} 
					else if(mode == "pingpong")
					{
						if(!reverse)
						{
							frame = frameCount - 1;
							direction = -1;
						}
						else
						{
							frameCount = 0;
							frame = frameCount - 1; 
						}
					}
					else if(mode == "pingpongloop")
					{
						frame = frameCount - 1;
						direction = -1;
					}
					else
					{
						frameCount = 0;
						frame = frameCount - 1;
					}
				}
				else if(frame < 0)
				{
					if(mode == "loop")
					{
						frame = frameCount - 1;
					} 
					else if(mode == "pingpong")
					{
						if(!reverse)
						{
							frameCount = 0;
							frame = 0;
						} 
						else
						{
							frame = 0;
							direction = 1;
						}
					}
					else if(mode == "pingpongloop")
					{
						frame = 0;
						direction = 1;
					}
					else
					{
						frameCount = 0;
						frame = 0;
					}
				}
			}

			if(frame >= 0) 
			{
				sprite.setRegion(frames.get(frame));
			}
		}
	}
}
