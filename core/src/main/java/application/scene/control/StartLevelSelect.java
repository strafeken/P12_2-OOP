package application.scene.control;

import abstractengine.input.Action;
import abstractengine.scene.ISceneManager;
import application.scene.SceneID;

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
