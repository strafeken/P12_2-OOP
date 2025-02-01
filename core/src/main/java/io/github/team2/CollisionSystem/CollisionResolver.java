package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;

import java.util.ArrayList;
import java.util.List;

import io.github.team2.GameScene;
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
        case POWERUP_BUCKET:
            handlePowerUpBucketCollision(a, b);
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
    
    private void handlePowerUpBucketCollision(Entity a, Entity b) {
        Entity powerUp = (a.getEntityType() == EntityType.POWERUP) ? a : b;
        
        // Count droplets on screen
        int dropletCount = 0;
        List<Entity> dropletsToRemove = new ArrayList<>();
        for (Entity entity : em.getEntities()) {
            if (entity.getEntityType() == EntityType.DROP) {
                dropletCount++;
                dropletsToRemove.add(entity);
            }
        }
        
        // Award points based on droplets
        if (GameScene.getInstance().getPointsManager() != null) {
            GameScene.getInstance().getPointsManager().addPoints(dropletCount * 10);
        }
        // Remove all droplets
        for (Entity droplet : dropletsToRemove) {
            em.markForRemoval(droplet);
        }
        
        // Remove power-up
        em.markForRemoval(powerUp);
        AudioManager.getInstance().playSoundEffect("ding");
    }
}
