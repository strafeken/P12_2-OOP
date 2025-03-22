package abstractengine.collision;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import abstractengine.entity.Entity;
import game.entity.CollisionType;

public class CollisionDetector implements ContactListener {

    private List<CollisionListener> listeners = new ArrayList<>();

    // Add Box2DDebugRenderer field
    private Box2DDebugRenderer debugRenderer;
    
    public CollisionDetector() {
        // Initialize the debug renderer
        this.debugRenderer = new Box2DDebugRenderer();
    }
    
    // Add a method to render the debug visualization of physics bodies
    public void renderDebug(World world, Matrix4 projectionMatrix) {
        if (debugRenderer != null) {
            debugRenderer.render(world, projectionMatrix);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (userDataA != null && userDataB != null &&
            userDataA instanceof Entity && userDataB instanceof Entity) {
            Entity a = (Entity) userDataA;
            Entity b = (Entity) userDataB;

            // Handle collision type listeners
            CollisionType type = CollisionType.getCollisionType(a, b);
            if (type != null) {
                notifyListeners(a, b, type);
            }
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

    private void notifyListeners(Entity a, Entity b, CollisionType type) {
        for (CollisionListener listener : listeners) {
            listener.onCollision(a, b, type);
        }
    }
    
    // Add a dispose method to clean up resources
    public void dispose() {
        if (debugRenderer != null) {
            debugRenderer.dispose();
            debugRenderer = null;
        }
    }
}
