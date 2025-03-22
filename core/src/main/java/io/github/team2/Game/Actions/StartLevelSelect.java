package io.github.team2.Game.Actions;

import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.SceneID;

public class StartLevelSelect implements Action{
	
	  private final ISceneManager sm;

	    public StartLevelSelect(ISceneManager sceneManager) {
	        this.sm = sceneManager;
	    }

	    @Override
	    public void execute() {

	        if (sm.getCurrentSceneID() == SceneID.MAIN_MENU || sm.getCurrentSceneID() == SceneID.GAME_OVER) {
	            sm.setNextScene(SceneID.LEVEL_SELECT);
	        }
	    }
	


}
