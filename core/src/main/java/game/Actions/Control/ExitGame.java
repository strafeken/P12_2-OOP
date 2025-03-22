package game.Actions.Control;

import com.badlogic.gdx.Gdx;

import abstractEngine.InputSystem.Action;
import abstractEngine.SceneSystem.ISceneManager;
import abstractEngine.SceneSystem.SceneID;

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
