package io.github.team2.CollisionExtensions;

import com.badlogic.gdx.physics.box2d.Contact;

import io.github.team2.PlayerStatus;
import io.github.team2.CollisionSystem.ICollisionHandler;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.EntitySystem.IEntityManager;
import io.github.team2.Trash.RecyclableTrash;

public class RecyclableCarrierHandler implements ICollisionHandler {
    private IEntityManager entityManager;

    public RecyclableCarrierHandler(IEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void handleCollision(Entity entityA, Entity entityB, Contact contact) {
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        // Check if one entity is the player and the other is recyclable trash
        Entity player = null;
        Entity recyclable = null;

        if (entityA.getEntityType() == EntityType.PLAYER && entityB.getEntityType() == EntityType.RECYCLABLE) {
            player = entityA;
            recyclable = entityB;
        } else if (entityB.getEntityType() == EntityType.PLAYER && entityA.getEntityType() == EntityType.RECYCLABLE) {
            player = entityB;
            recyclable = entityA;
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
