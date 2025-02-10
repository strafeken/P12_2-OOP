package io.github.team2.CollisionSystem;

import java.util.ArrayList;
import java.util.List;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;


public class CollisionManager {
    private EntityManager entityManager;
    private List<CollisionListener> collisionListeners;

    public CollisionManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.collisionListeners = new ArrayList<>();
    }

    public void addCollisionListener(CollisionListener listener) {
        collisionListeners.add(listener);
    }

    public void removeCollisionListener(CollisionListener listener) {
        collisionListeners.remove(listener);
    }

    public void handleCollision(Entity entityA, Entity entityB) {
        CollisionType type = CollisionType.getCollisionType(entityA, entityB);
        if (type != null) {
            for (CollisionListener listener : collisionListeners) {
                listener.onCollision(entityA, entityB, type);
            }
        }
    }

    public void handleLayerCollision(Entity entityA, Entity entityB) {
        // Handle layer-specific collisions if needed
    }
}
