package io.github.team2.CollisionExtensions;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;

public enum CollisionType {
	CARD_PLAYER;

    public static CollisionType getCollisionType(Entity a, Entity b) {
        if (isPair(a, b, EntityType.CARD, EntityType.PLAYER))
        	return CARD_PLAYER;  
        
        return null;
    }

	private static boolean isPair(Entity a, Entity b, EntityType type1, EntityType type2) {
		return (a.getEntityType() == type1 && b.getEntityType() == type2)
				|| (a.getEntityType() == type2 && b.getEntityType() == type1);
	}
}
