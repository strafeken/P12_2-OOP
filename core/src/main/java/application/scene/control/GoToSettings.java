package application.scene.control;

import abstractengine.io.Action;
import abstractengine.scene.ISceneManager;
import application.scene.SceneID;

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
