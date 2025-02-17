package io.github.team2.Actions;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;

public class ResumeGame implements Action {
	private final SceneManager sm;

	public ResumeGame(SceneManager sceneManager) {
		this.sm = sceneManager;
	}

	@Override
	public void execute() {
		if (sm.getCurrentSceneID() == SceneID.PAUSE_MENU || sm.getCurrentSceneID() == SceneID.SETTINGS_MENU) {
			sm.removeOverlay();
		}
	}
}