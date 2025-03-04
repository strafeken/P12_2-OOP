package io.github.team2.Actions;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.ISceneManager;

public class GoToSettings implements Action {
    private final ISceneManager sm;

    public GoToSettings(ISceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        sm.setNextScene(SceneID.SETTINGS_MENU);
    }
}
