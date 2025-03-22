package io.github.team2.Game.Actions.Control;

import io.github.team2.Abstract.InputSystem.Action;
import io.github.team2.Abstract.SceneSystem.ISceneManager;
import io.github.team2.Abstract.SceneSystem.SceneID;

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
