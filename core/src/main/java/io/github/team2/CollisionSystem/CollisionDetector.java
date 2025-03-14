package io.github.team2.CollisionSystem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.github.team2.CollisionExtensions.CollisionType;
import io.github.team2.EntitySystem.Entity;

public class CollisionDetector implements ContactListener {

    private List<CollisionListener> listeners = new ArrayList<>();
    private List<ICollisionHandler> handlers = new ArrayList<>();

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (userDataA != null && userDataB != null) {
            Entity a = (Entity) userDataA;
            Entity b = (Entity) userDataB;

            // Handle collision type listeners
            CollisionType type = CollisionType.getCollisionType(a, b);
            if (type != null) {
                notifyListeners(a, b, type);
            }

            // Handle direct contact handlers
            notifyHandlers(a, b, contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Empty implementation
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Empty implementation
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Empty implementation
    }

    public void addListener(CollisionListener listener) {
        listeners.add(listener);
    }

    public void addHandler(ICollisionHandler handler) {
        handlers.add(handler);
    }

    private void notifyListeners(Entity a, Entity b, CollisionType type) {
        for (CollisionListener listener : listeners) {
            listener.onCollision(a, b, type);
        }
    }

    private void notifyHandlers(Entity a, Entity b, Contact contact) {
        for (ICollisionHandler handler : handlers) {
            handler.handleCollision(a, b, contact);
        }
    }
}
