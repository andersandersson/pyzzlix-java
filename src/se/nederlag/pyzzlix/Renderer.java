package se.nederlag.pyzzlix;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Renderer {

	static final int SPRITES = 400000;
	static Renderer instance = null;
	
	double currentTime = 0.0;
	double deltaT = 0.0;
	
	boolean softBlend = false;
	
	Texture image;
	Sprite[] sprites;	
	SpriteBatch batch;
	
	private Renderer()
	{
		batch = new SpriteBatch();		
	}

	public static Renderer getInstance() 
	{
		if (instance == null)
			instance = new Renderer();
		return instance;
	}

	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho(-160, 160, -120, 120, 0, 1);
	}
	
	public void drawSprite(Sprite sprite, double currenttime, Point last_pos, Color last_col, Float last_rot, Point last_scale, Point last_origin)
	{
        if (sprite.getSoftBlending() != this.softBlend)
        {
            if (sprite.getSoftBlending() == true)
            {
            	batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE); 
            }
            else
            {
            	batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
            }
            this.softBlend = sprite.getSoftBlending();
        }
            
		Point pos = sprite.calcPos(currenttime);
		Color col = sprite.calcCol(currenttime);
		float rot = (float)sprite.calcRot(currenttime);
		Point scale = sprite.calcScale(currenttime);
		Point origin = sprite.getCenter();
		
		
		pos = pos.mul(last_scale);

		Point new_pos = new Point(0.0, 0.0);
		
		double rad = last_rot*Math.PI/180.0;
		
		new_pos.setX((pos.getX())*Math.cos(rad) - (pos.getY())*Math.sin(rad));
		new_pos.setY((pos.getX())*Math.sin(rad) + (pos.getY())*Math.cos(rad));

		new_pos = new_pos.add(last_pos);
		
		pos = new_pos.sub(origin);
		
		col.set(last_col.r*col.r, last_col.g*col.g, last_col.b*col.b, last_col.a*col.a);
		scale = scale.mul(last_scale);
		rot += last_rot;

		if(sprite.getTexture() != null) {
			sprite.setOrigin((float)origin.getX(), (float)origin.getY());
			sprite.setPosition((float)pos.getX(), (float)pos.getY());
			sprite.setColor(col);
			sprite.setRotation(rot);
			sprite.setScale((float)scale.getX(), (float)scale.getY());			
			sprite.draw(batch);
		}
		
		List<Sprite> subsprites = sprite.getSubSprites();
		for(Sprite subsprite : subsprites)
		{
			drawSprite(subsprite, currenttime, new_pos, col, new Float(rot), scale, origin);
		}
	}
	
	public void render(double deltatime)
	{		
		deltaT = deltatime;
		currentTime += deltaT;
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl11.glColor4f(0,1,0,1);
		batch.begin();
		batch.enableBlending();

		SceneHandler.getInstance().renderScenes();

		batch.end();			
	}

	public void renderScene(Scene scene) {
		for(Sprite sprite : scene.getSprites()) {
			drawSprite(sprite, currentTime, new Point(0,0), new Color(1,1,1,1), 0.0f, new Point(1,1), new Point(0,0));
		}
	}
}