package io.github.team2.InputSystem;

import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.math.Vector2;
//import io.github.team2.SceneSystem.SceneManager;
//import io.github.team2.InputSystem.KeyboardManager;

public class InputManager implements InputProcessor {
    protected KeyboardManager keyboardManager;
    protected MouseManager mouseManager;

    public InputManager() {
        keyboardManager = new KeyboardManager();
        mouseManager = new MouseManager();
    }

    public void registerKeyDown(int keycode, Action action) {
        keyboardManager.registerKeyDown(keycode, action);
    }

    public void registerKeyUp(int keycode, Action action) {
        keyboardManager.registerKeyUp(keycode, action);
    }

    public void registerTouchDown(int button, Action action) {
        mouseManager.registerTouchDown(button, action);
    }

    public void registerButton(Button button) {
        mouseManager.registerButton(button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return keyboardManager.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return keyboardManager.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return mouseManager.touchDown(screenX, screenY, pointer, button);
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
        keyboardManager.update();
    }

    public void clearActiveKeys() {
        keyboardManager.clearActiveKeys();
    }
}
