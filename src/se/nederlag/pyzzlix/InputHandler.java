package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.events.EventHandler;
import se.nederlag.pyzzlix.events.EventKeyState;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		EventKeyState event = new EventKeyState(EventKeyState.State.DOWN, keycode);
		EventHandler.getInstance().post(event);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		EventKeyState event = new EventKeyState(EventKeyState.State.UP, keycode);
		EventHandler.getInstance().post(event);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
