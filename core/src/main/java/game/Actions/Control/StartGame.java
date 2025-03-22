package game.Actions.Control;

import abstractengine.input.Action;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.SceneID;

public class StartGame implements Action {
    private final ISceneManager sm;

    public StartGame(ISceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {

        if (sm.getCurrentSceneID() == SceneID.MAIN_MENU || sm.getCurrentSceneID() == SceneID.GAME_OVER) {
            sm.setNextScene(SceneID.GAME_SCENE);
        }
    }
}
