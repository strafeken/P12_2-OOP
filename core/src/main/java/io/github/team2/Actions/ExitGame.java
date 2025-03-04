package io.github.team2.Actions;

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
        if (sm.getCurrentSceneID() == SceneID.PAUSE_MENU) {
            sm.setNextScene(SceneID.MAIN_MENU);
        } else {
            Gdx.app.exit();
        }
    }
}
