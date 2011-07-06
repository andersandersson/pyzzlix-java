package se.nederlag.pyzzlix;

import com.badlogic.gdx.Input;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

public class Scene_InputText extends Scene {
	private static Scene_InputText instance = null;

	private Font font;
	private Text text;
	private String textBuffer = "";
	private int textBufferCounter;
	private String currentChar = "";
	private int ticker;
	private Callback callback;
	private int length;
	
	private Scene_InputText() {
        this.font = new Font("data/font_fat.png", 8, 8);

        this.text = new Text(160, 120, this.font, "");
        this.text.setAnchor(Text.Anchor.CENTER);
        this.text.setScale(new Point(2.0, 2.0));

        this.addSprite(this.text);

        this.updateBlocker = true;

        this.textBuffer = "";
        this.textBufferCounter = 0;
        this.currentChar = "A##";
        
        this.ticker = 0;
        this.callback = null;
        this.length = 3;
	}
	
	public static Scene_InputText getInstance()
	{
		if (instance == null)
			instance = new Scene_InputText();
		return instance;
	}
	
	public void display(int x, int y, int length, Callback callback) {
        this.text.setPos(new Point(x, y));
        this.length = length;
        this.textBuffer = "";
        this.textBufferCounter = 0;
        this.currentChar = "A";
        this.callback = callback;

        SceneHandler.getInstance().pushScene(this);		 
	}
	
	public void tick() {
		String text = this.textBuffer;

        if(this.textBufferCounter < this.length) {
            if(this.ticker >= 0 && this.ticker < 5) {
                text = text + this.currentChar;
            } else if(this.ticker >= 5 && this.ticker < 9) {
                text = text + "#";
            } else {
                text = text + "#";
                this.ticker = 0;
            }
                
            this.ticker += 1;
        }

        this.text.setText(text);
	}
	
	public boolean handleEvent(Event event) {
		char c;
		
		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;
						
			if(keyevent.state == EventKeyState.State.DOWN) {
				switch(keyevent.key) {
				case Input.Keys.UP:
	                c = (char) (this.currentChar.toCharArray()[0]-1);
	                if(c < 'A') {
	                	c = 'Z';
	                }
	
	                this.currentChar = String.valueOf(c);
				break;

				case Input.Keys.DOWN:
	                c = (char) (this.currentChar.toCharArray()[0]+1);
	                if(c > 'Z') {
	                	c = 'A';
	                }
	
	                this.currentChar = String.valueOf(c);
					break;

				case Input.Keys.ENTER:
	                if(this.textBufferCounter < this.length) {
	                    this.textBufferCounter += 1;
	                    this.textBuffer += this.currentChar;
	
	                } else if(this.textBufferCounter >= this.length) {
	                    SceneHandler.getInstance().removeScene(this);
	                    
	                    if(this.callback != null) {
	                        this.callback.call(this.textBuffer);
	                    }					 
	                 }
					break;
				}				
			}
		}
		return true;
	}
}
