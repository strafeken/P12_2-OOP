package io.github.team2.InputSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.InputProcessor;

public class InputManager implements InputProcessor {

	private Map<Integer, Action> keyDownActions;
	private Map<Integer, Action> keyUpActions;
	private Map<Integer, Action> touchDownActions;
	private Set<Integer> activeKeys;

	public InputManager() {
		keyDownActions = new HashMap<>();
		keyUpActions = new HashMap<>();
		touchDownActions = new HashMap<>();
		activeKeys = new HashSet<>();
	}

	public void registerKeyDown(int keycode, Action action) {
		keyDownActions.put(keycode, action);
	}

	public void registerKeyUp(int keycode, Action action) {
		keyUpActions.put(keycode, action);
	}

	public void registerTouchDown(int button, Action action) {
		touchDownActions.put(button, action);
	}

	@Override
	public boolean keyDown(int keycode) {
		activeKeys.add(keycode);

		Action action = keyDownActions.get(keycode);

		if (action == null)
			return false;

		action.execute();

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		activeKeys.remove(keycode);

		Action action = keyUpActions.get(keycode);

		if (action == null)
			return false;

		action.execute();

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Action action = touchDownActions.get(button);

		if (action == null)
			return false;

		action.execute();

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	public void update() {
		for (Integer key : activeKeys) {
			Action action = keyDownActions.get(key);

			if (action != null)
				action.execute();
		}
	}

	public void clearActiveKeys() {
		activeKeys.clear();
	}
}
