package io.github.team2.CollisionSystem;

import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityManager;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.GameManager;
import io.github.team2.AudioSystem.AudioManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision resolution between entities
 * Implements CollisionListener interface for collision event handling
 */
public class CollisionResolver implements CollisionListener {
    private EntityManager em;

    public CollisionResolver(EntityManager entityManager) {
        em = entityManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
        try {
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
                case POWERUP_PLAYER:
                    handlePowerUpPlayerCollision(a, b);
                    break;
                default:
                    System.out.println("Unhandled collision: " + a.getEntityType() + " : " + b.getEntityType());
            }
        } catch (Exception e) {
            System.err.println("Error in collision handling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handlePlayerDropCollision(Entity a, Entity b) {
        Entity player = (a.getEntityType() == EntityType.PLAYER) ? a : b;
        Entity drop = (a.getEntityType() == EntityType.DROP) ? a : b;

        AudioManager.getInstance().playSoundEffect("ding");
        if (GameManager.getInstance().getPointsManager() != null) {
            GameManager.getInstance().getPointsManager().addPoints(10);
            //System.out.println("Points added: 10, Total: " + GameManager.getInstance().getPointsManager().getPoints());
        } else {
            System.out.println("PointsManager is null!");
        }
        em.markForRemoval(drop);
    }

    private void handlePowerUpPlayerCollision(Entity a, Entity b) {
        Entity powerUp = (a.getEntityType() == EntityType.POWERUP) ? a : b;
        handlePowerUpEffect(powerUp);
    }

    private void handlePlayerBucketCollision(Entity a, Entity b) {
        Entity player = (a.getEntityType() == EntityType.PLAYER) ? a : b;
        Entity bucket = (a.getEntityType() == EntityType.BUCKET) ? a : b;

        // Reserved for future implementation
        // System.out.println("handle collision: PLAYER | BUCKET");
    }

    private void handleBucketDropCollision(Entity a, Entity b) {
//        Entity bucket = (a.getEntityType() == EntityType.BUCKET) ? a : b;
//        Entity drop = (a.getEntityType() == EntityType.DROP) ? a : b;
//
//        float bucketTopY = bucket.getPosition().y + ((DynamicTextureObject)bucket).getHeight();
//        float dropY = drop.getPosition().y;
//
//        if (Math.abs(dropY - bucketTopY) <= 10) {
//            AudioManager.getInstance().playSoundEffect("ding");
//            GameManager.getInstance().getPointsManager().addPoints(10);
//            em.markForRemoval(drop);
//        }
    }

    private void handleCircleDropCollision(Entity a, Entity b) {
        Entity circle = (a.getEntityType() == EntityType.CIRCLE) ? a : b;
        Entity drop = (a.getEntityType() == EntityType.DROP) ? a : b;
        AudioManager.getInstance().playSoundEffect("ding");
        em.markForRemoval(drop);
    }

    private void handlePowerUpBucketCollision(Entity a, Entity b) {
        Entity powerUp = (a.getEntityType() == EntityType.POWERUP) ? a : b;
        handlePowerUpEffect(powerUp);
    }

    private void handlePowerUpEffect(Entity powerUp) {
        // Count droplets and store for removal
        int dropletCount = 0;
        List<Entity> dropletsToRemove = new ArrayList<>();

        for (Entity entity : em.getEntities()) {
            if (entity.getEntityType() == EntityType.DROP) {
                dropletCount++;
                dropletsToRemove.add(entity);
            }
        }

        // Award points based on droplets
        GameManager.getInstance().getPointsManager().addPoints(dropletCount * 10);

        // Remove all droplets
        for (Entity droplet : dropletsToRemove) {
            em.markForRemoval(droplet);
        }

        // Remove power-up and play sound
        em.markForRemoval(powerUp);
        AudioManager.getInstance().playSoundEffect("ding");
    }
}
