package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;

public class CollisionRemovalHandler implements CollisionListener {
    private EntityManager em;

    public CollisionRemovalHandler(EntityManager entityManager) {
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
