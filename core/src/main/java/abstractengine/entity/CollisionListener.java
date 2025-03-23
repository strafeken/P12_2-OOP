package abstractengine.entity;

import application.entity.CollisionType;

public interface CollisionListener {
    void onCollision(Entity a, Entity b, CollisionType type);
}
