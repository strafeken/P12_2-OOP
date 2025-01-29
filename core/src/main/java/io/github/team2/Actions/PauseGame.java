package io.github.team2.Actions;

import io.github.team2.Action;
import io.github.team2.SceneID;
import io.github.team2.SceneManager;

public class PauseGame implements Action {

	private SceneManager sm;

    public PauseGame(SceneManager sceneManager)
    {
        sm = sceneManager;
    }

    @Override
    public void execute()
    {
        if (sm.getCurrentSceneID() == SceneID.GAME_SCENE)
            sm.overlayScene(SceneID.PAUSE_MENU);
    }
}
