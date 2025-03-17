package io.github.team2.Actions;



import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.ISceneManager;
import io.github.team2.SceneSystem.SceneID;

public class StartLevel1 implements Action{

    private final ISceneManager sm;

    public StartLevel1(ISceneManager sceneManager) {
	 
        this.sm = sceneManager;
    }

    @Override
    public void execute() {

        if (sm.getCurrentSceneID() == SceneID.LEVEL_SELECT) {
            sm.setNextScene(SceneID.GAME_SCENE);
            
        }
    }
	
	
}
