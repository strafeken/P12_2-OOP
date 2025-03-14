package io.github.team2.CollisionSystem;

import com.badlogic.gdx.physics.box2d.Contact;
import io.github.team2.EntitySystem.Entity;

/**
 * Interface for handling collisions between entities
 * This is different from CollisionListener as it works directly with Box2D Contacts
 */
public interface ICollisionHandler {
    /**
     * Handle a collision between two entities
     * @param entityA The first entity in the collision
     * @param entityB The second entity in the collision
     * @param contact The Box2D contact object containing collision details
     */
    void handleCollision(Entity entityA, Entity entityB, Contact contact);
}
