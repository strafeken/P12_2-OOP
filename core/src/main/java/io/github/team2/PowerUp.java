package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.Actions.DropBehaviour;
import io.github.team2.Actions.Move;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.SceneSystem.SceneManager;


public class PowerUp extends DynamicTextureObject<DropBehaviour.State, DropBehaviour.Move> {
    private static final float SCALE = 0.1f;

    public PowerUp(EntityType type, String texture, Vector2 position, Vector2 direction, float speed,
    				DropBehaviour.State state, DropBehaviour.Move actionState) {
        super(new Texture(texture));
        setEntityType(type);
        setPosition(position);
        setDirection(direction);
        setSpeed(speed);
        
        initActionMoveMap();
    }

    @Override
    public void draw(SpriteBatch batch) {
        float drawX = getPosition().x - (getTexture().getWidth() * SCALE / 2);
        float drawY = getPosition().y - (getTexture().getHeight() * SCALE / 2);
        batch.draw(getTexture(),
                  drawX, drawY,
                  getTexture().getWidth() * SCALE,
                  getTexture().getHeight() * SCALE);
    }

    private boolean isOutOfBounds() {
        return getPosition().y < 0;
    }

    private void resetPosition() {
        Random random = new Random();
        getBody().setLocation(
            random.nextFloat() * SceneManager.screenWidth,
            SceneManager.screenHeight
        );
    }
    
    
	  public void initActionMoveMap() {
			 
		  getMoveMap().put(DropBehaviour.Move.DROP, new Move(this, new Vector2(0, -1)));
	 
	 }
    
	  
	  public void updateMovement() {

			if (getCurrentState() == DropBehaviour.State.IDLE) {

				setCurrentActionState(DropBehaviour.Move.DROP);
				setCurrentState(DropBehaviour.State.MOVING);
			}

			else if (getCurrentState() == DropBehaviour.State.MOVING) {

				switch (getCurrentActionState()) {

				case NONE:
					// state not changed
					System.out.println("drop state stuck in NONE");
					break;

				case DROP:

					// check if reach
					if (!isOutOfBounds()) {

						
						resetPosition();

					}
					break;

				default:

					System.out.println("Unknown direction drop");
					break;
				}

				getAction(getCurrentActionState()).execute();

			}
	  }
	  
	  
	  
    @Override
    public void update() {
        if (!isOutOfBounds()) {
            if (getAction(getCurrentActionState()) != null) {
                getAction(getCurrentActionState()).execute();
            }
        } else {
            resetPosition();
        }
        updateBody();
    }

    @Override
    public float getWidth() {
        return getTexture().getWidth() * SCALE;
    }

    @Override
    public float getHeight() {
        return getTexture().getHeight() * SCALE;
    }
}
