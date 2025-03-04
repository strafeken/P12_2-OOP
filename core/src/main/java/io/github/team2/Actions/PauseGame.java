package io.github.team2.Actions;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.ISceneManager;

public class PauseGame implements Action {
    private final ISceneManager sm;

    public PauseGame(ISceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        sm.overlayScene(SceneID.PAUSE_MENU);
    }
}
