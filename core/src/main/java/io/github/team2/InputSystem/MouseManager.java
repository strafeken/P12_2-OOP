package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.Cards.Card;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.Utils.DisplayManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MouseManager {
	private final List<Clickable> clickables;
	private final List<Draggable> draggables;
    private final Map<Integer, Action> mouseDownActions;
    private final Map<Integer, Action> mouseUpActions;
    private final Set<Integer> previousPressedButtons;
    private Draggable selectedDraggable;
    
    public MouseManager()
    {
    	clickables = new ArrayList<>();
    	draggables = new ArrayList<>();
        mouseDownActions = new HashMap<>();
        mouseUpActions = new HashMap<>();
        previousPressedButtons = new HashSet<>();
        selectedDraggable = null;
    }

    public void registerClickable(Clickable clickable) {
        clickables.add(clickable);
    }
    
    public void registerDraggable(Draggable draggable) {
        draggables.add(draggable);
    }
    
    public void deregisterDraggable(Draggable draggable) {
        draggables.remove(draggable);
        
        if (selectedDraggable == draggable) {
            selectedDraggable.stopDragging();
            selectedDraggable = null;
        }
    }

    public void registerMouseDown(int button, Action action) {
        mouseDownActions.put(button, action);
    }

    public void registerMouseUp(int button, Action action) {
        mouseUpActions.put(button, action);
    }

    public void update() {
    	 if (!Gdx.input.isTouched()) {
    	        if (selectedDraggable != null) {
    	            selectedDraggable.stopDragging();
    	            selectedDraggable = null;
    	        }
    	        return;
    	    }

    	    Vector2 touchPos = new Vector2(Gdx.input.getX(), DisplayManager.getScreenHeight() - Gdx.input.getY());

    	    if (Gdx.input.isTouched()) {
    	        // If currently dragging, continue updating the dragged object
    	        if (selectedDraggable != null && draggables.contains(selectedDraggable)) {
    	            selectedDraggable.updateDragging();
    	        } else {
    	            // Try to select a new draggable object
    	            for (Draggable draggable : draggables) {
    	                if (draggable instanceof DynamicTextureObject<?, ?>) {
    	                    DynamicTextureObject<?, ?> entity = (DynamicTextureObject<?, ?>) draggable;
    	                    if (entity instanceof Card && ((Card) entity).getBounds().contains(touchPos)) {
    	                        // Release the previously selected draggable before assigning a new one
    	                        if (selectedDraggable != null) {
    	                            selectedDraggable.stopDragging();
    	                        }

    	                        selectedDraggable = draggable;
    	                        selectedDraggable.startDragging();
    	                        return;
    	                    }
    	                }
    	            }

    	            // If no draggable was found, check for clickable objects
    	            for (Clickable clickable : clickables) {
    	                if (clickable.isPressed(touchPos)) {
    	                    clickable.execute();
    	                    return;
    	                }
    	            }
    	        }
    	    } else if (selectedDraggable != null) {
    	        // Release the dragged object
    	        selectedDraggable.stopDragging();
    	        selectedDraggable = null;
    	    }

    	    checkForMouseClicks();
    }
    
    private void checkForMouseClicks() {
	    Set<Integer> currentPressedButtons = new HashSet<>();

	    for (int button = 0; button < 3; button++) {
	        if (Gdx.input.isButtonPressed(button)) {
	            currentPressedButtons.add(button);
	            if (!previousPressedButtons.contains(button) && mouseDownActions.containsKey(button)) {
	                mouseDownActions.get(button).execute();
	            }
	        } else if (previousPressedButtons.contains(button) && mouseUpActions.containsKey(button)) {
	            mouseUpActions.get(button).execute();
	        }
	    }

	    previousPressedButtons.clear();
	    previousPressedButtons.addAll(currentPressedButtons);
    }
}
