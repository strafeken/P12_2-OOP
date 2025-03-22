package abstractengine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyboardManager {
	private final Map<Integer, Action> keyDownActions;
    private final Map<Integer, Action> keyUpActions;
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

    // Improve getKeyDownActions method
    public Map<Integer, Action> getKeyDownActions() {
        // Return a defensive copy to maintain encapsulation
        return new HashMap<>(keyDownActions);
    }

    // Add a corresponding method for key up actions
    public Map<Integer, Action> getKeyUpActions() {
        // Return a defensive copy to maintain encapsulation
        return new HashMap<>(keyUpActions);
    }

    // Add more specific methods to access key bindings
    public Action getActionForKeyDown(int keycode) {
        return keyDownActions.get(keycode);
    }

    public Action getActionForKeyUp(int keycode) {
        return keyUpActions.get(keycode);
    }

    // Return boolean to indicate success/failure
    public boolean changeKeyBinding(int oldKeycode, int newKeycode, boolean isKeyDown) {
        if (isKeyDown) {
            Action action = keyDownActions.get(oldKeycode);
            if (action != null) {
                keyDownActions.remove(oldKeycode);
                registerKeyDown(newKeycode, action);
                printKeyBindingChange(oldKeycode, newKeycode);
                return true;
            }
        } else {
            Action action = keyUpActions.get(oldKeycode);
            if (action != null) {
                keyUpActions.remove(oldKeycode);
                registerKeyUp(newKeycode, action);
                printKeyBindingChange(oldKeycode, newKeycode);
                return true;
            }
        }
        return false;
    }

    private void printKeyBindingChange(int oldKeycode, int newKeycode) {
        String oldKeyName = Input.Keys.toString(oldKeycode);
        String newKeyName = Input.Keys.toString(newKeycode);
        System.out.println("Key binding changed from " + oldKeycode + " (" + oldKeyName + ") to " + newKeycode + " (" + newKeyName + ")");
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
