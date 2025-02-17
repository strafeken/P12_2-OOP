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
import io.github.team2.InputSystem.PInputManager;
import io.github.team2.InputSystem.PlayerInputManager;
import io.github.team2.SceneSystem.Scene;
import io.github.team2.SceneSystem.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsMenu extends Scene {
	private PInputManager playerInputManager;
    private List<Button> buttons;
    private boolean waitingForNewKey;
    private Button selectedButton;
    private String currentBinding;
    private Map<String, Integer> keyBindings;  // Store key bindings by action name
    private Button backButton;
    
    private AudioManager audioManager;
    
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
        selectedButton = null;
        currentBinding = null;
        keyBindings = new HashMap<>();
    	
    	playerInputManager = GameManager.getInstance(GameManager.class).getPlayerInputManager();
        
    	createButtonsForKeyBindings();
        
        backButton = new Button("backBtn.png",
        	    new Vector2(PANEL_LEFT + 40, 50), // Position inside panel with margin
        	    new ResumeGame(SceneManager.getInstance(SceneManager.class)), 80, 32);
        
        gameInputManager.registerClickable(backButton);
        
        backButton.update();
        
        audioManager = AudioManager.getInstance(AudioManager.class);
    }

    private void createButtonsForKeyBindings() {
        Map<Integer, Action> keyDownActions = playerInputManager.getKeyboardManager().getKeyDownActions();
        
        float y = START_Y;

        // Create a button for each key
        for (Integer key : keyDownActions.keySet()) {
            String keyName = Input.Keys.toString(key);
            System.out.println(keyName);
            Action action = keyDownActions.get(key);

            Button button = new Button("keyboard.png", new Vector2(PANEL_RIGHT - 52, y - 15), () -> {
                waitingForNewKey = true;
                currentBinding = keyName;  // Set current binding when the button is clicked
            }, 32, 32);

            buttons.add(button);
            gameInputManager.registerClickable(button);
            y -= SPACING;

            // Store the initial key bindings in the map
            keyBindings.put(keyName, key);
        }
        
        sliderY = START_Y - (keyBindings.size() * SPACING) - VOLUME_TEXT_OFFSET;
        sliderX = LEFT_MARGIN;
    }

    @Override
    public void update() {
        if (waitingForNewKey) {
            // Look for any key that is pressed
            for (int i = 0; i < 256; i++) {  // Loop over the range of possible key codes
                if (Gdx.input.isKeyPressed(i)) {
                    int newKeyCode = i;  // Capture the pressed key's code

                    if (currentBinding != null) {
                        // Check for duplicate key bindings
                        if (keyBindings.containsValue(newKeyCode) && !keyBindings.containsKey(currentBinding)) {
                            errorMessage = Input.Keys.toString(newKeyCode) + " is already assigned to another action.";
                            errorTimer = 3.0f;
                        } else {
                            keyBindings.put(currentBinding, newKeyCode);
                        }
                    } else {
                        // Call changeKeyBinding here
                        playerInputManager.changeKeyBinding(keyBindings.get(currentBinding), newKeyCode, true);
                        keyBindings.put(currentBinding, newKeyCode);  // Update the key binding map
                    }

                    waitingForNewKey = false;  // Reset the flag to stop waiting
                    currentBinding = null;  // Reset current binding once the key is set
                    break;  // Exit the loop after capturing the key press
                }
            }
        }

        for (Button button : buttons) {
            button.update();
        }

        updateVolumeSlider();

        gameInputManager.update();
        playerInputManager.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
    	textManager.draw(batch, "SETTINGS", LEFT_MARGIN, TITLE_Y, titleColor);
    	
    	float y = START_Y;  // Starting position for the text
        
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);

            // Directly get the key name from the keyBindings map using the index
            String keyName = (String) keyBindings.keySet().toArray()[i];  // Retrieve the key name from keyBindings

            String displayText = keyName + ": " + Input.Keys.toString(keyBindings.get(keyName));  // Get the current key binding from the map

            // If waiting for a new key press for the current binding, show the prompt
            if (waitingForNewKey && currentBinding != null && currentBinding.equals(keyName)) {
                displayText += ": (Press any key)";
                textManager.draw(batch, displayText, LEFT_MARGIN, y, Color.YELLOW);  // Display prompt in yellow
            } else {
                textManager.draw(batch, displayText, LEFT_MARGIN, y, Color.WHITE);  // Display the current key binding
            }

            button.draw(batch);  // Draw the button
            y -= SPACING;  // Move down for the next button
        }
 
        
        // Draw volume controls header with increased spacing
        textManager.draw(batch, "VOLUME", LEFT_MARGIN, sliderY + VOLUME_TEXT_OFFSET, titleColor);
        
        float volume = audioManager.getVolume();
        textManager.draw(batch, (int)(volume * 100) + "%", sliderX + sliderWidth + 20, sliderY + sliderHeight/2 + 6, textColor);
        
        // Draw back button
        backButton.draw(batch);
        
        if (errorTimer > 0) {
            errorTimer -= Gdx.graphics.getDeltaTime();
            textManager.draw(batch, errorMessage, LEFT_MARGIN, 60, Color.RED);
        }
    }


    @Override
    public void draw(ShapeRenderer shape) {
//        shape.setColor(backgroundColor);
//        shape.rect(80, 30, Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 60);
        
        // Draw background track
    	shape.setColor(sliderBackgroundColor);
    	shape.rect(sliderX, sliderY, sliderWidth, sliderHeight);

        // Draw filled portion
    	float volume = audioManager.getVolume();
    	shape.setColor(sliderFillColor);
    	shape.rect(sliderX, sliderY, sliderWidth * volume, sliderHeight);

        // Draw handle
        float handleX = sliderX + (sliderWidth * volume);
        float handleSize = sliderHeight * 1.8f;
        shape.setColor(sliderHandleColor);
        shape.circle(handleX, sliderY + (sliderHeight / 2), handleSize / 2);
    }

    @Override
    public void unload() {
        // Cleanup
    }

    @Override
    public void dispose() {
        // Dispose resources
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
                audioManager.setVolume(normalized);
            }
        } else {
            draggingSlider = false;
        }
    }
}