package io.github.team2;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.Actions.Chase;
import io.github.team2.AlienBehaviour.Move;
import io.github.team2.AlienBehaviour.State;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;

public class Alien extends DynamicTextureObject<AlienBehaviour.State, AlienBehaviour.Move> {
    private Entity targetPlayer;
    

    // Add respawn position to remember where to place the alien when respawning
    private Vector2 respawnPosition;

    public Alien(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
            float speed, State state, Move actionState) {
        super(type, texture, size, position, direction, rotation, speed, state, actionState);
        // Store initial position for respawning
        this.respawnPosition = new Vector2(position);
    }
    
    
    @Override
 	public void initActionMap() {
    	
    	getActionMap().put(AlienBehaviour.Move.CHASE, new Chase(this, targetPlayer));
 	}
    
    
    public void updateMovement() {
    	
		// move from default
		if (getCurrentState() == AlienBehaviour.State.IDLE) {
			
			setCurrentActionState(AlienBehaviour.Move.CHASE);
			setCurrentState(AlienBehaviour.State.MOVING);
		} 
		
		else if (getCurrentState() == AlienBehaviour.State.MOVING) {
			switch (getCurrentActionState()) {
			
			case NONE:
				// state not changed
				System.out.println("Alien state stuck in NONE");
				return;

			case CHASE:

				
				break;


			default:
				System.out.println("Unknown direction");
				break;
			}
    		
			
			getAction(getCurrentActionState()).execute();

    }
    


    
    }
    
    
    @Override
    public void update() {
    	
    	updateMovement();
        // Update the physics body
        updateBody();
    }

    // Method to respawn the alien at its initial position
    public void respawn() {
        if (getPhysicsBody() != null) {
            getPhysicsBody().setLocation(respawnPosition.x, respawnPosition.y);
            getPhysicsBody().getBody().setLinearVelocity(0, 0);
        }
    }

    public void setTargetPlayer(Entity player) {
        this.targetPlayer = player;
        
        initActionMap();
    }

    public Entity getTargetPlayer() {
        return targetPlayer;
    }
}
