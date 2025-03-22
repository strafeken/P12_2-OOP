package io.github.team2.Game.Actions.Movement;

import com.badlogic.gdx.math.Vector2;

import abstractEngine.EntitySystem.Dynamics;
import abstractEngine.EntitySystem.Entity;
import abstractEngine.InputSystem.Action;
import io.github.team2.Game.Entity.EntityType;

public class Chase implements Action{
	
	
	private Entity entity;
	private Entity target;
	
    private float chaseSpeed;

    private float maxDistance = 500f; // Maximum distance to chase player
    private float minDistance = 100f; // Increased from 50f to keep alien further from player


	public Chase(Entity entity, Entity target,int level) {
		this.entity = entity;
		this.target = target;
		


        switch (level) {
            case 2:  	chaseSpeed = 50f; break;
            case 3: 	chaseSpeed = 80f; break;
            case 4: 	chaseSpeed = 120f; break;
            default: 	chaseSpeed = 30f;
        }
        

		
	}

	@Override
	public void execute() {
		
		
		  if (entity != null && entity.getPhysicsBody() != null) {
	            // Get player and alien positions
	            Vector2 entityPos = entity.getPosition();
	            Vector2 targetPos = target.getPosition();

	            // Calculate direction to player
	            Vector2 direction = new Vector2(targetPos.x - entityPos.x, targetPos.y - entityPos.y);
	            float distance = direction.len();
	            
	            // Only move if player is within range and not too close
	            if (distance < maxDistance && distance > minDistance) {
	                // Normalize and scale by chase speed
	                direction.nor();
	                direction.scl(chaseSpeed);

	                // Apply movement
	                entity.getPhysicsBody().getBody().setLinearVelocity(direction);
	            } else if (distance <= minDistance) {
	                // Stop if too close
	            	entity.getPhysicsBody().getBody().setLinearVelocity(0, 0);
	            } else {
	                // Slow down if too far
	            	entity.getPhysicsBody().getBody().setLinearVelocity(0, 0);
	            }
	        }
		
		
		
		
		}

}
