package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.AudioSystem.AudioManager;
import io.github.team2.EntitySystem.TextureObject;

public class CollisionResolver implements CollisionListener {

    private EntityManager em;

    public CollisionResolver(EntityManager entityManager) {
        em = entityManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        switch (type) {
        case PLAYER_DROP:
            handlePlayerDropCollision(a, b);
            break;
        case PLAYER_BUCKET:
            handlePlayerBucketCollision(a, b);
            break;
        case BUCKET_DROP:
            handleBucketDropCollision(a, b);
            break;
        case CIRCLE_DROP:
            handleCircleDropCollision(a, b);
            break;
        default:
            System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
        }
    }

    private void handlePlayerDropCollision(Entity a, Entity b) {
//        System.out.println("handle collision: PLAYER | DROP");
    }

    private void handlePlayerBucketCollision(Entity a, Entity b) {
//        System.out.println("handle collision: PLAYER | BUCKET");
    }

    private void handleBucketDropCollision(Entity a, Entity b) {
        Entity bucket = (a.getEntityType() == EntityType.BUCKET) ? a : b;
        Entity drop = (a.getEntityType() == EntityType.DROP) ? a : b;
        
        // Get the top edge Y position of the bucket
        float bucketTopY = bucket.getPosition().y + ((TextureObject)bucket).getHeight();
        // Get drop Y position
        float dropY = drop.getPosition().y;
        
        // Only count collision if drop hits near the top edge of bucket (within 10 pixels)
        if (Math.abs(dropY - bucketTopY) <= 10) {
            // Play the ding sound effect when bucket top collides with droplet
            AudioManager.getInstance().playSoundEffect("ding");
            em.markForRemoval(drop);
        }
    }

    private void handleCircleDropCollision(Entity a, Entity b) {
//        System.out.println("handle collision: CIRCLE | TRIANGLE");
    }
}