package io.github.team2.Game.Entity;

import abstractEngine.Camera.Camera;
import abstractEngine.CollisionSystem.CollisionListener;
import abstractEngine.EntitySystem.Entity;
import abstractEngine.SceneSystem.ISceneManager;
import abstractEngine.SceneSystem.SceneManager;
import io.github.team2.Game.Actions.Control.StartLevel;
import io.github.team2.Game.Manager.LevelManager;

public class StartLevelHandler implements CollisionListener {
    private ISceneManager sceneManager;
    private boolean level1 = false;
    private LevelManager levelManager;
    private int detectedLevel = 0;

    public StartLevelHandler() {
        this.sceneManager = SceneManager.getInstance();
        this.levelManager = LevelManager.getInstance();
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.PLANET_PLAYER) {
        	
            Planet planetEntity = null;
            if (a instanceof Planet) {
                planetEntity = (Planet) a;
            } else if (b instanceof Planet) {
                planetEntity = (Planet) b;
            }
        	
            if (planetEntity.getLevel() == 1) {
                
                System.out.println("LEVEL 1 starting");
                level1 = true;
                detectedLevel = 1;
            	
            	
            } else if (planetEntity.getLevel () == 2) {
                System.out.println("LEVEL 2 starting");
                level1 = true;
                detectedLevel = 2;
				
			} else if (planetEntity.getLevel () == 3) {
	            System.out.println("LEVEL 3 starting");
	            level1 = true;
	            detectedLevel = 3;
				
			}else if (planetEntity.getLevel () == 4) {
	            System.out.println("LEVEL 4 starting");
	            level1 = true;
	            detectedLevel = 4;
				
			}
            
            	
        } 
        

    }

    public void processAction(Camera camera) {
        if (level1) {
            // Reset the flag
            level1 = false;
            camera.cameraReset();

            // Set the current level in LevelManager
            if (detectedLevel > 0) {
                levelManager.setCurrentLevel(detectedLevel);
            }
            

                
            StartLevel beginLevel = new StartLevel(sceneManager, detectedLevel);
            beginLevel.execute();
        	

        
        }
    }
}