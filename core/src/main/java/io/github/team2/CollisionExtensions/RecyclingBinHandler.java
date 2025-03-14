package io.github.team2.CollisionExtensions;

import com.badlogic.gdx.physics.box2d.Contact;

import io.github.team2.PlayerStatus;
import io.github.team2.PointsManager;
import io.github.team2.CollisionSystem.ICollisionHandler;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.EntitySystem.EntityType;
import io.github.team2.Trash.RecyclableTrash;

public class RecyclingBinHandler implements ICollisionHandler {
    private PointsManager pointsManager;

    public RecyclingBinHandler(PointsManager pointsManager) {
        this.pointsManager = pointsManager;
    }

    @Override
    public void handleCollision(Entity entityA, Entity entityB, Contact contact) {
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        // Check if one entity is the player and the other is a recycling bin
        if ((entityA.getEntityType() == EntityType.PLAYER && entityB.getEntityType() == EntityType.RECYCLING_BIN) ||
            (entityB.getEntityType() == EntityType.PLAYER && entityA.getEntityType() == EntityType.RECYCLING_BIN)) {

            // Check if the player is carrying recyclable trash
            if (playerStatus.isCarryingRecyclable()) {
                Entity carriedItem = playerStatus.getCarriedItem();

                // Determine points based on the type of recyclable
                int points = 0;
                if (carriedItem instanceof RecyclableTrash) {
                    RecyclableTrash recyclable = (RecyclableTrash) carriedItem;

                    // Award points based on type
                    switch (recyclable.getRecyclableType()) {
                        case PAPER:
                            points = 10;
                            break;
                        case PLASTIC:
                            points = 15;
                            break;
                        case GLASS:
                            points = 20;
                            break;
                        case METAL:
                            points = 25;
                            break;
                        default:
                            points = 5;
                    }

                    // Add points to the score
                    pointsManager.addPoints(points);

                    System.out.println("Recycled item! +" + points + " points");

                    // Clear the carried item
                    playerStatus.setCarriedItem(null);
                }
            }
        }
    }
}
