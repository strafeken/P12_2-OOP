package abstractEngine.CollisionSystem;

import abstractEngine.EntitySystem.Entity;
import io.github.team2.Game.Entity.CollisionType;

public interface CollisionListener {
    void onCollision(Entity a, Entity b, CollisionType type);
}
