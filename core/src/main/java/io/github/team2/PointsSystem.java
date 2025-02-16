package io.github.team2;

import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.CollisionSystem.CollisionType;
import io.github.team2.EntitySystem.Entity;

public class PointsSystem implements CollisionListener {
    private PointsManager pointsManager;

    public PointsSystem(PointsManager pointsManager) {
        this.pointsManager = pointsManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        if (type == CollisionType.PLAYER_DROP) {
            pointsManager.addPoints(10);
        }
    }
}