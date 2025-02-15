package io.github.team2.EntitySystem;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Move;
import io.github.team2.Actions.TriangleBehaviour;
import io.github.team2.InputSystem.Action;


public abstract class Dynamics <S extends Enum<S>, A extends Enum<A>>  extends Entity{
    protected float speed;
    private Action action;
    

    
    private HashMap<A, Action> moveMap;
    private S currentState;
    private A currentActionState;
	private Vector2 rotation;
    

    public Dynamics(float speed) {
        super();
        this.speed = speed;
        
        moveMap = new HashMap<>();
        currentState = null;
        currentActionState = null;
        action = null;
       
    }

    public Dynamics(Vector2 position, Vector2 direction, Vector2 rotation, float speed, S state, A actionState) {
        super(position, direction, rotation);
        this.speed = speed;
        
        moveMap = new HashMap<>();
        currentState = state;
        currentActionState = actionState;
        action = null;
        
        
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    
	// TODO: adjust in future 
	public <E extends Enum<E>> Action getAction(E moveKey) {
		
		action = moveMap.get(moveKey);
		
		
		
		return action;
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
