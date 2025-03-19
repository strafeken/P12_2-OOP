package io.github.team2.Actions;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.Dynamics;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.InputSystem.Action;

public class Chase implements Action{
	
	
	private Entity entity;
	private Entity target;
	
    private float chaseSpeed = 30f; // Reduced from 60f to make alien slower
    private float maxDistance = 500f; // Maximum distance to chase player
    private float minDistance = 100f; // Increased from 50f to keep alien further from player


	public Chase(Entity entity, Entity target) {
		this.entity = entity;
		this.target = target;
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
