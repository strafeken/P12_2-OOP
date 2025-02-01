package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;

public interface CollisionListener {

	void onCollision(Entity a, Entity b, CollisionType type);

}
