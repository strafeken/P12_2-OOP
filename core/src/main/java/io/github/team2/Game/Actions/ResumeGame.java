package io.github.team2.Game.Actions;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.ISceneManager;

public class ResumeGame implements Action {
    private final ISceneManager sm;

    public ResumeGame(ISceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        sm.removeOverlay();
    }
}
