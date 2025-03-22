package game.scene.control;

import abstractengine.input.Action;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.SceneID;

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
