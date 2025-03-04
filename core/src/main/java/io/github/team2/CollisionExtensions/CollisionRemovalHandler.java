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
            case CARD_PLAYER:
            	Entity card = getCardEntity(a, b);
            	em.markForRemoval(card);
            	break;
            default:
                System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
        }
    }
    

    private Entity getCardEntity(Entity a, Entity b) {
        if (a.getEntityType() == EntityType.CARD)
            return a;
        else if (b.getEntityType() == EntityType.CARD)
            return b;

        return null;
    }
}
