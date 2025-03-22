package io.github.team2.Game.Entity;

import io.github.team2.CollisionSystem.CollisionListener;
import io.github.team2.EntitySystem.Entity;
import io.github.team2.Game.Manager.PlayerStatus;
import io.github.team2.Game.Manager.PointsManager;
import io.github.team2.Game.Trash.RecyclableTrash;
import io.github.team2.Game.Trash.RecyclingBin;

public class RecyclingBinHandler implements CollisionListener {
    private PointsManager pointsManager;

    public RecyclingBinHandler(PointsManager pointsManager) {
        this.pointsManager = pointsManager;
    }

    @Override
    public void onCollision(Entity a, Entity b, CollisionType type) {
    	if (type != CollisionType.RECYCLING_BIN_PLAYER)
    		return;
    	
        PlayerStatus playerStatus = PlayerStatus.getInstance();

        // Check if one entity is the player and the other is a recycling bin
        if ((a.getEntityType() == EntityType.PLAYER && b.getEntityType() == EntityType.RECYCLING_BIN) ||
            (b.getEntityType() == EntityType.PLAYER && a.getEntityType() == EntityType.RECYCLING_BIN)) {
        	
        	// check which is bin
        	Entity bin = (a.getEntityType() == EntityType.RECYCLING_BIN) ? a : b;
        	
            // Check if the player is carrying recyclable trash
            if (playerStatus.isCarryingRecyclable()) {
                Entity carriedItem = playerStatus.getCarriedItem();

                // Determine points based on the type of recyclable
                int points = 0;
                if (bin instanceof RecyclingBin && carriedItem instanceof RecyclableTrash) {
                    RecyclableTrash recyclable = (RecyclableTrash) carriedItem;
                    
                    RecyclingBin recyclingBin = (RecyclingBin) bin;
                    	
                    if (recyclingBin.accepts(recyclable)) {
                    	
                        

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
                    
                    
                    } else {
                    	  
                    	System.out.println("Wrong Bin");
                    }
                     
                    
                    

                }
            }
        }
    }
}
