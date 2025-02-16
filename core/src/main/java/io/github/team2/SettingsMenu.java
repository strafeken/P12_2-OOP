package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.PlayerInputManager;
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
    private Map<String, Integer> currentBindings;
    private String errorMessage = "";
    private float errorTimer = 0;

    // Volume control fields
    private float sliderX = 200;
    private float sliderY = 50;
    private float sliderWidth = 200;
    private float sliderHeight = 20;
    private boolean draggingSlider = false;

    @Override
    public void load() {
        keyBindButtons = new HashMap<>();
//        PlayerInputManager playerInputManager = GameManager.getInstance().getPlayerInputManager();
//        currentBindings = new HashMap<>(playerInputManager.getKeyBindings());

        // Initialize audio manager
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.loadSoundEffect("start", "sounds/start.mp3");
        audioManager.loadSoundEffect("ding", "sounds/ding.mp3");
        audioManager.loadSoundEffect("mainmenu", "sounds/mainmenu.mp3");

//        backButton = new Button(1, "Back", "backBtn.png",
//            new Vector2(100, 50),
//            () -> {
////                GameManager.getInstance().getPlayerInputManager().setKeyBindings(currentBindings);
//                SceneManager.getInstance(SceneManager.class).removeOverlay();
//            },
//            100, 50);

//        inputManager.registerButton(backButton);
        createKeyBindButtons();
        shapeRenderer = new ShapeRenderer();
    }

    private void createKeyBindButtons() {
        float y = 400;
        int id = 2;
//        for (String action : currentBindings.keySet()) {
//            Button bindButton = new Button(
//                id++,
//                "Change",
//                "keyboard.png",
//                new Vector2(400, y),
//                () -> startBinding(action),
//                80, 30
//            );
//            keyBindButtons.put(action, bindButton);
////            inputManager.registerButton(bindButton);
//            y -= 50;
//        }
    }

    @Override
    public void update() {
        if (waitingForKey) {
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    String duplicate = null;
                    for (Map.Entry<String, Integer> entry : currentBindings.entrySet()) {
                        if (entry.getValue() == i && !entry.getKey().equals(currentBinding)) {
                            duplicate = entry.getKey();
                            break;
                        }
                    }
                    if (duplicate != null) {
                        errorMessage = Input.Keys.toString(i) + " is already used for " + duplicate;
                        errorTimer = 3.0f;

                    } else {
                        currentBindings.put(currentBinding, i);
//                        GameManager.getInstance().getPlayerInputManager().setKeyBindings(currentBindings);

                    }
                    waitingForKey = false;
                    currentBinding = "";
                    break;
                }
            }
        }

        updateVolumeSlider();
//        inputManager.update();

        if (errorTimer > 0) {
            errorTimer -= Gdx.graphics.getDeltaTime();
            if (errorTimer <= 0) {
                errorMessage = "";
            }
        }
    }

    private void updateVolumeSlider() {
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if touch is within slider bounds
            boolean inSliderBounds = touchX >= sliderX && touchX <= sliderX + sliderWidth &&
                                   touchY >= sliderY && touchY <= sliderY + sliderHeight;

            // Start dragging if touching slider or continue if already dragging
            if (inSliderBounds || draggingSlider) {
                draggingSlider = true;
                float normalized = Math.min(1f, Math.max(0f, (touchX - sliderX) / sliderWidth));
                AudioManager.getInstance().setVolume(normalized);
                Gdx.app.log("SettingsMenu", "Volume updated to: " + normalized);
            }
        } else {
            draggingSlider = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        textManager.draw(batch, "Settings", 200, 450, Color.WHITE);

        float y = 400;
        for (Map.Entry<String, Integer> binding : currentBindings.entrySet()) {
            String text = binding.getKey() + ": " + Input.Keys.toString(binding.getValue());
            if (waitingForKey && currentBinding.equals(binding.getKey())) {
                text += " (Press any key)";
            }
            textManager.draw(batch, text, 200, y, Color.WHITE);
            keyBindButtons.get(binding.getKey()).draw(batch);
            y -= 50;
        }

        backButton.draw(batch);
        drawVolumeSlider(batch);

        if (!errorMessage.isEmpty()) {
            textManager.draw(batch, errorMessage, 200, 100, Color.RED);
        }
    }

    private void drawVolumeSlider(SpriteBatch batch) {
        float volume = AudioManager.getInstance().getVolume();

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw background track
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(sliderX, sliderY, sliderWidth, sliderHeight);

        // Draw filled portion
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(sliderX, sliderY, sliderWidth * volume, sliderHeight);

        // Draw handle
        float handleX = sliderX + (sliderWidth * volume);
        float handleSize = sliderHeight * 1.5f;
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.circle(handleX, sliderY + (sliderHeight / 2), handleSize / 2);

        shapeRenderer.end();

        batch.begin();
        textManager.draw(batch, "Volume: " + (int)(volume * 100) + "%",sliderX, sliderY + 40, Color.WHITE);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // No additional shapes to draw
    }

    @Override
    public void unload() {
        dispose();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }

    private void startBinding(String action) {
        waitingForKey = true;
        currentBinding = action;

    }
}
