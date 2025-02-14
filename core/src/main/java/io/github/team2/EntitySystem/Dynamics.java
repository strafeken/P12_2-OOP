package io.github.team2.EntitySystem;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.TriangleBehaviour;
import io.github.team2.InputSystem.Action;


public abstract class Dynamics <S extends Enum<S>, A extends Enum<A>>  extends Entity{
    protected float speed;
    
    // TODO: when done shift to dynamic class
    
    private HashMap<A, Action> moveMap;
    private S currentState;
    private A currentActionState;
    

    public Dynamics(float speed) {
        super();
        this.speed = speed;
        
        moveMap = new HashMap<>();
        currentState = null;
        currentActionState = null;
       
    }

    public Dynamics(Vector2 position, Vector2 direction, float speed, S state, A actionState) {
        super(position, direction);
        this.speed = speed;
        
        moveMap = new HashMap<>();
        currentState = state;
        currentActionState = actionState;
        
        
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    
    
    
    public  HashMap<A, Action> getMoveMap() {
        return (HashMap<A, Action>) moveMap;
    }

    public void setCurrentActionState(A actionState) {
        currentActionState = actionState;  // Cast needed if `currentActionState` is strongly typed
    }

    public A getCurrentActionState() {
        return currentActionState;  
    }

    public void setCurrentState(S state) {
        currentState = state;  
    }

    public S  getCurrentState() {
        return currentState;
    }
    
    
    
    
    
    
    
    
    /*
    public HashMap<TriangleBehaviour.Move, Action> getMoveMap() {
		return moveMap;
	}



	public void setCurrentActionState(TriangleBehaviour.Move moveState) {
		currentActionState = moveState;
	}

	public TriangleBehaviour.Move getCurrentActionState() {
		return currentActionState;
	}

	public void setCurrentState(TriangleBehaviour.State state) {
		currentState = state;
	}

	public TriangleBehaviour.State getCurrentState() {
		return currentState;
	}

	abstract public void initActionMoveMap();
	*/
    
    
	public void clearMoveMap() {

		moveMap.clear();
	}
  
    
    
    @Override
    public void update() {
        // Update position based on direction and speed
        Vector2 currentPos = getPosition();
        Vector2 direction = getDirection();
        currentPos.add(direction.x * speed, direction.y * speed);
        setPosition(currentPos);

        // Update physics body
        updateBody();
    }

    /**
     * Check if the entity is moving
     * @return true if the entity has non-zero speed and direction
     */
    public boolean isMoving() {
        return speed != 0 && !getDirection().isZero();
    }
}
