package io.github.team2.Game.Actions.Control;

import abstractEngine.InputSystem.Action;
import abstractEngine.SceneSystem.ISceneManager;
import abstractEngine.SceneSystem.SceneID;

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
