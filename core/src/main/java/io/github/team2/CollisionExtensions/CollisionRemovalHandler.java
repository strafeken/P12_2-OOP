package io.github.team2.CollisionExtensions;

import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.IEntityManager;

public class CollisionRemovalHandler implements CollisionListener {
    private IEntityManager em;

    public CollisionRemovalHandler(IEntityManager entityManager) {
        em = entityManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        switch (type) {
            case RECYCLABLE_PLAYER:
            case NON_RECYCLABLE_PLAYER:
            	Entity toBeRemoved = getEntityToRemove(a, b);
            	em.markForRemoval(toBeRemoved);
            	break;
            case RECYCLING_BIN_PLAYER:
                System.out.println("ERROR");
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
