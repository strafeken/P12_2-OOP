package io.github.team2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.ResumeGame;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.InputSystem.Action;
import io.github.team2.InputSystem.Button;
import io.github.team2.InputSystem.GameInputManager;
import io.github.team2.InputSystem.KeyboardManager;
import io.github.team2.InputSystem.PlayerInputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SettingsMenu extends Scene {
    private PlayerInputManager playerInputManager;
    private List<Button> buttons;
    private boolean waitingForNewKey;
    private String currentBinding;
    private Map<String, Integer> keyBindings;
    private Button backButton;
    
    private AudioManager audioManager;
    
    // Layout constants
    private static final float TITLE_Y = 500;
    private static final float START_Y = 420;
    private static final float SPACING = 55;
    private static final float LEFT_MARGIN = 180;
    private static final float TEXT_WIDTH = 150;
    private static final float PANEL_LEFT = 80;
    private static final float PANEL_RIGHT = Gdx.graphics.getWidth() - 160;
    private static final float VOLUME_TEXT_OFFSET = 60;
    
    // Volume control fields
    private float sliderX;
    private float sliderY;
    private float sliderWidth = 300;
    private float sliderHeight = 18;
    private boolean draggingSlider = false;
    
    // Visual styling
    private final Color overlayColor = new Color(0, 0, 0, 0.7f);
    private final Color panelColor = new Color(0.15f, 0.15f, 0.2f, 0.95f);
    private final Color borderColor = new Color(0.3f, 0.3f, 0.4f, 1f);
    private final Color titleColor = new Color(0.9f, 0.9f, 1f, 1);
    private final Color textColor = new Color(0.8f, 0.8f, 0.85f, 1);
    private final Color sliderBackgroundColor = new Color(0.2f, 0.2f, 0.25f, 1);
    private final Color sliderFillColor = new Color(0.4f, 0.7f, 1f, 1);
    private final Color sliderHandleColor = new Color(0.9f, 0.9f, 1f, 1);
    
    // Panel styling
    private static final float PANEL_PADDING = 20;
    private static final float PANEL_BORDER_WIDTH = 2;
    private static final float PANEL_CORNER_RADIUS = 10;
    
    private String errorMessage = "";
    private float errorTimer = 0;

    public SettingsMenu() {
        super();
    }

    @Override
    public void load() {
        // Initialize managers
        entityManager = new EntityManager();
        gameInputManager = new GameInputManager();
        textManager = new TextManager();
        
        buttons = new ArrayList<>();
        waitingForNewKey = false;
        currentBinding = null;
        keyBindings = new LinkedHashMap<>();
        
        playerInputManager = GameManager.getInstance(GameManager.class).getPlayerInputManager();
        
        createButtonsForKeyBindings();
        
        backButton = new Button("backBtn.png",
            new Vector2(PANEL_LEFT + 40, 50),
            new ResumeGame(SceneManager.getInstance(SceneManager.class)), 80, 32);
        
        gameInputManager.registerClickable(backButton);
        backButton.update();
        
        audioManager = AudioManager.getInstance(AudioManager.class);
    }

    private void createButtonsForKeyBindings() {
        Map<Integer, Action> keyDownActions = playerInputManager.getKeyboardManager().getKeyDownActions();
        float y = START_Y;

        for (Integer key : keyDownActions.keySet()) {
            String keyName = Input.Keys.toString(key);
            
            Button button = new Button("keyboard.png", new Vector2(PANEL_RIGHT - 52, y - 15), () -> {
                waitingForNewKey = true;
                currentBinding = keyName;
            }, 32, 32);

            buttons.add(button);
            gameInputManager.registerClickable(button);
            y -= SPACING;

            keyBindings.put(keyName, key);
        }
        
        sliderY = START_Y - (keyBindings.size() * SPACING) - VOLUME_TEXT_OFFSET;
        sliderX = LEFT_MARGIN;
    }

    @Override
    public void update() {
        if (waitingForNewKey) {
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyPressed(i)) {
                    int newKeyCode = i;

                    if (currentBinding != null) {
                        if (keyBindings.containsValue(newKeyCode) || !keyBindings.containsKey(currentBinding)) {
                            errorMessage = Input.Keys.toString(newKeyCode) + " is already assigned to another action.";
                            errorTimer = 3.0f;
                        } else {
                            playerInputManager.changeKeyBinding(keyBindings.get(currentBinding), newKeyCode, true);
                            playerInputManager.changeKeyBinding(keyBindings.get(currentBinding), newKeyCode, false);
                            keyBindings.put(currentBinding, newKeyCode);
                        }
                    }

                    waitingForNewKey = false;
                    currentBinding = null;
                    break;
                }
            }
        }

        for (Button button : buttons) {
            button.update();
        }

        updateVolumeSlider();
        gameInputManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // End sprite batch to draw shapes
        batch.end();
        
        // Set ShapeRenderer to filled
        ShapeRenderer shape = new ShapeRenderer();
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        
        // Draw full screen overlay
        shape.setColor(overlayColor);
        shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        // Draw panel background with rounded corners
        shape.setColor(panelColor);
        float x = PANEL_LEFT;
        float y = 30;
        float width = PANEL_RIGHT - PANEL_LEFT;
        float height = Gdx.graphics.getHeight() - 60;
        
        // Main panel rectangle
        shape.rect(x + PANEL_CORNER_RADIUS, y, width - 2 * PANEL_CORNER_RADIUS, height);
        shape.rect(x, y + PANEL_CORNER_RADIUS, width, height - 2 * PANEL_CORNER_RADIUS);
        
        // Corners
        shape.circle(x + PANEL_CORNER_RADIUS, y + PANEL_CORNER_RADIUS, PANEL_CORNER_RADIUS);
        shape.circle(x + width - PANEL_CORNER_RADIUS, y + PANEL_CORNER_RADIUS, PANEL_CORNER_RADIUS);
        shape.circle(x + PANEL_CORNER_RADIUS, y + height - PANEL_CORNER_RADIUS, PANEL_CORNER_RADIUS);
        shape.circle(x + width - PANEL_CORNER_RADIUS, y + height - PANEL_CORNER_RADIUS, PANEL_CORNER_RADIUS);
        
        // Draw border
        shape.setColor(borderColor);
        shape.rect(x - PANEL_BORDER_WIDTH, y - PANEL_BORDER_WIDTH, 
                  width + 2 * PANEL_BORDER_WIDTH, PANEL_BORDER_WIDTH); // Bottom
        shape.rect(x - PANEL_BORDER_WIDTH, y + height, 
                  width + 2 * PANEL_BORDER_WIDTH, PANEL_BORDER_WIDTH); // Top
        shape.rect(x - PANEL_BORDER_WIDTH, y, 
                  PANEL_BORDER_WIDTH, height); // Left
        shape.rect(x + width, y, 
                  PANEL_BORDER_WIDTH, height); // Right
        
        // Draw volume slider background
        shape.setColor(sliderBackgroundColor);
        shape.rect(sliderX, sliderY, sliderWidth, sliderHeight);

        // Draw volume slider fill
        float volume = audioManager.getVolume();
        shape.setColor(sliderFillColor);
        shape.rect(sliderX, sliderY, sliderWidth * volume, sliderHeight);

        // Draw volume slider handle
        float handleX = sliderX + (sliderWidth * volume);
        float handleSize = sliderHeight * 1.8f;
        shape.setColor(sliderHandleColor);
        shape.circle(handleX, sliderY + (sliderHeight / 2), handleSize / 2);
        
        shape.end();
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
        shape.dispose();
        
        // Resume sprite batch for text and buttons
        batch.begin();
        
        // Draw title and content
        textManager.draw(batch, "SETTINGS", LEFT_MARGIN, TITLE_Y, titleColor);
        
        float contentY = START_Y;
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            String keyName = (String) keyBindings.keySet().toArray()[i];
            String displayText = keyName + ": " + Input.Keys.toString(keyBindings.get(keyName));

            if (waitingForNewKey && currentBinding != null && currentBinding.equals(keyName)) {
                displayText += ": (Press any key)";
                textManager.draw(batch, displayText, LEFT_MARGIN, contentY, Color.YELLOW);
            } else {
                textManager.draw(batch, displayText, LEFT_MARGIN, contentY, Color.WHITE);
            }

            button.draw(batch);
            contentY -= SPACING;
        }
        
        // Draw volume controls
        textManager.draw(batch, "VOLUME", LEFT_MARGIN, sliderY + VOLUME_TEXT_OFFSET, titleColor);
        textManager.draw(batch, (int)(audioManager.getVolume() * 100) + "%", 
                        sliderX + sliderWidth + 20, sliderY + sliderHeight/2 + 6, textColor);
        
        backButton.draw(batch);
        
        if (errorTimer > 0) {
            errorTimer -= Gdx.graphics.getDeltaTime();
            textManager.draw(batch, errorMessage, LEFT_MARGIN, 60, Color.RED);
        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        // Empty because we handle all shape rendering in the main draw method
    }

    private void updateVolumeSlider() {
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            boolean inSliderBounds = touchX >= sliderX - 10 && touchX <= sliderX + sliderWidth + 10 &&
                                   touchY >= sliderY - 15 && touchY <= sliderY + sliderHeight + 15;

            if (inSliderBounds || draggingSlider) {
                draggingSlider = true;
                float normalized = Math.min(1f, Math.max(0f, (touchX - sliderX) / sliderWidth));
                audioManager.setVolume(normalized);
            }
        } else {
            draggingSlider = false;
        }
    }

    @Override
    public void unload() {
        // Cleanup
    }

    @Override
    public void dispose() {
        // Dispose resources
    }
}