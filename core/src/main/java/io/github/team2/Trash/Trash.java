package io.github.team2.Trash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.Trash.TrashBehaviour.Move;
import io.github.team2.Trash.TrashBehaviour.State;
import io.github.team2.Utils.DisplayManager;

public abstract class Trash extends DynamicTextureObject<TrashBehaviour.State, TrashBehaviour.Move> {
    private Vector2 velocity;
    private float maxSpeed = 50f;
    private float dampingFactor = 0.99f;  // Slight damping for realistic movement

    public Trash(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
            float speed, State state, Move actionState) {
        super(type, texture, size, position, direction, rotation, speed, state, actionState);

        // Initialize with random velocity for zero-gravity effect
        this.velocity = new Vector2(
            MathUtils.random(-maxSpeed, maxSpeed),
            MathUtils.random(-maxSpeed, maxSpeed)
        );
    }

    @Override
    public void update() {
        // Apply zero-gravity physics
        if (getPhysicsBody() != null) {
            // Apply velocity to the body
            getPhysicsBody().getBody().setLinearVelocity(velocity);

            // Update position based on physics body
            updateBody();

            // Check for screen boundaries and bounce
            Vector2 position = getPosition();
            boolean bounced = false;

            // Screen bounds checking with bounce effect
            if (position.x < getWidth()/2) {
                position.x = getWidth()/2;
                velocity.x = Math.abs(velocity.x);
                bounced = true;
            } else if (position.x > DisplayManager.getScreenWidth() - getWidth()/2) {
                position.x = DisplayManager.getScreenWidth() - getWidth()/2;
                velocity.x = -Math.abs(velocity.x);
                bounced = true;
            }

            if (position.y < getHeight()/2) {
                position.y = getHeight()/2;
                velocity.y = Math.abs(velocity.y);
                bounced = true;
            } else if (position.y > DisplayManager.getScreenHeight() - getHeight()/2) {
                position.y = DisplayManager.getScreenHeight() - getHeight()/2;
                velocity.y = -Math.abs(velocity.y);
                bounced = true;
            }

            if (bounced) {
                // Apply damping when bouncing off walls
                velocity.scl(dampingFactor);
                // Ensure trash stays in bounds
                getPhysicsBody().setLocation(position.x, position.y);
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

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
