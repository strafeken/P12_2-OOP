package io.github.team2.Actions;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;

public class GoToSettings implements Action {
	private final SceneManager sm;

	public GoToSettings(SceneManager sceneManager) {
		this.sm = sceneManager;
	}

	@Override
	public void execute() {
		if (sm.getCurrentSceneID() == SceneID.GAME_SCENE) {
			sm.overlayScene(SceneID.SETTINGS_MENU);
		}
	}
}
