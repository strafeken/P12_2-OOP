package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.Game.Entity.CollisionType;

public interface CollisionListener {
    void onCollision(Entity a, Entity b, CollisionType type);
}
