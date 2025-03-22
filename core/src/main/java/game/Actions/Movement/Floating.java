package game.Actions.Movement;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import abstractengine.entity.DynamicTextureObject;
import abstractengine.entity.Dynamics;
import abstractengine.entity.Entity;
import abstractengine.input.Action;
import abstractengine.utils.DisplayManager;
import game.Entity.EntityType;

public class Floating implements Action {
	private Vector2 velocity;
    private float maxSpeed = 50f;
    private float dampingFactor = 0.99f;  // Slight damping for realistic movement

	private final DynamicTextureObject<?, ?> entity;
	
	public Floating(DynamicTextureObject<?, ?> entity) {
		this.entity = entity;
		
		
        this.velocity = new Vector2(
                MathUtils.random(-maxSpeed, maxSpeed),
                MathUtils.random(-maxSpeed, maxSpeed)
            );
		
		
	}
	
	
    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
	

	@Override
	public void execute() {
		  // Apply zero-gravity physics
        if (entity.getPhysicsBody() != null) {
            // Apply velocity to the body
        	entity.getPhysicsBody().getBody().setLinearVelocity(velocity);

            // Update position based on physics body
        	entity.updateBody();

            // Check for screen boundaries and bounce
            Vector2 position = entity.getPosition();
            boolean bounced = false;

            // Screen bounds checking with bounce effect
            if (position.x < entity.getWidth()/2) {
                position.x = entity.getWidth()/2;
                velocity.x = Math.abs(velocity.x);
                bounced = true;
            } else if (position.x > DisplayManager.getScreenWidth() - entity.getWidth()/2) {
                position.x = DisplayManager.getScreenWidth() - entity.getWidth()/2;
                velocity.x = -Math.abs(velocity.x);
                bounced = true;
            }

            if (position.y < entity.getHeight()/2) {
                position.y = entity.getHeight()/2;
                velocity.y = Math.abs(velocity.y);
                bounced = true;
            } else if (position.y > DisplayManager.getScreenHeight() - entity.getHeight()/2) {
                position.y = DisplayManager.getScreenHeight() - entity.getHeight()/2;
                velocity.y = -Math.abs(velocity.y);
                bounced = true;
            }

            if (bounced) {
                // Apply damping when bouncing off walls
                velocity.scl(dampingFactor);
                // Ensure trash stays in bounds
                entity.getPhysicsBody().setLocation(position.x, position.y);
            }

            // Apply general damping
            velocity.scl(dampingFactor);

            // Ensure objects don't slow down too much
            if (velocity.len() < 20) {
                // Give it a small random impulse to keep things moving
                if (MathUtils.randomBoolean(0.02f)) { // 2% chance per update
                    velocity.add(
                        MathUtils.random(-20f, 20f),
                        MathUtils.random(-20f, 20f)
                    );
                }
            }
        }
	}
	
	
	
}
