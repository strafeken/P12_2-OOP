package game.Actions.Control;

import abstractEngine.InputSystem.Action;
import abstractEngine.SceneSystem.ISceneManager;
import abstractEngine.SceneSystem.SceneID;

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
