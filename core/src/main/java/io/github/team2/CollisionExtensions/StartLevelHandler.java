package io.github.team2.CollisionExtensions;

import io.github.team2.Camera;
import io.github.team2.Actions.StartLevel1;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;

import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.SceneSystem.ISceneManager;

public class StartLevelHandler implements CollisionListener {
	private ISceneManager sceneManager;
	private boolean level1 = false;

	public StartLevelHandler() {
		this.sceneManager = SceneManager.getInstance();
	}

	@Override
	public void onCollision(Entity a, Entity b, CollisionType type) {
		if (type == CollisionType.LEVEL1_PLAYER) {
			System.out.println("LEVEL 1 starting");
			level1 = true;

		}
	}

	public void processAction(Camera camera) {
		if (level1) {
			// Reset the flag
			level1 = false;
			camera.cameraReset();

			StartLevel1 beginLevel1 = new StartLevel1(sceneManager);
			beginLevel1.execute();

		}

	}

}
