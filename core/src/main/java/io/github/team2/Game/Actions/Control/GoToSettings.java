package io.github.team2.Game.Actions.Control;

import abstractEngine.InputSystem.Action;
import abstractEngine.SceneSystem.ISceneManager;
import abstractEngine.SceneSystem.SceneID;

public class GoToSettings implements Action {
    private final ISceneManager sm;

    public GoToSettings(ISceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        sm.overlayScene(SceneID.SETTINGS_MENU);
    }
}
