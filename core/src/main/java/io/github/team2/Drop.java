package io.github.team2;


import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.TextureObject;
import io.github.team2.SceneSystem.SceneManager;


public class Drop extends TextureObject {
	
	// private float dropSpeed = 100;

	public Drop(String texture) {
		setEntityType(EntityType.DROP);
		setTexture(new Texture(texture));
		setPosition(new Vector2(0, 0));
		setDirection(new Vector2(0, 0));
		setSpeed(10);
	}

	public Drop(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
		setEntityType(type);
		setTexture(new Texture(texture));
		setPosition(position);
		setDirection(direction);
		setSpeed(speed);
		
	}


	

	
	public boolean checkPosition() {

		if (this.getPosition().y < 0) {

			return true;
		}

		return false;
	}

	
	public void updateMovement() {

        if (this.checkPosition() == false) {
        	
        	if (getAction() != null) getAction().execute();
        	
        } else {
            
            // Check if drop hit bottom
            //if (this.getPosition().y < 0) {
                // Increment fail counter through GameScene
                if (GameScene.getInstance().getPointsManager() != null) {
                    GameScene.getInstance().getPointsManager().incrementFails();
                }
            //}
            Random random =new Random();
            getBody().setLocation(random.nextFloat() * SceneManager.screenWidth, SceneManager.screenHeight);
        }
		
	}
	
	
	@Override
	public void update() {
		
		updateMovement();
		updateBody();
	}

}
