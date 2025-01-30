package io.github.team2;

public class PointsSystem implements CollisionListener
{
    private PointsManager pointsManager;

    public PointsSystem(PointsManager pointsManager)
    {
        this.pointsManager = pointsManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type)
    {
        if (type == CollisionType.BUCKET_DROP)
        {
            pointsManager.addPoints(10);
        }
    }
}
