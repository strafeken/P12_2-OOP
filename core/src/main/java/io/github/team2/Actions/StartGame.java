package io.github.team2.Actions;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.ISceneManager;

public class StartGame implements Action {
    private final ISceneManager sm;

    public StartGame(ISceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        // Stop the main menu background music before switching scenes
        AudioManager.getInstance(AudioManager.class).stopMusic();

        if (sm.getCurrentSceneID() == SceneID.MAIN_MENU || sm.getCurrentSceneID() == SceneID.GAME_OVER) {
            sm.setNextScene(SceneID.GAME_SCENE);
        }
    }
}
