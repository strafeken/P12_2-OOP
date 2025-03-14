package io.github.team2;

import com.badlogic.gdx.math.Vector2;

import io.github.team2.AlienBehaviour.Move;
import io.github.team2.AlienBehaviour.State;
import io.github.team2.EntitySystem.DynamicTextureObject;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;

public class Alien extends DynamicTextureObject<AlienBehaviour.State, AlienBehaviour.Move> {
    private Entity targetPlayer;
    private float chaseSpeed = 30f; // Reduced from 60f to make alien slower
    private float maxDistance = 500f; // Maximum distance to chase player
    private float minDistance = 100f; // Increased from 50f to keep alien further from player

    // Add respawn position to remember where to place the alien when respawning
    private Vector2 respawnPosition;

    public Alien(EntityType type, String texture, Vector2 size, Vector2 position, Vector2 direction, Vector2 rotation,
            float speed, State state, Move actionState) {
        super(type, texture, size, position, direction, rotation, speed, state, actionState);
        // Store initial position for respawning
        this.respawnPosition = new Vector2(position);
    }

    @Override
    public void update() {
        if (targetPlayer != null && getPhysicsBody() != null) {
            // Get player and alien positions
            Vector2 playerPos = targetPlayer.getPosition();
            Vector2 alienPos = getPosition();

            // Calculate direction to player
            Vector2 direction = new Vector2(playerPos.x - alienPos.x, playerPos.y - alienPos.y);
            float distance = direction.len();

            // Only move if player is within range and not too close
            if (distance < maxDistance && distance > minDistance) {
                // Normalize and scale by chase speed
                direction.nor();
                direction.scl(chaseSpeed);

                // Apply movement
                getPhysicsBody().getBody().setLinearVelocity(direction);
            } else if (distance <= minDistance) {
                // Stop if too close
                getPhysicsBody().getBody().setLinearVelocity(0, 0);
            } else {
                // Slow down if too far
                getPhysicsBody().getBody().setLinearVelocity(0, 0);
            }
        }

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
    }

    public Entity getTargetPlayer() {
        return targetPlayer;
    }
}
