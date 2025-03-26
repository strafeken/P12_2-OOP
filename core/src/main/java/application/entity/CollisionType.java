package application.entity;

import abstractengine.entity.Entity;

public enum CollisionType {
    ALIEN_PLAYER,
    RECYCLABLE_PLAYER,
    RECYCLABLE_RECYCLINGBIN,
    NON_RECYCLABLE_PLAYER,
    RECYCLING_BIN_PLAYER,
    PLANET_PLAYER;

    public static CollisionType getCollisionType(Entity a, Entity b) {
    	if (isPair(a, b, EntityType.PLANET, EntityType.PLAYER)) {
            return PLANET_PLAYER;
        } else if (isPair(a, b, EntityType.ALIEN, EntityType.PLAYER)) {
            return ALIEN_PLAYER;
        } else if (isPair(a, b, EntityType.RECYCLABLE, EntityType.PLAYER)) {
            return RECYCLABLE_PLAYER;
        } else if (isPair(a, b, EntityType.NON_RECYCLABLE, EntityType.PLAYER)) {
            return NON_RECYCLABLE_PLAYER;
        } else if (isPair(a, b, EntityType.RECYCLING_BIN, EntityType.PLAYER)) {
            return RECYCLING_BIN_PLAYER;
        }
        return null;
    }

    private static boolean isPair(Entity a, Entity b, EntityType type1, EntityType type2) {
        return (a.getEntityType() == type1 && b.getEntityType() == type2) ||
               (a.getEntityType() == type2 && b.getEntityType() == type1);
    }
}
