package io.github.team2.Actions;

import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;

public class StartGame implements Action {
    private final SceneManager sm;

    public StartGame(SceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
        // Stop the main menu background music before switching scenes
        AudioManager.getInstance(AudioManager.class).stopSoundEffect("mainmenu");

        if (sm.getCurrentSceneID() == SceneID.MAIN_MENU || sm.getCurrentSceneID() == SceneID.GAME_OVER) {
            sm.setNextScene(SceneID.GAME_SCENE);
        }
    }
}
