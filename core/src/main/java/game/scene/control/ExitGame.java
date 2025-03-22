package game.scene.control;

import com.badlogic.gdx.Gdx;

import abstractengine.input.Action;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.SceneID;

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
