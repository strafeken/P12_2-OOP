package io.github.team2.InputSystem;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Actions.Move;
import io.github.team2.Actions.Rotate;
import io.github.team2.EntitySystem.Entity;
//import io.github.team2.GameManager;
import java.util.Map;

public class PlayerInputManager extends InputManager {
    private Entity player;

    public PlayerInputManager(Entity player) {
        super();
        this.player = player;
        initializeDefaultBindings();
        // Only register if we have a valid player
        if (player != null) {
            System.out.println("[DEBUG] Initializing PlayerInputManager with player: " + player);
            registerUserInput();
        } else {
            System.out.println("[DEBUG] PlayerInputManager initialized with null player");
        }
    }

    @Override
    public void setKeyBindings(Map<String, Integer> newBindings) {
        System.out.println("[DEBUG] PlayerInputManager setting new bindings: " + newBindings);

        // Clear ALL existing bindings first
//        keyboardManager.clearAllBindings();
        keyBindings.clear();

        if (newBindings != null && !newBindings.isEmpty()) {
            // Update the bindings map
            keyBindings.putAll(newBindings);

            // Register the new bindings with current player
            if (player != null) {
                System.out.println("[DEBUG] Registering new bindings for player");
                registerUserInput();
            } else {
                System.out.println("[ERROR] Cannot register bindings - no player available");
            }
        }
        System.out.println("[DEBUG] Final keyBindings state: " + keyBindings);
    }

    @Override
    public void registerUserInput() {
        if (player == null) {
            System.out.println("[ERROR] Cannot register input - player is null");
            return;
        }

        try {
//            keyboardManager.clearAllBindings();
            System.out.println("[DEBUG] Current keyBindings: " + keyBindings);

            for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
                String action = entry.getKey();
                int keycode = entry.getValue();

                switch (action) {
                    case "MOVE_UP":
                        keyboardManager.registerKeyDown(keycode, new Move(player, new Vector2(0, 1)));
                        keyboardManager.registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
                        System.out.println("[DEBUG] Registered UP with key: " + Input.Keys.toString(keycode));
                        break;
                    case "MOVE_LEFT":
                        keyboardManager.registerKeyDown(keycode, new Move(player, new Vector2(-1, 0)));
                        keyboardManager.registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
                        System.out.println("[DEBUG] Registered LEFT with key: " + Input.Keys.toString(keycode));
                        break;
                    case "MOVE_DOWN":
                        keyboardManager.registerKeyDown(keycode, new Move(player, new Vector2(0, -1)));
                        keyboardManager.registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
                        System.out.println("[DEBUG] Registered DOWN with key: " + Input.Keys.toString(keycode));
                        break;
                    case "MOVE_RIGHT":
                        keyboardManager.registerKeyDown(keycode, new Move(player, new Vector2(1, 0)));
                        keyboardManager.registerKeyUp(keycode, new Move(player, new Vector2(0, 0)));
                        System.out.println("[DEBUG] Registered RIGHT with key: " + Input.Keys.toString(keycode));
                        break;
                    case "ROTATE_CLOCKWISE": 
                        keyboardManager.registerKeyDown(keycode, new Rotate(player, new Vector2(1, 0)));
                        keyboardManager.registerKeyUp(keycode, new Rotate(player, new Vector2(0, 0)));
                        System.out.println("[DEBUG] Registered CLOCKWISE with key: " + Input.Keys.toString(keycode));
                        break;
                    case "ROTATE_ANTICLOCKWISE": 
                        keyboardManager.registerKeyDown(keycode, new Rotate(player, new Vector2(-1, 0)));
                        keyboardManager.registerKeyUp(keycode, new Rotate(player, new Vector2(0, 0)));
                        System.out.println("[DEBUG] Registered ANTICLOCKWISE with key: " + Input.Keys.toString(keycode));
                        break;

                }
            }
            System.out.println("[DEBUG] All bindings registered successfully");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to register bindings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setPlayer(Entity player) {
        this.player = player;
        if (player != null) {
            System.out.println("[DEBUG] Setting new player reference and re-registering bindings");
            registerUserInput();
        }
    }
}
