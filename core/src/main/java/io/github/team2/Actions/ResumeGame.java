package io.github.team2.Actions;

import io.github.team2.Action;
import io.github.team2.SceneID;
import io.github.team2.SceneManager;

public class ResumeGame implements Action {
    private final SceneManager sm;

    public ResumeGame(SceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        if (sm.getCurrentSceneID() == SceneID.PAUSE_MENU) {
            sm.removeOverlay();
        }
    }
}