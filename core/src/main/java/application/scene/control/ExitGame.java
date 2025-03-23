package application.scene.control;

import com.badlogic.gdx.Gdx;

import abstractengine.io.Action;
import abstractengine.scene.ISceneManager;
import application.scene.SceneID;

public class ExitGame implements Action {
    private final ISceneManager sm;

    public ExitGame(ISceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        if (sm.getCurrentSceneID() == SceneID.GAME_SCENE) {
            sm.setNextScene(SceneID.MAIN_MENU);
        }
    }
}
