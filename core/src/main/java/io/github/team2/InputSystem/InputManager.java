package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.Move;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.GameManager;
import java.util.Map;
import java.util.HashMap;

public class InputManager {
    protected KeyboardManager keyboardManager;
    protected MouseManager mouseManager;

    private Map<String, Integer> keyBindings;

    public InputManager() {
        keyboardManager = new KeyboardManager();
        mouseManager = new MouseManager();
        keyBindings = new HashMap<>();
        initializeDefaultBindings();
    }

    private void initializeDefaultBindings() {
        // Default movement keys
        keyBindings.put("MOVE_UP", Input.Keys.W);
        keyBindings.put("MOVE_LEFT", Input.Keys.A);
        keyBindings.put("MOVE_DOWN", Input.Keys.S);
        keyBindings.put("MOVE_RIGHT", Input.Keys.D);
    }

    public void setKeyBinding(String action, int keycode) {
        // First remove any existing bindings for this key
        removeExistingBinding(keycode);
        keyBindings.put(action, keycode);
        updateActionBindings();
    }

    private void removeExistingBinding(int keycode) {
        keyBindings.entrySet().removeIf(entry -> entry.getValue() == keycode);
    }

    public int getKeyBinding(String action) {
        return keyBindings.getOrDefault(action, Input.Keys.UNKNOWN);
    }

    public void updateActionBindings() {
        // Clear existing bindings
        keyboardManager.clearAllBindings();

        // Re-register all actions with current keybindings
        registerUserInput();
    }

    public void registerUserInput() {
        Entity player = GameManager.getInstance().getPlayerEntityManager().getPlayer();

        // Register movement actions with current keybindings
        registerKeyDown(getKeyBinding("MOVE_UP"), new Move(player, new Vector2(0, 1)));
        registerKeyDown(getKeyBinding("MOVE_LEFT"), new Move(player, new Vector2(-1, 0)));
        registerKeyDown(getKeyBinding("MOVE_DOWN"), new Move(player, new Vector2(0, -1)));
        registerKeyDown(getKeyBinding("MOVE_RIGHT"), new Move(player, new Vector2(1, 0)));

        // Register key up events
        registerKeyUp(getKeyBinding("MOVE_UP"), new Move(player, new Vector2(0, 0)));
        registerKeyUp(getKeyBinding("MOVE_LEFT"), new Move(player, new Vector2(0, 0)));
        registerKeyUp(getKeyBinding("MOVE_DOWN"), new Move(player, new Vector2(0, 0)));
        registerKeyUp(getKeyBinding("MOVE_RIGHT"), new Move(player, new Vector2(0, 0)));
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

    public void update() {
        keyboardManager.update();

        if (Gdx.input.isTouched()) {
            mouseManager.touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0);
        }
    }

    public void clearActiveKeys() {
        keyboardManager.clearActiveKeys();
    }

    // Helper method to get current key bindings map
    public Map<String, Integer> getKeyBindings() {
        return new HashMap<>(keyBindings);
    }
}
