package io.github.team2;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import io.github.team2.Actions.ResumeGame;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.InputSystem.Button;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;
import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Scene {
    private ShapeRenderer shapeRenderer;
    private Map<String, Button> keyBindButtons;
    private Button backButton;
    private boolean waitingForKey = false;
    private String currentBinding = "";

    private float sliderX = 200;
    private float sliderY = 300;
    private float sliderWidth = 200;
    private float sliderHeight = 20;
    private boolean draggingSlider = false;

    @Override
    public void load() {
        // Initialize button map
        keyBindButtons = new HashMap<>();

        // Add back button with null texture (will use default rendering)
        backButton = new Button(1, "Back", "backBtn.png",  //change null to your png for button
            new Vector2(100, 50),
            () -> SceneManager.getInstance(SceneManager.class).removeOverlay(),
            100, 50);

        inputManager.registerButton(backButton);
        createKeyBindButtons();
        
        // Initialize ShapeRenderer
        shapeRenderer = new ShapeRenderer();
    }

    private void createKeyBindButtons() {
        float y = 400;
        int id = 2;

        // Create a button for each key binding with null texture
        for (String action : GameManager.getInstance().getPlayerInputManager().getKeyBindings().keySet()) {
            Button bindButton = new Button(
                id++,
                "Change",
                "keyboard.png",  //change null to your png for button for keybind.
                new Vector2(400, y),
                () -> startBinding(action),
                80, 30
            );
            keyBindButtons.put(action, bindButton);
            inputManager.registerButton(bindButton);
            y -= 50;
        }
    }

    @Override
    public void update() {
        if (waitingForKey) {
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    GameManager.getInstance().getPlayerInputManager().setKeyBinding(currentBinding, i);
                    waitingForKey = false;
                    currentBinding = "";
                    break;
                }
            }
        }
        // Update slider
        updateVolumeSlider();

        inputManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw title
        textManager.draw(batch, "Settings", 200, 450, Color.WHITE);

        // Draw current bindings
        float y = 400;
        Map<String, Integer> bindings = GameManager.getInstance().getPlayerInputManager().getKeyBindings();

        for (Map.Entry<String, Integer> binding : bindings.entrySet()) {
            String text = binding.getKey() + ": " + Input.Keys.toString(binding.getValue());
            if (waitingForKey && currentBinding.equals(binding.getKey())) {
                text += " (Press any key)";
            }
            textManager.draw(batch, text, 200, y, Color.WHITE);
            keyBindButtons.get(binding.getKey()).draw(batch);
            y -= 50;
        }

        backButton.draw(batch);

        // Draw volume slider last
        drawVolumeSlider(batch);
    }

    private void startBinding(String action) {
        waitingForKey = true;
        currentBinding = action;
    }

    private void updateVolumeSlider() {
        // Check if the mouse is within the slider bounds
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // invert y
            boolean inSlider = (touchX >= sliderX && touchX <= sliderX + sliderWidth
                && touchY >= sliderY && touchY <= sliderY + sliderHeight);

            if (inSlider) {
                draggingSlider = true;
            }
        } else {
            draggingSlider = false;
        }

        // If we’re dragging, adjust volume
        if (draggingSlider) {
            float touchX = Gdx.input.getX();
            // Convert position to 0.0 - 1.0 range
            float normalized = Math.min(1f, Math.max(0f, (touchX - sliderX) / sliderWidth));
            AudioManager.getInstance().setVolume(normalized);
        }
    }

    private void drawVolumeSlider(SpriteBatch batch) {
        float volume = AudioManager.getInstance().getVolume();
        
        // End SpriteBatch before using ShapeRenderer
        batch.end();
        
        // Use the instance variable instead of null
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(sliderX, sliderY, sliderWidth, sliderHeight);

        // Draw the slider "thumb"
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(sliderX, sliderY, sliderWidth * volume, sliderHeight);
        shapeRenderer.end();
        
        // Resume SpriteBatch for text
        batch.begin();
        textManager.draw(batch, "Volume: " + (int)(volume * 100) + "%", sliderX, sliderY + 40, Color.WHITE);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // No shapes to draw
    }

    @Override
    public void unload() {
        dispose();
    }

    @Override
    public void dispose() {
        // Cleanup
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }
}
