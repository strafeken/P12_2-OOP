package io.github.team2.InputSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.team2.Utils.DisplayManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MouseManager {
	private List<Clickable> clickables;
    private Map<Integer, Action> mouseDownActions;
    private Map<Integer, Action> mouseUpActions;
    private Set<Integer> previousPressedButtons;
    
    public MouseManager()
    {
    	clickables = new ArrayList<>();
        mouseDownActions = new HashMap<>();
        mouseUpActions = new HashMap<>();
        previousPressedButtons = new HashSet<>();
    }

    public void registerClickable(Clickable clickable) {
        clickables.add(clickable);
    }

    public void registerMouseDown(int button, Action action) {
        mouseDownActions.put(button, action);
    }

    public void registerMouseUp(int button, Action action) {
        mouseUpActions.put(button, action);
    }

    public void update() {
        Vector2 touchPos = new Vector2(Gdx.input.getX(), DisplayManager.getScreenHeight() - Gdx.input.getY());
        
        // check if a clickable component is clicked
        if (clickables != null) {
        	for (Clickable clickable : clickables) {
        		if (clickable != null && clickable.isPressed(touchPos)) {
        			clickable.execute();
        			return;
        		}
        	}
        }

        // check for mouse click functions
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
