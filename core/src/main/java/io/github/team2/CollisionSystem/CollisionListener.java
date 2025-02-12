package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;


public interface CollisionListener {
    default void beginContact(Contact contact) {}
    default void endContact(Contact contact) {}
    default void preSolve(Contact contact, Manifold oldManifold) {}
    default void postSolve(Contact contact, ContactImpulse impulse) {}
    void onCollision(Entity entityA, Entity entityB, CollisionType type);
}
