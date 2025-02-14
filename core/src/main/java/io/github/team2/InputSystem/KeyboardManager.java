package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyboardManager {
    private Map<Integer, Action> keyDownActions;
    private Map<Integer, Action> keyUpActions;
    private Set<Integer> activeKeys;

    public KeyboardManager() {
        keyDownActions = new HashMap<>();
        keyUpActions = new HashMap<>();
        activeKeys = new HashSet<>();
    }

    public void registerKeyDown(int keycode, Action action) {
        keyDownActions.put(keycode, action);
    }

    public void registerKeyUp(int keycode, Action action) {
        keyUpActions.put(keycode, action);
    }

    public void update() {
        Set<Integer> keysToRemove = new HashSet<>();

        // Handle key presses
        keyDownActions.forEach((keycode, action) -> {
            if (Gdx.input.isKeyJustPressed(keycode)) {
                activeKeys.add(keycode);
                action.execute();
            }
        });

        // Handle key releases
        activeKeys.forEach(keycode -> {
            if (!Gdx.input.isKeyPressed(keycode)) {
                keysToRemove.add(keycode);
                Action action = keyUpActions.get(keycode);
                if (action != null) {
                    action.execute();
                }
            }
        });

        // Remove released keys
        activeKeys.removeAll(keysToRemove);
    }

    public void clearBinding(int keycode) {
        keyDownActions.remove(keycode);
        keyUpActions.remove(keycode);
        activeKeys.remove(keycode);
    }

    public void clearAllBindings() {
        keyDownActions.clear();
        keyUpActions.clear();
        activeKeys.clear();
    }

    public void clearActiveKeys() {
        activeKeys.clear();
    }
}
