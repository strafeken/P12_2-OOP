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

    // Constants for layout - adjusted to prevent overlapping
    private static final float TITLE_Y = 500;
    private static final float START_Y = 420;
    private static final float SPACING = 55;
    private static final float LEFT_MARGIN = 180;
    private static final float TEXT_WIDTH = 150; // Estimated width for text
    private static final float PANEL_LEFT = 80;  // Panel left edge
    private static final float PANEL_RIGHT = Gdx.graphics.getWidth() - 160;  // Panel right edge
    private static final float VOLUME_TEXT_OFFSET = 60;  // Increased spacing above volume slider
    
    // Volume control fields - moved below key bindings
    private float sliderX;
    private float sliderY;
    private float sliderWidth = 300;
    private float sliderHeight = 18;
    private boolean draggingSlider = false;
    
    // Colors
    private final Color backgroundColor = new Color(0.1f, 0.1f, 0.15f, 0.9f);
    private final Color titleColor = new Color(0.9f, 0.9f, 1f, 1);
    private final Color textColor = new Color(0.8f, 0.8f, 0.85f, 1);
    private final Color sliderBackgroundColor = new Color(0.2f, 0.2f, 0.25f, 1);
    private final Color sliderFillColor = new Color(0.4f, 0.7f, 1f, 1);
    private final Color sliderHandleColor = new Color(0.9f, 0.9f, 1f, 1);

    @Override
    public void load() {
        keyBindButtons = new HashMap<>();
//        PlayerInputManager playerInputManager = GameManager.getInstance().getPlayerInputManager();
//        currentBindings = new HashMap<>(playerInputManager.getKeyBindings());

        // Calculate the position of the volume slider based on number of key bindings
        calculateVolumeSliderPosition();

        // Initialize audio manager
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.loadSoundEffect("start", "sounds/start.mp3");
        audioManager.loadSoundEffect("ding", "sounds/ding.mp3");
        audioManager.loadSoundEffect("mainmenu", "sounds/mainmenu.mp3");

//        backButton = new Button("backBtn.png",
//        	    new Vector2(PANEL_LEFT + 40, 50), // Position inside panel with margin
//        	    () -> {
//        	        GameManager.getInstance().getPlayerInputManager().setKeyBindings(currentBindings);
//        	        SceneManager.getInstance(SceneManager.class).removeOverlay();
//        	    },
//        	    80, 32
//        	     
//        	);
//        inputManager.registerButton(backButton);

        createKeyBindButtons();
        shapeRenderer = new ShapeRenderer();
    }

    private void calculateVolumeSliderPosition() {
        int bindingCount = currentBindings.size();
        // Add extra spacing above volume controls
        sliderY = START_Y - (bindingCount * SPACING) - VOLUME_TEXT_OFFSET;
        sliderX = LEFT_MARGIN;
    }

    private void createKeyBindButtons() {
        float y = START_Y;
        int id = 2;

//        for (String action : currentBindings.keySet()) {
//            // Position buttons near right edge of panel
//            Button bindButton = new Button(
//                id++,
//                "Change",
//                "keyboard.png",
//                new Vector2(PANEL_RIGHT - 52, y - 15), 
//                () -> startBinding(action),
//                32,
//                32
//            );
//            keyBindButtons.put(action, bindButton);
//            inputManager.registerButton(bindButton);
//            y -= SPACING;
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
                        AudioManager.getInstance().playSoundEffect("ding");
                    } else {
                        currentBindings.put(currentBinding, i);
//                        GameManager.getInstance().getPlayerInputManager().setKeyBindings(currentBindings);
                        AudioManager.getInstance().playSoundEffect("start");
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

            // Check if touch is within slider bounds (with extended touch area)
            boolean inSliderBounds = touchX >= sliderX - 10 && touchX <= sliderX + sliderWidth + 10 &&
                                   touchY >= sliderY - 15 && touchY <= sliderY + sliderHeight + 15;

            // Start dragging if touching slider or continue if already dragging
            if (inSliderBounds || draggingSlider) {
                draggingSlider = true;
                float normalized = Math.min(1f, Math.max(0f, (touchX - sliderX) / sliderWidth));
                AudioManager.getInstance().setVolume(normalized);
            }
        } else {
            draggingSlider = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw background panel
        batch.end();
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(80, 30, Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 60);
        shapeRenderer.end();
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
        batch.begin();
        
        // Draw title
        textManager.draw(batch, "SETTINGS", LEFT_MARGIN, TITLE_Y, titleColor);
        
        // Draw key bindings
        float y = START_Y;
        for (Map.Entry<String, Integer> binding : currentBindings.entrySet()) {
            String text = binding.getKey() + ": " + Input.Keys.toString(binding.getValue());
            if (waitingForKey && currentBinding.equals(binding.getKey())) {
                text += " (Press any key)";
                textManager.draw(batch, text, LEFT_MARGIN, y, Color.YELLOW);
            } else {
                textManager.draw(batch, text, LEFT_MARGIN, y, textColor);
            }
            // Draw the button (already positioned in createKeyBindButtons)
            keyBindButtons.get(binding.getKey()).draw(batch);
            y -= SPACING;
        }

        // Draw volume controls header with increased spacing
        textManager.draw(batch, "VOLUME", LEFT_MARGIN, sliderY + VOLUME_TEXT_OFFSET, titleColor);
        drawVolumeSlider(batch);
        
        // Draw back button
        backButton.draw(batch);

        // Draw error message if any
        if (!errorMessage.isEmpty()) {
            textManager.draw(batch, errorMessage, LEFT_MARGIN, 60, Color.RED);
        }
    }

    private void drawVolumeSlider(SpriteBatch batch) {
        float volume = AudioManager.getInstance().getVolume();

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw background track
        shapeRenderer.setColor(sliderBackgroundColor);
        shapeRenderer.rect(sliderX, sliderY, sliderWidth, sliderHeight);

        // Draw filled portion
        shapeRenderer.setColor(sliderFillColor);
        shapeRenderer.rect(sliderX, sliderY, sliderWidth * volume, sliderHeight);

        // Draw handle
        float handleX = sliderX + (sliderWidth * volume);
        float handleSize = sliderHeight * 1.8f;
        shapeRenderer.setColor(sliderHandleColor);
        shapeRenderer.circle(handleX, sliderY + (sliderHeight / 2), handleSize / 2);

        shapeRenderer.end();

        batch.begin();
        textManager.draw(batch, (int)(volume * 100) + "%", sliderX + sliderWidth + 20, sliderY + sliderHeight/2 + 6, textColor);
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
        AudioManager.getInstance().playSoundEffect("ding");
    }
}