package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyboardManager {
	private Map<Integer, Action> keyDownActions;
    private Map<Integer, Action> keyUpActions;
    private Set<Integer> previousPressedKeys;

    public KeyboardManager() {
        keyDownActions = new HashMap<>();
        keyUpActions = new HashMap<>();
        previousPressedKeys = new HashSet<>();
    }

    public void registerKeyDown(int keycode, Action action) {
        keyDownActions.put(keycode, action);
    }

    public void registerKeyUp(int keycode, Action action) {
        keyUpActions.put(keycode, action);
    }
    
    public void changeKeyBinding(int oldKeycode, int newKeycode, boolean isKeyDown) {
        if (isKeyDown) {
            Action action = keyDownActions.get(oldKeycode);
            if (action != null) {
                keyDownActions.remove(oldKeycode);
                registerKeyDown(newKeycode, action);  // Register new key binding
            }
        } else {
            Action action = keyUpActions.get(oldKeycode);
            if (action != null) {
                keyUpActions.remove(oldKeycode);
                registerKeyUp(newKeycode, action);  // Register new key binding
            }
        }
    }
    
    public void update() {
        Set<Integer> currentPressedKeys = new HashSet<>();
        Set<Integer> keysToCheck = new HashSet<>();
        
        keysToCheck.addAll(keyDownActions.keySet());
        keysToCheck.addAll(keyUpActions.keySet());

        for (Integer key : keysToCheck) {
            if (Gdx.input.isKeyPressed(key)) {
                currentPressedKeys.add(key);
                // execute the key down action every frame the key is held
                Action action = keyDownActions.get(key);
                
                if (action != null)
                    action.execute();
            } else {
                // if the key was pressed in the previous frame and now is released, trigger key up
                if (previousPressedKeys.contains(key)) {
                    Action action = keyUpActions.get(key);
                    
                    if (action != null)
                        action.execute();
                }
            }
        }
        
        previousPressedKeys = currentPressedKeys;
    }
}
