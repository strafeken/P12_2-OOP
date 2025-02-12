package io.github.team2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.SceneSystem.SceneManager;


import java.util.HashMap;
import io.github.team2.Actions.DropBehaviour;
import io.github.team2.Actions.Move;
// maybe dn
import io.github.team2.InputSystem.Action;





public class Drop extends DynamicTextureObject {
    
  
    // TODO: when done shift to dynamic class
    private HashMap<DropBehaviour.Move, Action> moveMap;
    // movment states can move to dynamic
    private DropBehaviour.State currentState;
    private DropBehaviour.Move currentActionState;

  
    public Drop(String texture) {
        super(new Texture(texture));
        setEntityType(EntityType.DROP);
        setPosition(new Vector2(0, 0));
        setSpeed(10);
      
        moveMap = new HashMap<>();
        currentState = DropBehaviour.State.IDLE;
        currentActionState = DropBehaviour.Move.NONE;
        initActionMoveMap();
    }

    public Drop(EntityType type, String texture, Vector2 position, Vector2 direction, float speed) {
        super(new Texture(texture));
        setEntityType(type);
        setPosition(position);
        setDirection(direction);
        setSpeed(speed);
      
        moveMap = new HashMap<>();
		    currentState = DropBehaviour.State.IDLE;
		    currentActionState = DropBehaviour.Move.NONE;
		    initActionMoveMap();
      
    }
  
  
  
  

	public boolean checkPosition() {

		if (this.getPosition().y < 0) {

			return true;
		}

		return false;
	}

  
  

    public boolean isOutOfBounds() {
        return getPosition().y < 0;
    }

    private void resetPosition() {
        Random random = new Random();
        getBody().setLocation(
            random.nextFloat() * SceneManager.screenWidth,
            SceneManager.screenHeight
        );
    }

    private void handleDropMiss() {
        GameManager.getInstance().getPointsManager().incrementFails();
    }
  
  
  
	// TODO: move to dynamic class
	
	@Override
	public <E extends Enum<E>> Action getAction(E moveKey) {

		Action action = moveMap.get(moveKey);

		return action;
	}

	public void setCurrentActionState(DropBehaviour.Move moveState) {
		currentActionState = moveState;
	}

	public DropBehaviour.Move getCurrentActionState() {
		return currentActionState;
	}

	public void setCurrentState(DropBehaviour.State state) {
		currentState = state;
	}

	public DropBehaviour.State getCurrentState() {
		return currentState;
	}

	public void initActionMoveMap() {

		moveMap.put(DropBehaviour.Move.DROP, new Move(this, new Vector2(0, -1)));

	}

	public void clearMoveMap() {

		moveMap.clear();
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
				if (checkPosition()) {
					
					// Increment fail counter through GameScene
	                if (GameScene.getInstance().getPointsManager() != null) {
	                    GameScene.getInstance().getPointsManager().incrementFails();
	                }
	           
	            Random random =new Random();
	            getBody().setLocation(random.nextFloat() * SceneManager.screenWidth, SceneManager.screenHeight);
	            
				}
				break;
				

			default:
				
				System.out.println("Unknown direction drop");
				break;
			}

			getAction(getCurrentActionState()).execute();

		}

	else{
		
		System.out.println("Unknown state");

		}

	}

  
/*
	@Override
	public void update() {

		updateMovement();
		updateBody();
	}
  */

    @Override
    public void update() {
        if (getBody() != null) {  // Add null check
            if (!isOutOfBounds()) {
                if (getAction() != null) {
                    getAction().execute();
                }
            } else {
                handleDropMiss();
                resetPosition();
            }
            updateBody();
        }
    }

}
