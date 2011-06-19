package se.nederlag.pyzzlix;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	public enum Mode {
		LOOP, PINGPONG, PINGPONGLOOP, NORMAL;
	}
	
	double frameTimer = 0.0;
	List<Double> frameLengths = new ArrayList<Double>();
	List<TextureRegion> frames= new ArrayList<TextureRegion>();
	int frameCount = 0;
	Animation.Mode mode = Mode.NORMAL;
	boolean reverse = false;
	int direction = 1;
	int frame;
	Sprite sprite;
	
	private void init(Sprite sprite, int width, int height, List<TextureRegion> frames, double currenttime, double framelength, Animation.Mode mode, boolean reverse) {
		this.mode = mode;		
		this.reverse = reverse;
		this.sprite = sprite;
		this.frames = frames;

		this.sprite.setSize(width, height);
		
		for(int i=0; i<frames.size(); i++) {
			this.frameLengths.add(framelength);
		}
		
		reset(currenttime);
	}
	
	public Animation(Sprite sprite, int width, int height, double currenttime, double framelength, Animation.Mode mode, boolean reverse)
	{
		List<TextureRegion> frames = Resources.loadImageSheet(sprite.getTexture(), width, height);
		
		this.init(sprite, width, height, frames, currenttime, framelength, mode, reverse);
	}

	public Animation(Sprite sprite, int width, int height, int srcx, int srcy, int srcw, int srch, double currenttime, double framelength, Animation.Mode mode, boolean reverse)
	{
		List<TextureRegion> frames = Resources.loadImageSheet(sprite.getTexture(), width, height, srcx, srcy, srcw, srch); 

		this.init(sprite, width, height, frames, currenttime, framelength, mode, reverse);
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
					if(mode == Mode.LOOP)
					{
						frame = 0;
					} 
					else if(mode == Mode.PINGPONG)
					{
						if(!reverse)
						{
							frame = frameCount - 1;
							direction = -1;
						}
						else
						{
							frameCount = 0;
							frame = 0; 
						}
					}
					else if(mode == Mode.PINGPONGLOOP)
					{
						frame = frameCount - 1;
						direction = -1;
					}
					else
					{
						frame = frameCount - 1;
						frameCount = 0;
					}
				}
				else if(frame < 0)
				{
					if(mode == Mode.LOOP)
					{
						frame = frameCount - 1;
					} 
					else if(mode == Mode.PINGPONG)
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
					else if(mode == Mode.PINGPONGLOOP)
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
