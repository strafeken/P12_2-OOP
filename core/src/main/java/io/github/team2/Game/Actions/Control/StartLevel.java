package io.github.team2.Game.Actions.Control;



import io.github.team2.Abstract.InputSystem.Action;
import io.github.team2.Abstract.SceneSystem.ISceneManager;
import io.github.team2.Abstract.SceneSystem.SceneID;

public class StartLevel implements Action{

    private final ISceneManager sm;
    private int level;

    public StartLevel(ISceneManager sceneManager, int level) {
	 
        this.sm = sceneManager;
        this.level = level;
    }

    @Override
    public void execute() {

        if (sm.getCurrentSceneID() == SceneID.LEVEL_SELECT) {
        	
        	if (level == 1) {
        	
            sm.setNextScene(SceneID.LEVEL1);
	            
	        } else if (level == 2) {
	        	sm.setNextScene(SceneID.LEVEL2);
	        
	        } else if (level == 3) {
	        	sm.setNextScene(SceneID.LEVEL3);
	        
	        } else if (level == 4) {
	        	sm.setNextScene(SceneID.LEVEL4);
	        } 
        	
        	
			
		}
    }
	
	
}
