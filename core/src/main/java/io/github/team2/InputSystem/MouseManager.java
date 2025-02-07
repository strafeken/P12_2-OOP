package io.github.team2.InputSystem;

import com.badlogic.gdx.math.Vector2;
import io.github.team2.SceneSystem.SceneManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MouseManager {
    private Map<Integer, Action> touchDownActions;
    private List<Button> buttons;

    public MouseManager() {
        touchDownActions = new HashMap<>();
        buttons = new ArrayList<>();
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touchPos = new Vector2(screenX, SceneManager.screenHeight - screenY);

        for (Button btn : buttons) {
            if (btn.checkIsPressed(touchPos)) {
                btn.execute();
                return true;
            }
        }

        Action action = touchDownActions.get(button);
        if (action != null) {
            action.execute();
            return true;
        }

        return false;
    }

    public void registerTouchDown(int button, Action action) {
        touchDownActions.put(button, action);
    }

    public void registerButton(Button button) {
        buttons.add(button);
    }
}
