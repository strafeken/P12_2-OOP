package io.github.team2.Game.Actions.Control;

import io.github.team2.Abstract.InputSystem.Action;
import io.github.team2.Abstract.SceneSystem.ISceneManager;
import io.github.team2.Abstract.SceneSystem.SceneID;

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
