package game.Actions.Control;

import abstractengine.input.Action;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.SceneID;

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
