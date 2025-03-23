package application.scene.control;

import abstractengine.io.Action;
import abstractengine.scene.ISceneManager;
import application.scene.SceneID;

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
