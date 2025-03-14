package io.github.team2.CollisionExtensions;

import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.PointsManager;

public class PointsSystem implements CollisionListener {
    private PointsManager pointsManager;

    public PointsSystem(PointsManager pointsManager) {
        this.pointsManager = pointsManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
//        if (type == CollisionType.CARD_PLAYER) {
//            pointsManager.addPoints(10);
//        }
    }
}