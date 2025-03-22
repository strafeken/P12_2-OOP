package io.github.team2.Game.Entity;

import io.github.team2.Abstract.CollisionSystem.CollisionListener;
import io.github.team2.Abstract.EntitySystem.Entity;
import io.github.team2.Abstract.EntitySystem.IEntityManager;

public class CollisionRemovalHandler implements CollisionListener {
    private IEntityManager em;

    public CollisionRemovalHandler(IEntityManager entityManager) {
        em = entityManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        switch (type) {
            case NON_RECYCLABLE_PLAYER:
                Entity toBeRemoved = getEntityToRemove(a, b);
                em.markForRemoval(toBeRemoved);
                break;
            case RECYCLABLE_PLAYER:
                // Don't remove recyclables - let RecyclableCarrierHandler handle it
                break;
            case RECYCLING_BIN_PLAYER:
                break;
            case ALIEN_PLAYER:
                break;
            default:
                System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
        }
    }

    private Entity getEntityToRemove(Entity a, Entity b) {
        if (a.getEntityType() == EntityType.RECYCLABLE || a.getEntityType() == EntityType.NON_RECYCLABLE)
            return a;
        else if (b.getEntityType() == EntityType.RECYCLABLE || b.getEntityType() == EntityType.NON_RECYCLABLE)
            return b;

        return null;
    }
}
