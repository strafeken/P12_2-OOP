package game.Actions.Control;

import abstractEngine.InputSystem.Action;
import abstractEngine.SceneSystem.ISceneManager;
import abstractEngine.SceneSystem.SceneID;

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
