package io.github.team2.InputSystem;

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

    public boolean keyDown(int keycode) {
        activeKeys.add(keycode);
        Action action = keyDownActions.get(keycode);
        if (action == null)
            return false;
        action.execute();
        return true;
    }

    public boolean keyUp(int keycode) {
        activeKeys.remove(keycode);
        Action action = keyUpActions.get(keycode);
        if (action == null)
            return false;
        action.execute();
        return true;
    }

    public void registerKeyDown(int keycode, Action action) {
        keyDownActions.put(keycode, action);
    }

    public void registerKeyUp(int keycode, Action action) {
        keyUpActions.put(keycode, action);
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
