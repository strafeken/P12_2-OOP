package io.github.team2.Actions;

//import com.badlogic.gdx.math.Vector2;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.Drop;
//import io.github.team2.GameManager;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.InputSystem.Action;
import io.github.team2.SceneSystem.SceneID;
import io.github.team2.SceneSystem.SceneManager;
import io.github.team2.Utils.DisplayManager;

public class StartGame implements Action {
    private final SceneManager sm;

    public StartGame(SceneManager sceneManager) {
        this.sm = sceneManager;
    }

    @Override
    public void execute() {
    	
    	try {
    	
        if (sm.getCurrentSceneID() == SceneID.MAIN_MENU ||
            sm.getCurrentSceneID() == SceneID.GAME_OVER) {

            // Stop main menu music and play start sound
            AudioManager.getInstance().stopSoundEffect("mainmenu");
            AudioManager.getInstance().playSoundEffect("start");
//            GameManager.getInstance().resetGame();
            // Set next scene to game scene
            sm.setNextScene(SceneID.GAME_SCENE);

            // After scene changes, get all droplets and move them above half screen height
            for (Entity entity : sm.getCurrentScene().getEntityManager().getEntities()) {
                if (entity.getEntityType() == EntityType.DROP) {
                    Drop drop = (Drop)entity;
                    float halfHeight = DisplayManager.getScreenHeight() / 2;
                    if (drop.getPosition().y < halfHeight) {
                        // Position randomly above half screen height
                        float newY = halfHeight + (float)(Math.random() * halfHeight);
                        float currentX = drop.getPosition().x;
                        drop.getBody().setLocation(currentX, newY);
                    }
                }
            }
        }
        
        } catch (Exception e) {
			
			System.out.println("error in game start " + e);
		}
        
    }
}
