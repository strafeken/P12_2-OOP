package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.Rotate;
import io.github.team2.EntitySystem.Entity;

public class PInputManager {
	private final Entity player;
	private final KeyboardManager keyboardManager;
    private final MouseManager mouseManager;

    public PInputManager(Entity player) {
        this.player = player;
        keyboardManager = new KeyboardManager();
        mouseManager = new MouseManager();
        registerUserInput();
    }

    private void registerUserInput() {
    	// key down actions
        keyboardManager.registerKeyDown(Input.Keys.W, new Move(player, new Vector2(0, 1)));
        keyboardManager.registerKeyDown(Input.Keys.A, new Move(player, new Vector2(-1, 0)));
        keyboardManager.registerKeyDown(Input.Keys.S, new Move(player, new Vector2(0, -1)));
        keyboardManager.registerKeyDown(Input.Keys.D, new Move(player, new Vector2(1, 0)));
        keyboardManager.registerKeyDown(Input.Keys.Q, new Rotate(player, new Vector2(-1, 0)));
        keyboardManager.registerKeyDown(Input.Keys.E, new Rotate(player, new Vector2(1, 0)));
        // key up actions to set velocity to 0 / stop movement
        keyboardManager.registerKeyUp(Input.Keys.W, new Move(player, new Vector2(0, 0)));
        keyboardManager.registerKeyUp(Input.Keys.A, new Move(player, new Vector2(0, 0)));
        keyboardManager.registerKeyUp(Input.Keys.S, new Move(player, new Vector2(0, 0)));
        keyboardManager.registerKeyUp(Input.Keys.D, new Move(player, new Vector2(0, 0)));
        keyboardManager.registerKeyUp(Input.Keys.Q, new Move(player, new Vector2(0, 0)));
        keyboardManager.registerKeyUp(Input.Keys.E, new Move(player, new Vector2(0, 0)));
    }
    
    public void changeKeyBinding(int oldKeycode, int newKeycode, boolean isKeyDown) {
        keyboardManager.changeKeyBinding(oldKeycode, newKeycode, isKeyDown);
    }
    
    public KeyboardManager getKeyboardManager() {
        return keyboardManager;
    }
    
    public void update() {
        keyboardManager.update();
        if (Gdx.input.isTouched()) {
        	mouseManager.update();        	
        }
    }
}
