package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;

public enum CollisionType {
	PLAYER_DROP, PLAYER_BUCKET, BUCKET_DROP, CIRCLE_DROP;

    public static CollisionType getCollisionType(Entity a, Entity b) {
        if (isPair(a, b, EntityType.PLAYER, EntityType.DROP)) return PLAYER_DROP;
        if (isPair(a, b, EntityType.PLAYER, EntityType.BUCKET)) return PLAYER_BUCKET;
        if (isPair(a, b, EntityType.BUCKET, EntityType.DROP)) return BUCKET_DROP;
        if (isPair(a, b, EntityType.CIRCLE, EntityType.DROP)) return CIRCLE_DROP;
        return null;
    }

	private static boolean isPair(Entity a, Entity b, EntityType type1, EntityType type2) {
		return (a.getEntityType() == type1 && b.getEntityType() == type2)
				|| (a.getEntityType() == type2 && b.getEntityType() == type1);
	}
}
