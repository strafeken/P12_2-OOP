package game.Entity;

import abstractengine.collision.CollisionListener;
import abstractengine.entity.Entity;
import abstractengine.entity.IEntityManager;
import game.Manager.PlayerStatus;
import game.Trash.RecyclableTrash;

public class RecyclableCarrierHandler implements CollisionListener {
    private IEntityManager entityManager;

    public RecyclableCarrierHandler(IEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
    	if (type != CollisionType.RECYCLABLE_PLAYER)
    		return;
    	
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        // Check if one entity is the player and the other is recyclable trash
        Entity player = null;
        Entity recyclable = null;

        if (a.getEntityType() == EntityType.PLAYER && b.getEntityType() == EntityType.RECYCLABLE) {
            player = a;
            recyclable = b;
        } else if (b.getEntityType() == EntityType.PLAYER && a.getEntityType() == EntityType.RECYCLABLE) {
            player = b;
            recyclable = a;
        } else {
            // Not a player-recyclable collision
            return;
        }

        // Only allow pickup if player isn't already carrying recyclable trash
        if (!playerStatus.isCarryingRecyclable()) {
            // Set carrying status
            playerStatus.setCarriedItem(recyclable);

            // Remove the recyclable from the world (it's now "carried")
            entityManager.markForRemoval(recyclable);

            System.out.println("Player picked up recyclable trash: " +
                              ((recyclable instanceof RecyclableTrash) ?
                               ((RecyclableTrash)recyclable).getRecyclableType() : "unknown"));
        } else {
            // Player already carrying something, can't pickup
            System.out.println("Player already carrying recyclable trash. Return it first!");
        }
    }
}
