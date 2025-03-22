package abstractengine.collision;

import abstractengine.entity.Entity;
import game.Entity.CollisionType;

public interface CollisionListener {
    void onCollision(Entity a, Entity b, CollisionType type);
}
