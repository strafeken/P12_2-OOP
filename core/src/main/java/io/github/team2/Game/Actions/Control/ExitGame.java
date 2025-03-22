package io.github.team2.Game.Actions.Control;

import com.badlogic.gdx.Gdx;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.ISceneManager;

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
