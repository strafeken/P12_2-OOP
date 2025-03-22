package game.scene.control;

import abstractengine.input.Action;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.SceneID;

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
