package io.github.team2.Actions;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;

public class ExitGame implements Action {
	private final SceneManager sm;

	public ExitGame(SceneManager sceneManager) {
		this.sm = sceneManager;
	}

	@Override
	public void execute() {
		if (sm.getCurrentSceneID() == SceneID.GAME_SCENE) {
			sm.setNextScene(SceneID.MAIN_MENU);
		}
	}
}