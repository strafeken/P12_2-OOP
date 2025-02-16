package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.Move;
import io.github.team2.EntitySystem.Entity;
//import io.github.team2.GameManager;
import java.util.Map;
import java.util.HashMap;

public class InputManager {
    protected KeyboardManager keyboardManager;
    protected MouseManager mouseManager;
    protected Map<String, Integer> keyBindings;

    public InputManager() {
        keyboardManager = new KeyboardManager();
        mouseManager = new MouseManager();
        keyBindings = new HashMap<>();
        // Don't call initializeDefaultBindings here - let subclasses handle it
    }

    protected void initializeDefaultBindings() {
        keyBindings.clear();
        keyBindings.put("MOVE_UP", Input.Keys.W);
        keyBindings.put("MOVE_LEFT", Input.Keys.A);
        keyBindings.put("MOVE_DOWN", Input.Keys.S);
        keyBindings.put("MOVE_RIGHT", Input.Keys.D);
        keyBindings.put("ROTATE_CLOCKWISE", Input.Keys.E);
        keyBindings.put("ROTATE_ANTICLOCKWISE", Input.Keys.Q);
        // Don't automatically register - let caller decide when to register
    }


    public void setKeyBindings(Map<String, Integer> newBindings) {
        System.out.println("[DEBUG] Setting new key bindings: " + newBindings);
        clearAllBindings();
        if (newBindings != null && !newBindings.isEmpty()) {
            keyBindings.putAll(newBindings);
            registerUserInput();
            System.out.println("[DEBUG] New bindings registered: " + keyBindings);
        }
    }

    public void clearAllBindings() {
        if (keyboardManager != null) {
//            keyboardManager.clearAllBindings();
//            keyboardManager.clearActiveKeys();
        }
        keyBindings.clear();
    }

    public void registerUserInput() {
        System.out.println("[DEBUG] Starting to register user input...");
//        GameManager gameManager = GameManager.getInstance();
//        if (gameManager == null) {
//            System.out.println("[ERROR] GameManager or PlayerEntityManager is null");
//            return;
//        }

        try {
//            keyboardManager.clearAllBindings();
            System.out.println("[DEBUG] Current keyBindings: " + keyBindings);

            for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
                String action = entry.getKey();
                int keycode = entry.getValue();
                System.out.println("[DEBUG] Registering " + action + " with key: " + Input.Keys.toString(keycode));

//                switch (action) {
//                    case "MOVE_UP":
//                        registerKeyDown(keycode, new Move(player, new Vector2(0, 1)));
//                        registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
//                        break;
//                    case "MOVE_LEFT":
//                        registerKeyDown(keycode, new Move(player, new Vector2(-1, 0)));
//                        registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
//                        break;
//                    case "MOVE_DOWN":
//                        registerKeyDown(keycode, new Move(player, new Vector2(0, -1)));
//                        registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
//                        break;
//                    case "MOVE_RIGHT":
//                        registerKeyDown(keycode, new Move(player, new Vector2(1, 0)));
//                        registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
//                        break;
//                }
            }
            System.out.println("[DEBUG] All bindings registered successfully");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to register bindings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyboardManager != null) {
            keyboardManager.update();
        }
//        if (mouseManager != null && Gdx.input.isTouched()) {
//            mouseManager.touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0);
//        }
    }

    public void registerKeyDown(int keycode, Action action) {
        if (keyboardManager != null) {
            keyboardManager.registerKeyDown(keycode, action);
        }
    }

    public void registerKeyUp(int keycode, Action action) {
        if (keyboardManager != null) {
            keyboardManager.registerKeyUp(keycode, action);
        }
    }

    public void registerButton(Button button) {
//        if (mouseManager != null) {
//            mouseManager.registerButton(button);
//        }
    }

    protected String getActionForKey(int keycode) {
        for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
            if (entry.getValue() == keycode) {
                return entry.getKey();
            }
        }
        return null;
    }

    public int getKeyBinding(String action) {
        return keyBindings.getOrDefault(action, Input.Keys.UNKNOWN);
    }

    public Map<String, Integer> getKeyBindings() {
        return new HashMap<>(keyBindings);
    }

    public void clearActiveKeys() {
        if (keyboardManager != null) {
//            keyboardManager.clearActiveKeys();
        }
    }
}
