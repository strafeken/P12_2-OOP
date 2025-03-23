package application.scene.control;

import abstractengine.io.Action;
import abstractengine.scene.ISceneManager;
import application.scene.SceneID;

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
