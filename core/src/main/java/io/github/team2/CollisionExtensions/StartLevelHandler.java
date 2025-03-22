package io.github.team2.CollisionExtensions;

import io.github.team2.Actions.StartLevel;
import io.github.team2.Camera.Camera;
import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.Game.Entity.EntityType;
import io.github.team2.Game.Entity.Planet;
import io.github.team2.Game.Manager.LevelManager;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.SceneSystem.ISceneManager;

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