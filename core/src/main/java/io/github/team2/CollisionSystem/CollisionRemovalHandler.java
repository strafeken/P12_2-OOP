package io.github.team2.CollisionSystem;

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
            case PLAYER_DROP:
                Entity drop = getDropEntity(a, b);
                if (drop != null)
                    em.markForRemoval(drop);
                break;
            case CIRCLE_DROP:
                break;
            case CARD_CARD:
                break;
            default:
                System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
        }
    }

    private Entity getDropEntity(Entity a, Entity b) {
        if (a.getEntityType() == EntityType.DROP)
            return a;
        else if (b.getEntityType() == EntityType.DROP)
            return b;

        return null;
    }
}
