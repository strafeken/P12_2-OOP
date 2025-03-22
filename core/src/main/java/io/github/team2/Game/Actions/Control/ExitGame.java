package io.github.team2.Game.Actions.Control;

import com.badlogic.gdx.Gdx;

import io.github.team2.Abstract.InputSystem.Action;
import io.github.team2.Abstract.SceneSystem.ISceneManager;
import io.github.team2.Abstract.SceneSystem.SceneID;

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
