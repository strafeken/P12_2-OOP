package abstractEngine.CollisionSystem;

import abstractEngine.EntitySystem.Entity;
import game.Entity.CollisionType;

public interface CollisionListener {
    void onCollision(Entity a, Entity b, CollisionType type);
}
