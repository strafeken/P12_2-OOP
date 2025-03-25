package application.minigame.asteroids;

import abstractengine.entity.CollisionListener;
import abstractengine.entity.Entity;
import application.entity.CollisionType;

/**
 * Handles collisions between ships and asteroids.
 * Follows Single Responsibility Principle by focusing only on collision handling.
 */
public class AsteroidCollisionHandler implements CollisionListener {
   
    public AsteroidCollisionHandler() {
        
    }
    
    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        // Skip collision handling during the grace period
      

        if (type == CollisionType.ASTEROID_PLAYER) {
           
            System.out.println("Ship hit an asteroid!");
        }
    }
}