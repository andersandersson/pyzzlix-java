package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Background extends Sprite {
	private class BGSprite extends Sprite {
		protected Color mainColor;
		protected Texture bgTexture;
		protected List<Sprite> shapes;
		protected int lowestSpeed;
		protected int speed;
		protected int spin;
		protected int decline;
		protected int boosts;
		protected int order;
		
		protected double velX;
		protected double velY;
		
		public BGSprite(Color maincolor, String imagefile, int count) {
			//super()
	        this.mainColor = maincolor;

	        this.bgTexture = new Texture(Gdx.files.internal(imagefile));
	        
	        this.shapes = new LinkedList<Sprite>();
	        
	        for(int i=0; i<count; i++) {
	            Sprite s = new Sprite(this.bgTexture);
	            s.setCenter(new Point(64, 64));
	            s.setCol(this.mainColor);
	            s.setSize(128, 128);
	            this.shapes.add(s);
	        }
	        
	        this.lowestSpeed = 5;
	        this.speed = 0;
	        this.spin = 20;
	        this.decline = 10;
	        this.boosts = 0;
	        this.order = 0;
	        
	        this.velX = 0.0;
	        this.velY = 0.0;

	        this.setPos(new Point(160, 120));
	        this.setCenter(new Point(160, 120));

	        this.setCol(new Color(1.0f, 1.0f, 1.0f, 0.3f));
	        this.pulsate();
	   }
		
		public void update(double currentTime) {
			super.update(currentTime);
			
			 if(this.boosts > 0) {
	            this.speed += this.spin;
	            this.boosts -= 1;
			 } else {
	            this.speed -= this.decline * (this.currentTime - this.lastTime);
	            
	            if(this.speed <= this.lowestSpeed) {
	                this.speed = this.lowestSpeed;
	            }
			 }		                
		     
			 this.animate(this.speed);
		}
		
		public void pulsate() {
			SpriteCallback fade_to_done;
			SpriteCallback fade_from_done;
			
			fade_to_done = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					sprite.fadeTo(new Color(1.0f, 1.0f, 1.0f, 0.3f), currenttime, 1.0, (SpriteCallback) getArg(0));
				}			
			};
			
			fade_from_done = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					sprite.fadeTo(new Color(1.0f, 1.0f, 1.0f, 0.45f), currenttime, 1.0, (SpriteCallback) getArg(0));
				}			
			};
			
			fade_to_done.setArgs(fade_from_done);
			fade_from_done.setArgs(fade_to_done);

			fade_to_done.callback(this, this.currentTime);
		}
		
		public void glow(int count) {
			SpriteCallback done;
			SpriteCallback fade_in_done;
			SpriteCallback fade_out_done;
			
			done = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					BGSprite background = (BGSprite) sprite;
					background.pulsate();
				}			
			};
			
			fade_out_done = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					int count = (Integer) getArg(1);
					sprite.fadeTo(new Color(1.0f, 1.0f, 1.0f, 0.3f), currenttime, 1.0*count, (SpriteCallback) getArg(0));
				}			
			};
			
			fade_in_done = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					sprite.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), currenttime, 1.0, (SpriteCallback) getArg(0));
				}			
			};
			
			fade_in_done.setArgs(fade_out_done);
			fade_out_done.setArgs(done, count);

			fade_in_done.callback(this, this.currentTime);
		}
		
		public void boost(int count) {
	        this.boosts += count;
	        this.glow(count);
	    }
		
		public void animate(int speed) {			
		}
	}
	
	
	private class BG_Heart extends BGSprite {
		private double counter;
		
		public BG_Heart() {
			super(new Color(1.0f, 0.3f, 0.3f, 1.0f), "data/bg_heart.png", 10);
			
			for(int i = 0; i<10; i++) {
	            this.shapes.get(i).setScale(new Point(1.0 * Math.exp(i/3.5), 1.0 * Math.exp(i/3.5)));
	            this.shapes.get(i).setPos(new Point(160, 120));
	            this.shapes.get(i).setRot(90 * Math.sin(0.0 - (i/5.0)));
	            this.addSubSprite(this.shapes.get(i));
	        }
			
			this.counter = 0.0;
		}
		
		public void animate(int speed) {
			double nspeed = speed / 500.0;
	        this.counter += nspeed;
	        this.rotateTo(this.getRotation() - 30 * nspeed, this.currentTime, 0.05, null);

	        for(int i=0; i<10; i++) {
	            Sprite shape = this.shapes.get(i);
	            shape.rotateTo(90 * Math.sin(this.counter - (i/5.0)), this.currentTime, 0.05, null);
	        }
	    }
	}
	
	
	private class BG_Square extends BGSprite {
		private double counter;
		
		private List<Double> velocitiesX;
		private List<Double> velocitiesY;
		
		public BG_Square() {
			super(new Color(0.6f, 1.0f, 0.3f, 1.0f), "data/bg_square.png", 10);
			
			this.velocitiesX = new LinkedList<Double>();
			this.velocitiesY = new LinkedList<Double>();
			
			this.velocitiesX.add(0.0);
			this.velocitiesY.add(0.0);
			
            this.shapes.get(0).setScale(new Point(4.0, 4.0));
            this.shapes.get(0).setPos(new Point(160, 120));
            this.addSubSprite(this.shapes.get(0));

            for(int i = 0; i<9; i++) {
	            this.shapes.get(i+1).setScale(new Point(0.9, 0.9));
	            this.shapes.get(i+1).setPos(new Point(64, 64));

	            this.velocitiesX.add((double) (10 + (i % 2) * -20));
	            this.velocitiesY.add((double) (6 + (i % 2) * -12));
	            
	            this.shapes.get(i).addSubSprite(this.shapes.get(i+1));
	        }
			
			this.counter = 0.0;
		}
		
		public void animate(int speed) {
			this.rotateTo(this.calcRot(this.currentTime) + (float) speed, this.currentTime, 1.0, null);   
	        
	        for(int i=0; i<9; i++) {
	            int j = i + 1;
	            Sprite shape = this.shapes.get(j);
	            double sx = shape.calcPos(this.currentTime).getX();
	            double sy = shape.calcPos(this.currentTime).getY();

	            shape.rotateTo(shape.calcRot(this.currentTime) + speed, this.currentTime, 1.0, null);   
	            
	            if (sx > 72 && this.velocitiesX.get(j) > 0) {
	            	this.velocitiesX.set(j, -this.velocitiesX.get(j));
	            }

	            if (sx < 56 && this.velocitiesX.get(j) < 0) {
	            	this.velocitiesX.set(j, -this.velocitiesX.get(j));
	            }

	            if (sy > 72 && this.velocitiesY.get(j) > 0) {
	            	this.velocitiesY.set(j, -this.velocitiesY.get(j));
	            }

	            if (sy < 56 && this.velocitiesY.get(j) < 0) {
	            	this.velocitiesY.set(j, -this.velocitiesY.get(j));
	            }

	            shape.moveTo(new Point(sx + this.velocitiesX.get(j), sy + this.velocitiesY.get(j)), this.currentTime, 1.0, null);		
			}
		}
	}

	
	private class BG_Triangle extends BGSprite {
		private double counter;
		
		public BG_Triangle() {
			super(new Color(1.0f, 0.0f, 0.0f, 1.0f), "data/bg_diamond.png", 10);
		}
	}
	
	private class BG_Cross extends BGSprite {
		private double counter;
		
		public BG_Cross() {
			super(new Color(1.0f, 0.0f, 0.0f, 1.0f), "data/bg_diamond.png", 10);
		}
	}
	
	private class BG_Diamond extends BGSprite {
		private double counter;
		
		public BG_Diamond() {
			super(new Color(1.0f, 0.0f, 0.0f, 1.0f), "data/bg_diamond.png", 10);
		}
	}

	private class BG_Circle extends BGSprite {
		private double counter;
		
		public BG_Circle() {
			super(new Color(1.0f, 0.0f, 0.0f, 1.0f), "data/bg_diamond.png", 10);
		}
	}
	
	private class BG_Plus extends BGSprite {
		private double counter;
		
		public BG_Plus() {
			super(new Color(1.0f, 0.0f, 0.0f, 1.0f), "data/bg_diamond.png", 10);
		}
	}
	

	private BGSprite currentShape;
	private List<BGSprite> shapes;
	private int shapeCount;
	
    public Background() {
        this.setPos(new Point(0, 0));

        this.shapes = new LinkedList<BGSprite>();
        //this.shapes.add(new BG_Heart());
        this.shapes.add(new BG_Square());
        //this.shapes.add(new BG_Triangle());
        //this.shapes.add(new BG_Cross());
        //this.shapes.add(new BG_Diamond());
        //this.shapes.add(new BG_Circle());
        //this.shapes.add(new BG_Plus());
        
        this.shapeCount = this.shapes.size();
        this.currentShape = this.shapes.get(0);
        
        this.addSubSprite(this.currentShape);
    }
    
    public void update(double currentTime) { 
        super.update(currentTime);
        this.currentShape.update(currentTime);
    }

    public void boost(int count) {
        this.currentShape.boost(count);
    }

    public void flash(double delay) {
        SpriteCallback remove_white = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
	        	Background background = (Background) getArg(0);
	        	background.removeSubSprite(sprite);
			}
        };
    
        remove_white.setArgs(this);
        
	    Sprite white = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")));
	    white.setScale(new Point(320,240));
	    white.setCol(new Color(1.0f, 1.0f, 1.0f, 1.0f));
	    white.fadeTo(new Color(1.0f, 1.0f, 1.0f, 0.0f), this.currentTime, delay, remove_white);
	    this.addSubSprite(white);
    }

    public void setTheme(int shape) {
        this.removeSubSprite(this.currentShape);
        this.currentShape = this.shapes.get(shape);    
        this.addSubSprite(this.currentShape);
    }
}
