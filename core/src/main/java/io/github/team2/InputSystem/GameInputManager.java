package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;

public class GameInputManager {
	private final KeyboardManager keyboardManager;
    private final MouseManager mouseManager;
    
    public GameInputManager()
    {
    	keyboardManager = new KeyboardManager();
    	mouseManager = new MouseManager();
    }
    
    public void registerKeyDown(int keycode, Action action) {
    	keyboardManager.registerKeyDown(keycode, action);
    }
    
    public void registerKeyUp(int keycode, Action action) {
    	keyboardManager.registerKeyUp(keycode, action);
    }
    
    public void registerClickable(Clickable clickable) {
    	mouseManager.registerClickable(clickable);
    }

    public void registerMouseDown(int button, Action action) {
    	mouseManager.registerMouseDown(button, action);
    }

    public void registerMouseUp(int button, Action action) {
    	mouseManager.registerMouseUp(button, action);
    }
    
    public KeyboardManager getKeyboardManager() {
    	return keyboardManager;
    }
    
    public MouseManager getMouseManager() {
    	return mouseManager;
    }
    
    public void update() {
        keyboardManager.update();
        if (Gdx.input.isTouched())
        	mouseManager.update();
    }
}
