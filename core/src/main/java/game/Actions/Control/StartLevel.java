package game.Actions.Control;



import abstractengine.input.Action;
import abstractengine.scene.ISceneManager;
import abstractengine.scene.SceneID;

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
