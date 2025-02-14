package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;

public interface CollisionListener {
    void onCollision(Entity entityA, Entity entityB, CollisionType type);
}
