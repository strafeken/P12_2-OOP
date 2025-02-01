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
        if (sm.getCurrentSceneID() == SceneID.MAIN_MENU) {
            AudioManager.getInstance().stopMusic(); // Stop main menu music
            AudioManager.getInstance().playSoundEffect("start"); // Play start sound effect
            sm.setNextScene(SceneID.GAME_SCENE);
        }
    }
}